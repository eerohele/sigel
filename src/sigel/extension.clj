(ns sigel.extension
  "Facilities for defining Saxon [integrated extension functions](http://www.saxonica.com/html/documentation/extensibility/integratedfunctions/)."
  (:gen-class)
  (:require [sigel.utils :as u]
            [sigel.protocols :refer :all]
            [sigel.core :as saxon])
  (:import (net.sf.saxon.s9api ExtensionFunction
                               ItemType
                               OccurrenceIndicator
                               SequenceType
                               XdmAtomicValue
                               XdmItem
                               XPathCompiler
                               XsltCompiler)))

(defn make-sequence-type
  "A wrapper for [`SequenceType/makeSequenceType`](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/SequenceType.html). Takes arguments in the reverse
  order to the original method because it's more pleasant to write
  `[:one :boolean]` than `[:boolean :one]`."
  [occurrence-indicator item-type]
  {:pre [(some #{occurrence-indicator}
               (u/get-static-fields-as-keywords OccurrenceIndicator))
         (some #{item-type}
               (u/get-static-fields-as-keywords ItemType))]}
  (SequenceType/makeSequenceType
    (u/get-class-constant ItemType item-type)
    (u/get-class-constant OccurrenceIndicator occurrence-indicator)))

(defn function
  "Define a Saxon integrated extension function.

  Example:

  ```
  (require '[sigel.extension :as ext])

  (ext/function
   [\"local\" \"double\"] ;; the namespace URI and local name
   [[:one :int]]  ;; the argument types
   [:one :double] ;; the result type
   (fn [x] (double (* x 2)))) ;; body
  ```

  The argument and result types of the extension function are defined as tuples
  of an [OccurrenceIndicator](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/OccurrenceIndicator.html)
  and [ItemType](www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/ItemType.html), but
  as Clojure keywords instead of Java CONSTANTS.

  For example, `ONE` becomes `:one`, `ANY_NODE` becomes `:any-node` etc.
  "
  [qname arg-types result-type body]
  (reify ExtensionFunction
    (getName [this]
      (->qname qname))
    (getResultType [this]
      (apply make-sequence-type result-type))
    (getArgumentTypes [this]
      (into-array (map (partial apply make-sequence-type) arg-types)))
    (call [this args]
      (let [result (apply body (map saxon/xdmvalue->object args))]
        (if (instance? XdmItem result)
          result
          (XdmAtomicValue. result))))))

(defn register-extension-function!
  "Register a Saxon integrated extension function.

  You can use [[function]] to define an integrated extension function.

  `host` can be a Saxon `Processor`, `XPathCompiler`, or `XsltCompiler`."
  [host function]
  (let [processor (if (or (instance? XPathCompiler host)
                          (instance? XsltCompiler host))
                    (.getProcessor host)
                    host)]
    (doto processor (.registerExtensionFunction function))))
