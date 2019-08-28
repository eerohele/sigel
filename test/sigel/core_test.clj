(ns sigel.core-test
  (:require [clojure.test :refer :all]
            [sigel.core :refer :all]))

(deftest epic-fail
  (is (= (+ 1 1) 3)))
