(ns sigel.utils
  "A set of utility functions mostly for internal use."
  (:require [clojure.string :as string])
  (:import (javax.xml.transform.stream StreamSource)
           (java.io StringReader)))


(defn string->source
  "Build a StreamSource from a String."
  [string]
  (StreamSource. (StringReader. string)))


(defn replace-str
  "A version of clojure.string/replace that takes the input string as the last
  argument so that this function can be composed."
  [match replacement s]
  (string/replace s match replacement))


(def constantize-keyword
  "Takes a :keyword-like-this and turns it into a Java-style
  CONSTANT_LIKE_THIS."
  (comp string/upper-case (partial replace-str #"-" "_") name))


(defn get-static-fields-as-keywords
  "Get a list of all static fields in a class as keywords.

  Example:

  ```
  (get-static-fields-as-keywords Math)
  ;;=> (:e :pi)
  ```"
  [cls]
  (map #((comp keyword (partial replace-str #"_" "-") string/lower-case :name)
          (bean %))
       (seq (:fields (bean cls)))))


(defn get-class-constant
  "Get the value of a public static field of a Java class. Takes a keyword
  and assumes that the field is a constant and therefore uses all caps.

  Example:

  ```
  (get-class-constant Math :pi)
  ;;=> 3.141592653589793
  ```"
  [cls field]
  (eval (read-string
          (str (.getName cls) "/" (constantize-keyword field)))))
