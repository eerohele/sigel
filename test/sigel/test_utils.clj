(ns sigel.test-utils
  (:import (org.xmlunit.builder Input DiffBuilder)
           (net.sf.saxon.s9api QName XdmAtomicValue)))

(defn- diff-builder
  [actual]
  (.ignoreWhitespace (.checkForIdentical (DiffBuilder/compare (Input/fromString (str actual))))))

(defn xml-equal?
  [actual expected]
  (every? (fn [[a e]]
            (not (.hasDifferences (.build (.withTest (diff-builder a) (Input/fromString e))))))
          (map vector actual expected)))
