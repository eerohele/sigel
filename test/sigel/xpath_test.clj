(ns sigel.xpath-test
  (:require [clojure.test :refer :all]
            [sigel.test-utils :refer :all]
            [sigel.protocols :refer :all]
            [sigel.xpath.core :as xpath]
            [sigel.core :as saxon]))

(deftest set-default-xpath-namespace
  (let [compiler
        (xpath/set-default-namespace (xpath/compiler) "foo")]
    (is (= "foo" (xpath/default-namespace compiler)))))

(deftest match-xpath-pattern
  (let [context "<foo a=\"b\"><bar><foo c=\"d\"/></bar></foo>"]
    (is-xml-equal (xpath/match context "foo")
                  ["<foo a=\"b\"><bar><foo c=\"d\"/></bar></foo>" "<foo c=\"d\"/>"])))

(deftest select-xpath-expression
  (let [compiler (xpath/compiler saxon/processor nil [(xpath/ns "q" "quux")])
        context  "<foo xmlns=\"quux\"><bar a=\"b\"/><bar c=\"d\"/></foo>"]
    (is-xml-equal (seq (xpath/select compiler context "q:foo/q:bar" nil))
                  ["<bar xmlns=\"quux\" a=\"b\"/>" "<bar xmlns=\"quux\" c=\"d\"/>"])))

(deftest value-of-xpath-expression
  (is (= (xpath/value-of "<num>1</num>" "xs:int(num)") 1)))

(deftest matches-xpath-pattern
  (let [node (xpath/select "<num>1</num>" "num[1]")]
    (is (= (xpath/matches? node (xpath/pattern "num[xs:int(.) eq 1]")) true))))

(deftest set-xpath-variable-for-expression
  (is (xpath/is? (xpath/compiler)
                 "<num>1</num>" "xs:integer(num) * $two eq 2"
                 {:two 2})))

(deftest set-xpath-variable-for-pattern
  (is-xml-equal
    (xpath/match (xpath/compiler)
                 "<num>1</num>" "num[xs:integer(.) eq $one]"
                 {:one 1})
    ["<num>1</num>"]))
