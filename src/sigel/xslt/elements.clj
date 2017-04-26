(ns sigel.xslt.elements
  "Functions for creating XSLT elements.

  This namespace declares so many vars that conflict with vars in the
  Clojure core that you probably want to alias the vars in this
  namespace.

  The docstrings in this file are copied directly from
  [the Saxon documentation](http://www.saxonica.com/html/documentation/xsl-elements)."
  (:require [clojure.data.xml :as xml])
  (:refer-clojure :exclude
                  [assert
                   catch
                   comment
                   if
                   import
                   iterate
                   key
                   map
                   merge
                   namespace
                   sequence
                   sort
                   try
                   when]))

(def namespace-uri "http://www.w3.org/1999/XSL/Transform")

(xml/alias-uri 'xsl namespace-uri)

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
  "The `xsl:attribute` element is used to add an attribute value to an
  [[element]] element or literal result element, or to an element created using
  [[copy]]. The attribute must be output immediately after the element, with no
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
  [& xs]
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
  value is a boolean expression. The contents of the `xsl:if` element are
  expanded only of the expression is true."
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

(defn iterate
  "Used to iterate over a sequence, with the option to set parameters for use in
  the next iteration."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/iterate a xs])

(defn key
  "Used at the top level of the stylesheet to declare an attribute, or other
  value, that may be used as a key to identify nodes using the `key()` function
  within an expression. Each `xsl:key` definition declares a named key, which
  must match the name of the key used in the `key()` function."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/key a xs])

(defn map
  "Used to construct a new map."
  {:since "3.0"}
  [& xs]
  [::xsl/map xs])

(defn map-entry
  "Used to construct a singleton map (one key and one value)."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/map-entry a xs])

(defn matching-substring
  "Used within an [[analyze-string]] element to indicate the default action to
  be taken with substrings that match a regular expression."
  {:since "2.0"}
  [& xs]
  [::xsl/matching-substring xs])

(defn merge
  "The purpose of the instruction is to allow streamed merging of two or more
  pre-sorted input files."
  {:since "3.0"}
  [& xs]
  [::xsl/merge xs])

(defn merge-action
  "The purpose of the instruction is to allow streamed merging of two or more
  pre-sorted input files."
  {:since "3.0"}
  [& xs]
  [::xsl/merge-action xs])

(defn merge-key
  "Used to define the merge keys on which the input sequences of a merging
  operation are sorted."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/merge-key a xs])

(defn merge-source
  "Describes the input source for an [[merge]] instruction."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/merge-source a xs])

(defn message
  "Causes a message to be displayed."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/message a xs])

(defn mode
  "Allows properties of a mode to be defined."
  {:since "3.0"}
  [a]
  [::xsl/mode a])

(defn namespace
  "Creates a namespace node."
  {:since "2.0"}
  [& [a & xs]]
  [::xsl/namespace a xs])

(defn namespace-alias
  "A top-level element used to control the mapping between a namespace URI used
  in the stylesheet and the corresponding namespace URI used in the result
  document."
  {:since "1.0"}
  [a]
  [::xsl/namespace-alias a])

(defn next-iteration
  "The `xsl:next-iteration` instruction occurs within [[iterate]]. The contents
  are a set of [[with-param]] elements defining the values of the iteration
  parameters to be used on the next iteration."
  {:since "3.0"}
  [& xs]
  [::xsl/next-iteration xs])

(defn next-match
  "Chooses the next template to execute."
  {:since "2.0"}
  [& [a & xs]]
  [::xsl/next-match a xs])

(defn non-matching-substring
  "Used within an `xsl:analyze-string` element to indicate the default action to
  be taken with substrings that do not match a regular expression."
  {:since "2.0"}
  [& xs]
  [::xsl/non-matching-substring xs])

(defn number
  "Outputs the sequential number of a node in the source document."
  {:since "1.0"}
  [a]
  [::xsl/number a])

(defn on-completion
  "Occurs within `xsl:iterate` to define processing to be carried out when the
  input sequence is exhausted."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/on-completion a xs])

(defn on-empty
  "Used to allow conditional content construction to be made streamable. Outputs
  the enclosed content only if the containing sequence also generates
  \"ordinary\" content."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/on-empty a xs])

(defn on-non-empty
  "Used to allow conditional content construction to be made streamable. Outputs
  the enclosed content only if the containing sequence generates no \"ordinary\"
  content."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/on-empty a xs])

(defn otherwise
  "Used within [[choose]] to indicate the default action to be taken if none of
  the other choices matches."
  {:since "1.0"}
  [& xs]
  [::xsl/otherwise xs])

(defn output
  "Used to control the format of serial output files resulting from the
  transformation."
  {:since "1.0"}
  [a]
  [::xsl/output a])

(defn output-character
  "Used to define the output representation of a given Unicode character, in a
  [[character-map]]."
  {:since "2.0"}
  [a]
  [::xsl/output-character a])

(defn override
  "Allows a using package to override a component from a used package."
  {:since "3.0"}
  [& xs]
  [::xsl/override xs])

(defn package
  "Defines a set of stylesheet modules that can be separately compiled."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/package a xs])

(defn param
  "Used to define a formal parameter to a template, the stylesheet, a function,
  or an iteration."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/param a xs])

(defn perform-sort
  "Takes a sequence as its input and produces a sorted sequence as its output."
  {:since "2.0"}
  [& [a & xs]]
  [::xsl/perform-sort a xs])

(defn preserve-space
  "Used at the top level of the stylesheet to define elements in the source
  document for which whitespace nodes are significant and should be retained."
  {:since "1.0"}
  [a]
  [::xsl/preserve-space a])

(defn processing-instruction
  "Causes an XML processing instruction to be output."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/processing-instruction a xs])

(defn result-document
  "Used to direct output to a secondary output destination."
  {:since "2.0"}
  [& [a & xs]]
  [::xsl/result-document a xs])

(defn sequence
  "Used to construct arbitrary sequences. It may select any sequence of nodes
  and/or atomic values, and essentially adds these to the result sequence."
  {:since "2.0"}
  [& [a & xs]]
  [::xsl/sequence a xs])

(defn sort
  "Used within [[for-each]], [[apply-templates]], [[for-each-group]], or
  [[perform-sort]] to indicate the order in which the selected elements are
  processed."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/sort a xs])

(defn source-document
  "Used to initiate streamed processing of a source document."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/source-document a xs])

;; xsl:stream omitted -- use xsl:source-document instead.

(defn strip-space
  "Used at the top level of the stylesheet to define elements in the source
  document for which whitespace nodes are insignificant and should be removed
  from the tree before processing."
  {:since "1.0"}
  [a]
  [::xsl/strip-space a])

(defn stylesheet
  "The `xsl:stylesheet` element is always the top-level element of an XSLT
  stylesheet. The name `xsl:transform` may be used as a synonym."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/stylesheet (clojure.core/merge {:xmlns/xsl namespace-uri} a) xs])

(defn template
  "Defines a processing rule for source elements or other nodes of a particular
  type."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/template a xs])

(defn text
  "Causes its content to be output. The main reason for enclosing text within an
  `xsl:text` element is to allow whitespace to be output. Whitespace nodes in
  the stylesheet are ignored unless they appear immediately within an `xsl:text`
  element."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/text a xs])

;; xsl:transform omitted -- use xsl:stylesheet instead.

(defn try
  "In conjunction with [[catch]], the `xsl:try` instruction allows recovery from
  dynamic errors occurring within the expression it wraps."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/try a xs])

(defn use-package
  "Used to allow components of one package to be referenced within another."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/use-package a xs])

(defn value-of
  "Evaluates an expression as a string, and outputs its value to the current
  result tree."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/value-of a xs])

(defn variable
  "Evaluates an expression as a string, and outputs its value to the current
  result tree."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/variable a xs])

(defn when
  "Used within [[choose]] to indicate one of a number of choices."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/when a xs])

(defn where-populated
  "Used to allow conditional content construction to be made streamable. Used to
  avoid outputting an element if it would have no children."
  {:since "3.0"}
  [& [a & xs]]
  [::xsl/where-populated a xs])

(defn with-param
  "Used to define an actual parameter to a template: within [[call-template]],
  [[apply-templates]], [[apply-imports]], [[next-match]]. Also used to define
  parameters to an iteration (using [[next-iteration]]) or to a dynamic
  invocation of an XPath expression (using [[evaluate]])."
  {:since "1.0"}
  [& [a & xs]]
  [::xsl/with-param a xs])
