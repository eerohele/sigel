(ns sigel.test-utils
  (:require [clojure.test :refer [is]])
  (:import (org.xmlunit.builder Input DiffBuilder)
           (net.sf.saxon.s9api QName XdmAtomicValue)))

(defn- diff-builder
  [actual]
  (.ignoreWhitespace
    (.checkForIdentical (DiffBuilder/compare (Input/fromString (str actual))))))

(defn is-xml-equal
  [actual expected]
  (doseq [[a e] (map vector actual expected)]
    (let [diff (.build (.withTest (diff-builder a) (Input/fromString e)))]
      (is (not (.hasDifferences diff)) (.toString diff)))))
