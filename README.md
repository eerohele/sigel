Sigel
=====

[![Clojars Project](https://img.shields.io/clojars/v/me.flowthing/sigel.svg)](https://clojars.org/me.flowthing/sigel)

( [Changelog] | **[API]** )

Sigel «ᛋ» is a Clojure interface to the XSLT and XPath bits of [Saxon].

## XSLT

Sigel lets you write XSLT, but with parentheses instead of angle brackets.

### Examples

```clojure
(require '[sigel.xslt.core :as xslt]
         '[sigel.xslt.elements :as xsl]
         '[sigel.protocols :refer :all])

(def stylesheet-1
  "An XSLT stylesheet that converts <a/> to <b/>."
  (xsl/stylesheet {:version 3.0}
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

You can also write your transformation in EDN:

```clojure
;; a.edn
[:xsl/stylesheet {:version 3.0}
 [:xsl/template {:match "a"} [:b]]]

;; in your Clojure code
(xslt/transform (xslt/compile-edn "/path/to/a.edn") "<a/>")
;;=> #object[net.sf.saxon.s9api.XdmNode 0xf2a49c4 "<b/>"]
```

You can also execute XSLT transformations written in plain old XML:

```xsl
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

### Examples

```clojure
(require '[sigel.xpath.core :as xpath])

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

## XML

Every function in this library that takes XML as input accepts any object that implements [the `XMLSource` protocol](https://github.com/eerohele/sigel/blob/master/src/sigel/protocols.clj).

### Examples

```clojure
;; java.lang.String
(xpath/select "<a><b/><c/></a>" "a/b")
;;=> #object[net.sf.saxon.s9api.XdmNode 0x772300a6 "<b/>"]

;; java.io.File
(xpath/select (clojure.java.io/as-file "/tmp/a.xml") "a/b")
;;=> #object[net.sf.saxon.s9api.XdmNode 0x5487f8c7 "<b/>"]

;; java.net.URL
(xpath/select (clojure.java.io/as-url "http://www.xmlfiles.com/examples/note.xml") "/note/to")
;;=> #object[net.sf.saxon.s9api.XdmNode 0x79f4a8cb "<to>Tove</to>"]
```

## License

Copyright © 2017 Eero Helenius

Distributed under the [Eclipse Public License][EPL] either version 1.0 or (at
your option) any later version.

Saxon is licensed under the [Mozilla Public License][MPL].

[API]: https://eerohele.github.io/sigel
[CHANGELOG]: https://github.com/eerohele/sigel/blob/master/CHANGELOG.md

[EPL]: https://www.eclipse.org/legal/epl-v10.html
[MPL]: https://www.mozilla.org/en-US/MPL
[Saxon]: http://www.saxonica.com
