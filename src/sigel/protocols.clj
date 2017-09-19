(ns sigel.protocols
  "Protocols for converting Clojure values into objects Saxon understands."
  (:require [sigel.saxon :as saxon])
  (:import (javax.xml.transform.stream StreamSource)
           (net.sf.saxon.s9api QName XdmNode XdmValue XdmAtomicValue)
           (java.io StringReader File)
           (java.net URI URL)
           (javax.xml.transform Source)
           (clojure.lang Keyword APersistentVector)))

(defprotocol XMLSource
  "A protocol for things that can be converted into a Saxon
  [XdmNode](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XdmNode.html)."
  (build [source]))

(extend-protocol XMLSource
  XdmNode
  (build [xdmnode] xdmnode)
  String
  (build [xml-string]
    (.build saxon/builder (StreamSource. (StringReader. xml-string))))
  File
  (build [file]
    (.build saxon/builder file))
  Source
  (build [source]
    (.build saxon/builder source))
  URI
  (build [^URI uri]
    (.build saxon/builder (StreamSource. uri)))
  URL
  (build [^URL url]
    (.build saxon/builder (StreamSource. (.toString url)))))

(defprotocol QNameable
  "A protocol for things that can be converted into a
  [QName](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/QName.html)."
  (->qname [name]))

(extend-protocol QNameable
  QName
  (->qname [qname] qname)
  String
  (->qname [qname] (QName. qname))
  Keyword
  (->qname [qname] (-> qname name ->qname))
  APersistentVector
  (->qname [[namespace name]] (QName. namespace name)))

(defprotocol XMLValue
  "A protocol for things that can be converted into a Saxon
  [XdmValue](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XdmValue.html)."
  (->xdmvalue [value]))

(extend-protocol XMLValue
  XdmValue
  (->xdmvalue [xdmvalue] xdmvalue)
  Object
  (->xdmvalue [obj] (XdmAtomicValue. obj)))
