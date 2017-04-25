Sigel
=====

Sigel «ᛋ» is a Clojure interface to the XSLT and XPath bits of [Saxon].

## XSLT

Sigel lets you write XSLT, but with parentheses instead of angle brackets.

### Example

```clojure
(require '[sigel.xslt.core :as xslt]
         '[sigel.xslt.elements :as xsl]
         '[sigel.protocols :refer :all])

(def stylesheet-1
  "An XSLT stylesheet that converts <a/> to <b/>."
  (xsl/stylesheet {:version 3.0}
    ;; [:b] is equivalent to <b/>. See clojure.data.xml/sexp-as-element.
    (xsl/template {:match "a"} [:b])))

(def stylesheet-2
  "An XSLT stylesheet that converts <b/> to <c/>."
  (xsl/stylesheet {:version 3.0}
    (xsl/template {:match "b"} [:c])))

(def compiled-stylesheets
  [(xslt/compile-sexp stylesheet-1) (xslt/compile-sexp stylesheet-2)])

;; Transform the XML string "<a/>" with stylesheet-1 and then stylesheet-2.
(xslt/transform compiled-stylesheets "<a/>")
;;=> #object[net.sf.saxon.s9api.XdmNode 0x61acfa00 "<c/>"]
```

You can also execute XSLT transformations written in plain old XML:

```xslt
<!-- a-to-b.xsl -->
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="a">
    <b/>
  </xsl:template>
</xsl:stylesheet>
```

```clojure
(def stylesheet
  (StreamSource. (-> "a-to-b.xsl" io/file io/input-stream)))

(xslt/transform (xslt/compile stylesheet) "<a/>")
;;=> #object[net.sf.saxon.s9api.XdmNode 0x2bda7fdc "<b/>"]
```

## XPath

Select things in an XML file with XPath.

### Example

```clojure
(require '[sigel.xpath :as xpath])

;; Select nodes with XPath.
(seq (xpath/select "<a><b/><c/></a>" "a/b | a/c"))
;;=>
;;(#object[net.sf.saxon.s9api.XdmNode 0x3cadbb6f "<b/>"]
;; #object[net.sf.saxon.s9api.XdmNode 0x136b811a "<c/>"])

;; Get the result of evaluating an XPath expression against a node as a Java
;; object.
(xpath/value-of "<num>1</num>" "xs:int(num)")
;;=> 1
```

## XML sources

Both the XSLT and XPath components of Sigel understand anything that implements the `XMLSource` protocol.

The `XMLSource` protocol converts its input into a Saxon [XdmNode]. If Sigel can't understand your XML source, extend the `XMLSource`
protocol yourself.

## License

Copyright © 2017 Eero Helenius

Distributed under the [Eclipse Public License][EPL] either version 1.0 or (at
your option) any later version.

Saxon is licensed under the [Mozilla Public License][MPL].

[EPL]: https://www.eclipse.org/legal/epl-v10.html
[MPL]: https://www.mozilla.org/en-US/MPL
[Saxon]: http://www.saxonica.com
[XdmNode]: http://www.saxonica.com/html/documentation/javadoc/net/sf/saxon/s9api/XdmNode.html
