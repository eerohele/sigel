# XML Sources

Both the XSLT and XPath components of Sigel understand anything that implements
the `XMLSource` protocol.

Examples:

```clojure
(require '[sigel.xpath.core :as xpath])

;; java.lang.String is an XMLSource
(xpath/value-of "<num>1</num>" "xs:int(num)")
;;=> 1

;; java.io.File is an XMLSource
;;
;; Given a file called "num.xml" whose content is "<num>1</num>":
(require '[clojure.java.io :as io])

(xpath/value-of (io/file "num.xml") "xs:int(num)")
;;=> 1
```

`XMLSource` is a protocol that converts its input into a Saxon [XdmNode]. If
Sigel can't understand your XML source, extend the `XMLSource` protocol
yourself.

[XdmNode]: http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XdmNode.html
