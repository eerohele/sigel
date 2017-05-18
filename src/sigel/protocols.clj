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

(extend-protocol XMLSource XdmNode (build [xdmnode] xdmnode))

(extend-protocol XMLSource String
                 (build [xml-string]
                   (.build saxon/builder (StreamSource. (StringReader. xml-string)))))

(extend-protocol XMLSource File
                 (build [file]
                   (.build saxon/builder file)))

(extend-protocol XMLSource Source
                 (build [source]
                   (.build saxon/builder source)))

(extend-protocol XMLSource URI
                 (build [^URI uri]
                   (.build saxon/builder (StreamSource. uri))))

(extend-protocol XMLSource URL
                 (build [^URL url]
                   (.build saxon/builder (StreamSource. (.toString url)))))

(defprotocol QNameable
  "A protocol for things that can be converted into a
  [QName](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/QName.html)."
  (->qname [name]))

(extend-protocol QNameable QName
                 (->qname [qname] qname))

(extend-protocol QNameable String
                 (->qname [qname] (QName. qname)))

(extend-protocol QNameable Keyword
                 (->qname [qname] (-> qname name ->qname)))

(extend-protocol QNameable APersistentVector
                 (->qname [[namespace name]] (QName. namespace name)))

(defprotocol XMLValue
  "A protocol for things that can be converted into a Saxon
  [XdmValue](http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XdmValue.html)."
  (->xdmvalue [value]))

(extend-protocol XMLValue XdmValue
                 (->xdmvalue [xdmvalue] xdmvalue))

(extend-protocol XMLValue Object
                 (->xdmvalue [obj] (XdmAtomicValue. obj)))
