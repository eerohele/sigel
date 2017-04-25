(ns sigel.core
  "Sigel is a Clojure interface to the [Saxon](http://www.saxonica.com) XSLT and
  XPath implementations."
  (:gen-class)
  (:import (net.sf.saxon Configuration)
           (net.sf.saxon.s9api DocumentBuilder Processor)))

(def ^Configuration configuration
  "A default Saxon [Configuration](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/Configuration.html)."
  (Configuration.))

(def ^Processor processor
  "A default Saxon [Processor](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/Processor.html)."
  (Processor. configuration))

(def ^DocumentBuilder builder
  "A Saxon [DocumentBuilder](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/DocumentBuilder.html)."
  (.newDocumentBuilder processor))

(defn xdmvalue->object
  "Get the value of an XdmValue as the nearest equivalent Java object.

  If the XdmValue is a node, return the string value of that node."
  [value]
  (if (.isAtomicValue value)
    (.getValue value)
    (.getStringValue value)))
