(ns sigel.xslt-test
  (:require [sigel.xslt.elements :as xsl]
            [sigel.xslt.components :refer :all]
            [sigel.xslt.core :as xslt]
            [sigel.test-utils :refer :all]
            [sigel.utils :as u]
            [clojure.test :refer :all]))

(deftest xslt-compile-from-source
  (let [stylesheet
        (str "<xsl:stylesheet version=\"3.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">"
             "  <xsl:template match=\"@* | node()\">"
             "    <xsl:copy>"
             "      <xsl:apply-templates select=\"@* | node()\"/>"
             "    </xsl:copy>"
             "  </xsl:template>"
             "</xsl:stylesheet>")
        identity-transform
        (xslt/compile
          (u/string->source stylesheet))]
    (is (xml-equal? (xslt/transform identity-transform "<a/>")
                    ["<a/>"]))))

(deftest xslt-single-transformation
  (let [xslt (xslt3-identity (xsl/template {:match "a"} [:b]))]
    (is (xml-equal?
          (xslt/transform (xslt/compile-sexp xslt) "<a/>")
          ["<b/>"]))))

(deftest xslt-passing-nil-executable-returns-argument
  (is (xml-equal? (xslt/transform nil "<a/>") ["<a/>"])))

(deftest xslt-params
  (let [xslt (xslt3-identity
               (xsl/param {:name "factor" :as "xs:integer"})

               (xsl/template
                 {:match "num"}
                 (xsl/copy (xsl/value-of {:select "xs:int(.) * $factor"}))))]
    (is (xml-equal?
          (xslt/transform (xslt/compile-sexp xslt) {:factor 10} "<num>1</num>")
          ["<num>10</num>"]))))

(deftest xslt-pipeline
  (let [xslt-1 (xslt3-identity
                 (xsl/template {:match "a"} [:b])
                 (xsl/template {:match "w"} [:x]))
        xslt-2 (xslt3-identity
                 (xsl/template {:match "b"} [:c])
                 (xsl/template {:match "x"} [:y]))
        xslt-3 (xslt3-identity
                 (xsl/template {:match "c"} [:d])
                 (xsl/template {:match "y"} [:z]))
        executables (map xslt/compile-sexp [xslt-1 xslt-2 xslt-3])]
    (is (xml-equal?
          (map (partial xslt/transform executables) ["<a/>" "<w/>"])
          ["<d/>" "<z/>"]))))
