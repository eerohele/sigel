(ns sigel.xpath-spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as spec-test]
            [sigel.xpath.core :as xpath]
            [sigel.protocols :refer :all])
  (:import (net.sf.saxon.s9api XPathCompiler XdmValue)))

(s/def ::prefix string?)
(s/def ::uri string?)
(s/def ::namespace (s/keys :req-un [::prefix ::uri]))

(s/def ::xpath-expression string?)
(s/def ::xpath-compiler (partial instance? XPathCompiler))
(s/def ::xdmvalue (partial instance? XdmValue))
(s/def ::qnameable (partial satisfies? QNameable))
(s/def ::xml-value (partial satisfies? XMLValue))

(s/def ::bindings
  (s/nilable (s/map-of ::qnameable ::xml-value)))

(s/def ::xpath-fn-args
  (s/cat :compiler (s/? ::xpath-compiler)
         :context ::xml-value
         :expression ::xpath-expression
         :bindings (s/? ::bindings)))

(s/fdef xpath/select
        :args ::xpath-fn-args :ret ::xdmvalue)

(s/fdef xpath/match
        :args ::xpath-fn-args :ret ::xdmvalue)

(s/fdef xpath/is?
        :args ::xpath-fn-args :ret boolean?)

(s/fdef xpath/value-of
        :args ::xpath-fn-args :ret string?)

(spec-test/instrument `xpath/select)
(spec-test/instrument `xpath/match)
(spec-test/instrument `xpath/is?)
(spec-test/instrument `xpath/value-of)
