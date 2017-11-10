(ns sigel.extension-test
  (:require [clojure.test :refer :all]
            [sigel.protocols :refer :all]
            [sigel.xpath.core :as xpath]
            [sigel.extension :as ext]
            [sigel.saxon :as saxon]
            [sigel.xslt.core :as xslt]
            [sigel.xslt.elements :as xsl]
            [sigel.test-utils :refer :all]))


(deftest extension-function-atomic
  (let [compiler (xpath/compiler saxon/processor nil [(xpath/ns "local" "local")])]
    (ext/register-extension-function!
      compiler
      (ext/function ["local" "double"]
                    [[:one :int]]
                    [:one :double]
                    (fn [x] (double (* x 2)))))
    (is (= 2.0 (xpath/value-of compiler "<num>1</num>" "local:double(xs:int(num))" nil)))))


(deftest extension-function-node
  (let [compiler (xpath/compiler saxon/processor nil [(xpath/ns "local" "local")])
        bar (build "<bar/>")]
    (ext/register-extension-function!
      compiler
      (ext/function ["local" "always-return-bar"]
                    [[:one :any-node]]
                    [:one :any-node]
                    (fn [_] bar)))
    (is (= bar (xpath/select compiler "<foo/>" "local:always-return-bar(.)" nil)))))


(deftest extension-function-xslt
  (let [compiler (xslt/compiler)
        stylesheet (xsl/stylesheet
                     {:version 3.0 :xmlns:local "local"}
                     (xsl/template {:match "foo"}
                                   (xsl/copy
                                     (xsl/value-of {:select "local:reverse(.)"}))))]
    (ext/register-extension-function!
      compiler
      (ext/function ["local" "reverse"]
                    [[:one :string]]
                    [:one :string]
                    (fn [string] (clojure.string/join (reverse string)))))

    (is-xml-equal
      (xslt/transform (xslt/compile-sexp compiler stylesheet) "<foo>bar</foo>")
      ["<foo>rab</foo>"])))
