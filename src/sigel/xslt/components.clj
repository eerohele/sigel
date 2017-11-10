(ns sigel.xslt.components
  "A set of reusable XSLT components."
  (:require [sigel.xslt.elements :as xsl])
  (:refer-clojure :exclude [identity]))


(def identity
  "An XSLT identity template."
  (xsl/template
    {:match "@* | node()"}
    (xsl/copy
      (xsl/apply-templates {:select "@* | node()"}))))


(defn xslt3-identity
  "An XSLT 3.0 stylesheet with an identity template and the XML Schema namespace
  (http://www.w3.org/2001/XMLSchema) pre-bound to the `xs` namespace prefix."
  [& [a & xs]]
  (xsl/stylesheet (merge {:xmlns/xsl               "http://www.w3.org/1999/XSL/Transform"
                          :version                 3.0
                          :xmlns:xs                "http://www.w3.org/2001/XMLSchema"
                          :exclude-result-prefixes "xs"} a)
    identity
    xs))


(def identity-transformation (xslt3-identity nil))
