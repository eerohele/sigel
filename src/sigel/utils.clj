(ns sigel.utils
  (:import (javax.xml.transform.stream StreamSource)
           (java.io StringBufferInputStream)))

(defn string->source
  "Build a StreamSource from a String."
  [string]
  (StreamSource. (StringBufferInputStream. string)))
