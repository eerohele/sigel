(ns sigel.xslt.core
  "Write and execute XSLT transformations with Clojure.

  ## XML namespaces

  Scenario:

  - Your source XML document is in an XML namespace.
  - You want to transform that document with Sigel into the same namespace.
  - You want to write literal XML elements in your transformation, instead of
    using `xsl:element`.

  For example, you want to transform this:

  ```xml
  <a xmlns=\"my-ns\"/>
  ```

  To this:

  ```xml
  <b xmlns=\"my-ns\"/>
  ```

  In your XSLT transformation, you want to write something like this:

  ```
  (xsl/template {:match \"a\"} [:b])
  ```

  Instead of this:

  ```
  (xsl/template {:match \"a\"}
    (xsl/element {:name \"b\"}))
  ```

  If you create a literal element with `[:b]`, though, clojure.data.xml puts it
  into the empty namespace. That means that the XSLT stylesheet Sigel produces
  will have this:

  ```xml
  <xsl:template match=\"a\">
    <!-- note the empty xmlns namespace declaration -->
    <b xmlns=\"\"/>
  </xsl:template>
  ```

  When what you actually want is this:

  ```xml
  <xsl:template match=\"a\">
    <b/>
  </xsl:template>
  ```

  To do that, you must first set up your namespace with
  `clojure.data.xml/alias-uri`:

  ```clojure
  (xml/alias-uri 'my-ns \"my-ns-uri\")
  ```

  You then need to set the `xmlns` attribute of your stylesheet and use the
  `my-ns` prefix when emitting literal XML elements:

  ```clojure
  (xsl/stylesheet {:version 3.0 :xmlns \"my-ns-uri\"}
    (xsl/template {:match \"a\"} [::my-ns/b]))
  ```"
  (:require [clojure.data.xml :as xml]
            [sigel.saxon :as saxon]
            [sigel.protocols :refer :all]
            [sigel.utils :as u]
            [sigel.xslt.components :as components]
            [clojure.edn :as edn]
            [clojure.walk :as walk]
            [clojure.java.io :as io])
  (:import (java.io StringWriter PushbackReader)
           (net.sf.saxon.s9api XsltCompiler Serializer XsltExecutable XdmDestination Xslt30Transformer)))

(defn compiler
  "Create a new [XsltCompiler](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XsltCompiler.html)."
  []
  (.newXsltCompiler saxon/processor))

(def ^:dynamic ^XsltCompiler *compiler*
  "A default XSLT compiler.

  If you don't pass in your own [XsltCompiler](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XsltCompiler.html)
  instance when compiling a stylesheet, Sigel uses this instance."
  (compiler))

(defn compile-source
  "Compile an XSLT stylesheet from a [Source](https://docs.oracle.com/javase/8/docs/api/javax/xml/transform/Source.html)."
  ([compiler stylesheet]
   (.compile compiler stylesheet))
  ([stylesheet]
   (compile-source *compiler* stylesheet)))

(defn compile-sexp
  "Compile an XSLT stylesheet written in Clojure.

  If you don't pass in an [XsltCompiler](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XsltCompiler.html),
  Sigel uses [[*compiler*]].

  You can execute the compiled stylesheet with [[transform]].

  Example:

  ```
  ;; Define an XSLT stylesheet.
  (def stylesheet
    (xsl/stylesheet {:version 3.0}
      (xsl/template {:match \"a\"} [:b])))

  ;; Compile the stylesheet.
  (xslt/compile-sexp stylesheet)
  ;;=> #object[net.sf.saxon.s9api.XsltExecutable 0x1098b3aa \"net.sf.saxon.s9api.XsltExecutable@1098b3aa\"]
  ```"
  ([compiler stylesheet]
   (let [writer (xml/emit (xml/sexp-as-element stylesheet) (StringWriter.))]
     (.. compiler
         (compile (u/string->source (str writer))))))
  ([stylesheet]
   (compile-sexp *compiler* stylesheet)))

(defn- update-xsl-namespaces
  [stylesheet]
  (walk/postwalk
    #(if (and (keyword? %) (= (namespace %) "xsl"))
       (keyword "xmlns.http%3A%2F%2Fwww.w3.org%2F1999%2FXSL%2FTransform" (name %))
       %)
    stylesheet))

(defn compile-edn
  "Compile a stylesheet defined in an EDN file.

  To write an EDN stylesheet, instead of calling the functions in the
  `sigel.xslt.elements` namespace, use the `:xsl` namespace prefix like this:

  ```
  ;; a.edn
  [:xsl/stylesheet {:version 3.0}
    [:xsl/template {:match \"a\"} [:b]]]
  ```

  Then compile the stylesheet and transform some XML with it:

  ```
  (xslt/transform (xslt/compile-edn \"resources/examples/a.edn\") \"<a/>\")
  ;;=> #object[net.sf.saxon.s9api.XdmNode 0xf2a49c4 \"<b/>\"]
  ```"
  [path]
  (with-open
    [reader (PushbackReader. (io/reader path))]
    (-> (edn/read reader) update-xsl-namespaces compile-sexp)))

(defn- ->param-map
  [params]
  (reduce merge
          (map (fn [[k v]] (hash-map (->qname k) (->xdmvalue v))) params)))

(defn- get-transformer
  [executable params]
  (let [transformer (.load30 executable)]
    (when (seq params) (.setStylesheetParameters transformer (->param-map params)))
    transformer))

(def ^:private identity-xslt3-transformer
  (get-transformer
    (compile-sexp components/identity-transformation) nil))

(defn- sequentify
  [x]
  (if (sequential? x)
    x
    [x]))

(defn apply-templates
  ([^Xslt30Transformer transformer source]
   (.applyTemplates transformer (build source)))
  ([transformer source destination]
   (.applyTemplates transformer (build source) destination)
   destination))

(defn pipeline
  [[ex & exs] params source destination]
  (if (nil? ex)
    (apply-templates identity-xslt3-transformer source destination)
    (let [transformer (get-transformer ex params)]
      (if (empty? exs)
        (apply-templates transformer source destination)
        (pipeline exs params (apply-templates transformer source) destination)))))

(defn transform
  "Execute one or more XSLT transformations on the given source file.

  Example:

  ```
  ;; Define an XSLT transformation.
  (def stylesheet
    (xslt3-identity (xsl/template {:match \"a\"} [:b])))

  ;; Compile the stylesheet and use it to transform \"<a/>\".
  (xslt/transform (xslt/compile-sexp stylesheet) \"<a/>\")
  ;;=> \"<b/>\"
  ```

  To execute multiple transformations in sequence, pass a sequence of compiled
  stylesheets.

  Example:

  ```
  (def stylesheet-1
    (xslt3-identity (xsl/template {:match \"a\"} [:b])))

  (def stylesheet-2
    (xslt3-identity (xsl/template {:match \"b\"} [:c])))

  (def compiled-stylesheets
    [(xslt/compile-sexp stylesheet-1) (xslt/compile-sexp stylesheet-2)])

  (xslt/transform compiled-stylesheets \"<a/>\")
  ;;=> \"<c/>\"
  ```

  You can pass a map of parameters to the XSLT stylesheet.

  Example:

  ```
  (def xslt
    (xsl/stylesheet {:version 3.0 :xmlns:xs \"http://www.w3.org/2001/XMLSchema\"}
      (xsl/param {:name \"factor\" :as \"xs:integer\"})

      (xsl/template {:match \"num\"}
        (xsl/copy (xsl/value-of {:select \"xs:int(.) * $factor\"})))))

  (xslt/transform (xslt/compile-sexp xslt) {:factor 10} \"<num>1</num>\")
  ;;=> \"<num>10</num>\"
  ```
  "
  ([executables params source]
   (.getXdmNode
     (pipeline (sequentify executables) params source (XdmDestination.))))
  ([executables source]
   (transform executables nil source)))

(defn transform-to-file
  "Execute one or more XSLT transformations on the given source file.

  See also [[transform]].

  Example:

  ```
  ;; Define an XSLT transformation.
  (def stylesheet
    (xslt3-identity (xsl/template {:match \"a\"} [:b])))

  ;; Compile the stylesheet and use it to transform \"<a/>\".
  ;;
  ;; Save the result of the transformation into \"/tmp/b.xml\".
  (xslt/transform-to-file (xslt/compile-sexp stylesheet)
                          \"<a/>\"
                          (io/file \"/tmp/b.xml\")
  ```"
  ([executables params source target]
   (let [serializer (.newSerializer saxon/processor target)]
     (pipeline (sequentify executables) params source serializer)))
  ([executables source target]
   (transform-to-file executables nil source target)))
