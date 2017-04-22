(ns sigel.xslt.elements
  "Functions for creating XSLT elements.

  The docstrings in this file are copied directly from
  [the Saxon documentation](http://www.saxonica.com/html/documentation/xsl-elements)."
  (:require [clojure.data.xml :as xml])
  (:refer-clojure :exclude [assert catch if import]))

(xml/alias-uri 'xsl "http://www.w3.org/1999/XSL/Transform")

(defn accept
  "Allows a package to restrict the visibility of components exposed by a
  package that it uses."
  {:since "3.0"}
  [a] [::xsl/accept a])

(defn accumulator
  "An accumulator defines some processing that is to take place while a document
  is being sequentially processed: for example, a total that is to be
  accumulated."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/accumulator a xs])

(defn accumulator-rule
  "Defines a rule for an [[accumulator]]."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/accumulator-rule a xs])

(defn analyze-string
  "Applies a regular expression to a supplied string value."
  {:since "2.0"}
  [& [a & xs]]
  [::xsl/analyze-string a xs])

(defn apply-imports
  "Searches for a template that matches the current node and that is defined in
  a stylesheet that was imported (directly or indirectly) from the stylesheet
  containing the current template, and whose mode matches the current mode. If
  there is such a template, it is activated using the current node. If not, the
  built-in template for the kind of node is activated."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/apply-imports a xs])

(defn apply-templates
  "Causes navigation from the current element, usually but not necessarily to
  process its children. Each selected node is processed using the best-match
  xsl:template defined for that node."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/apply-templates a xs])

(defn assert
  "Used to make assertions in the form of XPath expressions, causing a dynamic
  error if the assertion turns out to be false."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/assert a xs])

(defn attribute
  "The xsl:attribute element is used to add an attribute value to an [[element]]
  element or literal result element, or to an element created using [[copy]].
  The attribute must be output immediately after the element, with no
  intervening character data."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/attribute a xs])

(defn attribute-set
  "Used to declare a named collection of attributes, which will often be used
  together to define an output style. It is declared at the top level
  (subordinate to [[stylesheet]])."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/attribute-set a xs])

(defn break
  "The `xsl:break` instruction is used within [[iterate]], and causes premature
  completion before the entire input sequence has been processed."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/break a xs])

(defn call-template
  "Invokes a named template."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/call-template a xs])

(defn catch
  "In conjunction with [[try]], the `xsl:catch` instruction allows recovery from
  dynamic errors."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/catch a xs])

(defn character-map
  "Defines a named character map for use during serialization."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/character-map a xs])

(defn choose
  "Used to choose one of a number of alternative outputs."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/choose a xs])

(defn comment
  "Indicates text that is to be output to the current output stream in the form
  of an XML or HTML comment."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/comment a xs])

(defn context-item
  "Used to declare the initial context item for a template: whether the template
  requires a context item, and if so, what its expected type is."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/context-item a xs])

(defn copy
  "Causes the current XML node in the source document to be copied to the
  output. The actual effect depends on whether the node is an element, an
  attribute, or a text node."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/copy a xs])

(defn copy-of
  "Copies the value obtained by evaluating the mandatory select attribute. It
  makes an exact copy."
  {:since "1.0"}
  [a]
  [::xsl/copy-of a])

(defn decimal-format
  "Indicates a set of localisation parameters. If the `xsl:decimal-format`
  element has a name attribute, it identifies a named format; if not, it
  identifies the default format."
  {:since "1.0"}
  [a]
  [::xsl/decimal-format a])

(defn document
  "Creates a new document node."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/document a xs])

(defn element
  "Used to create an output element whose name might be calculated at run-time."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/element a xs])

(defn evaluate
  "Allows dynamic evaluation of XPath expressions constructed as a string."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/evaluate a xs])

(defn expose
  "Used to modify the visibility of selected components within a package."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/expose a xs])

(defn fallback
  "Used to define recovery action to be taken when an instruction element is
  used in the stylesheet and no implementation of that element is available."
  {:since "1.0"}
  [xs]
  [::xsl/fallback xs])

(defn for-each
  "Causes iteration over the nodes selected by a node-set expression."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/for-each a xs])

(defn for-each-group
  "Selects a sequence of nodes and/or atomic values and organizes them into
  subsets called groups."
  {:since "2.0"}
  [& [a & xs]]
  [::xsl/for-each-group a xs])

(defn fork
  "The result of the xsl:fork instruction is the sequence formed by
  concatenating the results of evaluating each of its contained instructions, in
  order."
  {:since "3.0"}
  [& xs]
  [::xsl/fork xs])

(defn function
  "Defines a function within a stylesheet. The function is written in XSLT but
  it may be called from any XPath expression in the stylesheet. It must have a
  non-default namespace prefix."
  {:since "2.0"}
  [& [a & xs]]
  [::xsl/function a xs])

(defn global-context-item
  "Used to declare whether a global context item is required, and if so, to
  declare its required type."
  {:since "3.0"}
  [a]
  [::xsl/global-context-item a])

(defn if
  "Used for conditional processing. It takes a mandatory test attribute, whose
  value is a boolean expression. The contents of the xsl:if element are expanded
  only of the expression is true."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/if a xs])

;; TODO: Implement Saxon STYLE_PARSER_CLASS and XSLT_­STATIC_­URI_­RESOLVER_­CLASS
;; that can import and include stylesheet from Clojure files?
(defn import
  "Used to import the contents of one stylesheet module into another."
  {:since "1.0"}
  [a]
  [::xsl/import a])

(defn import-schema
  "Used to identify a schema containing definitions of types that are referred
  to in the stylesheet"
  {:since "2.0"}
  [& [a & xs]]
  [::xsl/import-schema a xs])

(defn include
  "Used to include the contents of one stylesheet within another."
  {:since "1.0"}
  [a]
  [::xsl/include a])

;;;

(defn stylesheet
  "The `xsl:stylesheet` element is always the top-level element of an XSLT
  stylesheet. The name `xsl:transform may be used as a synonym."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/stylesheet a xs])

(defn param
  [& [a & xs]]
  [::xsl/param a xs])

(defn template
  [& [a & xs]]
  [::xsl/template a xs])

(defn value-of
  [& [a & xs]]
  [::xsl/value-of a xs])
