(ns sigel.xpath.core
  "Functions for selecting and matching things in an XML file with XPath."
  (:require [sigel.core :as saxon]
            [sigel.protocols :refer :all])
  (:refer-clojure :exclude [ns])
  (:import
   (net.sf.saxon.s9api Axis
                       XdmAtomicValue
                       XdmNode
                       XdmValue
                       XPathSelector
                       XPathCompiler)
   (net.sf.saxon.pattern Pattern)))

;; An XPath namespace composed of a string prefix and a string URI.
(defrecord Namespace [prefix uri])

(defn static-context
  "Get the static context of an [XPathCompiler](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XPathCompiler.html)."
  [compiler]
  (.getUnderlyingStaticContext compiler))

(defn package-data
  "Get the package data of a StaticContext."
  [static-context]
  (.getPackageData static-context))

(defn early-evaluation-context
  "Make a new XPath evaluation context from a static context."
  [static-context]
  (.makeEarlyEvaluationContext static-context))

(defn ns
  "Create a Namespace record composed of a string prefix and a string URI."
  [prefix uri]
  (->Namespace prefix uri))

(defn set-default-namespace!
  "Set the default namespace URI of an [XPathCompiler](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XPathCompiler.html).

  Setting the default namespace URI allows you to omit the namespace prefix from
  your XPath expressions.

  Example:

  ```
  (def my-compiler (xpath/compiler))

  (set-default-namespace! my-compiler \"bar\")

  (xpath/select my-compiler \"<foo xmlns=\\\"bar\\\"><baz/></foo>\" \"foo/baz\")
  ;;=> #object[net.sf.saxon.s9api.XdmNode 0x2e91fb2b \"<baz xmlns=\\\"bar\\\"/>\"]
  ```"
  [compiler uri]
  (when ((complement empty?) uri)
    (.declareNamespace compiler "" uri))
  compiler)

(defn default-namespace
  "Get the default namespace URI of an [XPathCompiler](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XPathCompiler.html)."
  [compiler]
  (.. compiler (getUnderlyingStaticContext) (getDefaultElementNamespace)))

(defn declare-namespace!
  "Teach an [XPathCompiler](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XPathCompiler.html)
  about a namespace.

  You can give the namespace either as a string prefix and a string URI or
  as a Namespace record."
  ([compiler prefix uri]
   (doto compiler (.declareNamespace prefix uri)))
  ([compiler ns-decl]
   (declare-namespace! compiler (:prefix ns-decl) (:uri ns-decl))))

(defn compiler
  "Make a new [XPathCompiler](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XPathCompiler.html).

  Optionally, give the default namespace and additional namespace declarations
  you want the XPathCompiler to know about."
  ([processor default-namespace-uri namespaces]
   (let [compiler (doto (.newXPathCompiler processor)
                    (set-default-namespace! default-namespace-uri))]
     (doseq [namespace namespaces] (declare-namespace! compiler namespace))
     compiler))
  ([] (compiler saxon/processor nil nil)))

(def ^:dynamic *compiler*
  "A default XPath compiler.

  If you don't pass in your own [XPathCompiler](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XPathCompiler.html)
  instance when compiling a stylesheet, Sigel uses this instance."
  (compiler))

(defn set-variable
  "Set a variable on an [XPathSelector](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XPathSelector.html).

  The name of the variable must implement the [[saxon/QNameable]] protocol.

  The value of the variable must implement the [[saxon/XmlValue]] protocol."
  [selector [name value]]
  (doto selector (.setVariable (->qname name) (->xdmvalue value))))

(defn declare-variables!
  "Declare variables on an [XPathCompiler](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XPathCompiler.html).

  This function does not set the value of a variable. Rather, it tells the
  XPathCompiler that the value of a variable will be set later."
  [compiler variables]
  (doseq [[name _] variables] (.declareVariable compiler (->qname name)))
  compiler)

(defn- bind-selector
  [compiler xpath-type pattern context bindings]
  (let [compiler (declare-variables! compiler bindings)
        selector (.load (if (= xpath-type :pattern)
                          (.compilePattern compiler pattern)
                          (.compile compiler pattern)))]
    (doseq [binding bindings] (set-variable selector binding))
    (doto selector (.setContextItem (build context)))))

(defn ->seq
  "Return a seq on the nodes in the given document."
  [node]
  (iterator-seq (.axisIterator node Axis/DESCENDANT_OR_SELF)))

(defn match
  "Return a sequence of values that match an XPath pattern in the given XML
  context.

  Optionally, give a map of XPath variable bindings."
  ([compiler context pattern bindings]
   (let [selector (bind-selector compiler :pattern pattern context bindings)]
     (XdmValue.
       (filter (fn [node]
                 (.setContextItem selector node)
                 (.effectiveBooleanValue selector))
               (->seq (build context))))))
  ([context pattern]
   (match *compiler* context pattern nil)))

(defn is?
  "Given an XML context and an XPath expression, return a boolean that
  indicates whether the expression evaluates to true in that context.

  Optionally, give a map of XPath variable bindings.

  Example:

  (xpath/is? compiler (saxon/build \"<one>1</one>\") \"xs:int(one) + 2 eq 3\")
  ; => true"
  ([compiler context expression bindings]
   (.effectiveBooleanValue
     (bind-selector compiler :expression expression context bindings)))
  ([context expression]
   (is? *compiler* context expression nil)))

(defn select
  "Return a sequence of values that match an XPath expression in the given
  XML context."
  ([compiler context expression bindings]
   (.evaluate (bind-selector compiler :expression expression context bindings)))
  ([context expression]
   (select *compiler* context expression nil)))

(defn value-of
  "Return the value of an XPath expression evaluated in the given context.

  If the value is an atomic value, return the value as a Java object of the
  equivalent type. If it's a node, get the string value of the node."
  ([compiler context expression bindings]
   (let [value (.. (bind-selector compiler :expression expression context bindings)
                   (evaluateSingle))]
     (saxon/xdmvalue->object value)))
  ([context expression]
   (value-of *compiler* context expression nil)))

(defn pattern
  "Make an XPath pattern from a string."
  ([compiler string]
   (let [static-context (static-context compiler)
         package-data (package-data static-context)]
     (Pattern/make string static-context package-data)))
  ([string]
   (pattern *compiler* string)))

;; FIXME: Bindings?
(defn matches?
  "Check whether a node matches a XPath pattern (compiled with [[pattern]])."
  ([compiler node pattern bindings]
   (.matches pattern
             (.getUnderlyingNode (build node))
             (-> compiler static-context early-evaluation-context)))
  ([node pattern]
   (matches? *compiler* node pattern nil)))
