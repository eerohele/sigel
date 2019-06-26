(ns sigel.test-utils
  (:require [clojure.test :refer [is]])
  (:import (org.xmlunit.builder Input DiffBuilder)))


(defn- diff-builder
  [actual]
  (.ignoreWhitespace
    (.checkForIdentical (DiffBuilder/compare (Input/fromString (str actual))))))


(defn xml-equal?
  [actual expected]
  (every? true?
          (for [[a e] (map vector actual expected)]
            (let [diff (.build (.withTest (diff-builder a) (Input/fromString e)))]
              (not (.hasDifferences diff))))))
