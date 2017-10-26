(ns sigel.xslt-test
  (:require [sigel.xslt.elements :as xsl]
            [sigel.xslt.components :refer :all]
            [sigel.xslt.core :as xslt]
            [sigel.test-utils :refer :all]
            [sigel.utils :as u]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))

(deftest xslt-compile-from-edn
  (let [stylesheet (-> "examples/a.edn" io/resource xslt/compile-edn)]
    (is-xml-equal (xslt/transform stylesheet {:p 1} "<a/>") ["<b>1</b>"])))

(deftest xslt-compile-xslt-file
  (is-xml-equal (xslt/transform (xslt/compile-xslt-file "resources/examples/a-to-b.xsl") "<a/>") ["<b/>"]))

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
        (xslt/compile-source
         (u/string->source stylesheet))]
    (is-xml-equal (xslt/transform identity-transform "<a/>")
                  ["<a/>"])))

(deftest xslt-single-transformation
  (let [xslt (xsl/stylesheet {:version 3.0} (xsl/template {:match "a"} [:b]))]
    (is-xml-equal
     (xslt/transform (xslt/compile-sexp xslt) "<a/>")
     ["<b/>"])))

(deftest xslt-passing-nil-executable-returns-argument
  (is-xml-equal (xslt/transform nil "<a/>") ["<a/>"]))

(deftest xslt-params
  (let [xslt (xsl/stylesheet {:version 3.0
                              :xmlns:xs "http://www.w3.org/2001/XMLSchema"}
                             (xsl/param {:name "factor" :as "xs:integer"})

                             (xsl/template
                              {:match "num"}
                              (xsl/copy (xsl/value-of {:select "xs:int(.) * $factor"}))))]
    (is-xml-equal
     (xslt/transform (xslt/compile-sexp xslt) {:factor 10} "<num>1</num>")
     ["<num>10</num>"])))

(deftest xslt-pipeline
  (let [xslt-1      (xsl/stylesheet {:version 3.0}
                                    (xsl/template {:match "a"} [:b])
                                    (xsl/template {:match "w"} [:x]))
        xslt-2      (xsl/stylesheet {:version 3.0}
                                    (xsl/template {:match "b"} [:c])
                                    (xsl/template {:match "x"} [:y]))
        xslt-3      (xsl/stylesheet {:version 3.0}
                                    (xsl/template {:match "c"} [:d])
                                    (xsl/template {:match "y"} [:z]))
        executables (map xslt/compile-sexp [xslt-1 xslt-2 xslt-3])]
    (is-xml-equal
     (map (partial xslt/transform executables) ["<a/>" "<w/>"])
     ["<d/>" "<z/>"])))
