(ns sigel.xslt.core
  "Write XSLT transformations with Clojure."
  (:require [clojure.data.xml :as xml]
            [sigel.core :as saxon]
            [sigel.protocols :refer :all]
            [sigel.utils :as u])
  (:import (java.io StringWriter)
           (net.sf.saxon.s9api XsltCompiler Serializer XsltExecutable)))

(defn compiler
  "Create a new XSLT compiler."
  []
  (.newXsltCompiler saxon/processor))

(def ^:dynamic ^XsltCompiler *compiler*
  "A default XSLT compiler.

  If you don't pass in your own [XsltCompiler](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XsltCompiler.html)
  instance when compiling a stylesheet, Sigel uses this instance."
  (compiler))

(defn compile
  "Compile an XSLT stylesheet from a [Source](https://docs.oracle.com/javase/8/docs/api/javax/xml/transform/Source.html)."
  ([compiler stylesheet]
   (.compile compiler stylesheet))
  ([stylesheet]
   (compile *compiler* stylesheet)))

(defn compile-sexp
  "Compile an XSLT stylesheet written in Clojure.

  If you don't pass in an XsltCompiler, Sigel uses [[*compiler*]].

  Example:

  ```
  ```"
  ([compiler stylesheet]
   (let [writer (xml/emit (xml/sexp-as-element stylesheet) (StringWriter.))]
     (.. compiler
         (compile (u/string->source (str writer))))))
  ([stylesheet]
   (compile-sexp *compiler* stylesheet)))

(defn- ->param-map
  [params]
  (reduce merge
          (map (fn [[k v]] (hash-map (->qname k) (->xdmvalue v))) params)))

(defn- execute-transformation
  [executable params source]
  (let [transformer (.load30 executable)]
    (when (seq params) (.setStylesheetParameters transformer params))
    (.applyTemplates transformer (build source))))

(defn- internal-transform
  [executables params source conclude]
  (let [xdmnode (build source)
        parameters (->param-map params)]
    (cond (instance? XsltExecutable executables)
          (execute-transformation executables parameters xdmnode)
          (empty? executables)
          (conclude xdmnode)
          :else
          (internal-transform
           (rest executables)
           params
           (execute-transformation (first executables) parameters xdmnode)
           conclude))))

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
  ```"
  ([executables params source]
   (internal-transform executables params source identity))
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
  ;; Store the result of the transformation into \"/tmp/b.xml\".
  (xslt/transform-to-file (xslt/compile-sexp stylesheet)
                          \"<a/>\"
                          (io/file \"/tmp/b.xml\")
  ```"
  ([executables params source target]
   (letfn [(serialize-to-file [xdmnode]
             (let [serializer (.newSerializer saxon/processor target)]
               (.serializeNode serializer xdmnode)))]
     (internal-transform executables params source serialize-to-file)))
  ([executables source target]
   (transform-to-file executables nil source target)))
