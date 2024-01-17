(ns sigel.saxon
  (:import (net.sf.saxon Configuration)
           (net.sf.saxon.lib FeatureKeys)
           (net.sf.saxon.s9api DocumentBuilder Processor)))


(def ^Configuration configuration
  "A default Saxon [Configuration](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/Configuration.html)."
  (Configuration.))


(def ^Processor processor
  "A default Saxon [Processor](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/Processor.html)."
  (doto (Processor. configuration)
    (.setConfigurationProperty (str FeatureKeys/XML_PARSER_FEATURE "http://apache.org/xml/features/nonvalidating/load-external-dtd") false)
    (.setConfigurationProperty (str FeatureKeys/XML_PARSER_FEATURE "http://xml.org/sax/features/external-general-entities") false)
    (.setConfigurationProperty (str FeatureKeys/XML_PARSER_FEATURE "http://xml.org/sax/features/external-parameter-entities") false)))


(def ^DocumentBuilder builder
  "A Saxon [DocumentBuilder](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/DocumentBuilder.html)."
  (.newDocumentBuilder processor))


(defn xdmvalue->object
  "Get the value of an XdmValue as the nearest equivalent Java object.

  If the XdmValue is a node, return the string value of that node.

  Example:

  ```
  (class (xdmvalue->object (xpath/select \"<num>1</num>\" \"xs:integer(num)\")))
  ;;=> java.math.BigInteger
  ```"
  [value]
  (if (.isAtomicValue value)
    (.getValue value)
    (.getStringValue value)))
