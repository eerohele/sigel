(ns sigel.protocols-test
  (:require [clojure.test :refer [deftest is]]
            [sigel.protocols]
            [sigel.xpath.core :as xpath])
  (:import (java.io ByteArrayInputStream)))


(deftest input-stream->XMLSource
  (with-open [stream (ByteArrayInputStream. (.getBytes "<a>1</a>"))]
    (is (= 1 (xpath/value-of stream "xs:integer(/a)")))))
