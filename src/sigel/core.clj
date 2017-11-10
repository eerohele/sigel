(ns sigel.core
  "Sigel is a Clojure interface to the [Saxon](http://www.saxonica.com) XSLT and
  XPath implementations."
  (:gen-class)
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.string :as string]
            [sigel.xslt.core :as xslt]))

(def cli-options
  [["-e"
    "--edn EDN"
    "One or mode EDN files that define XSLT transformations."
    :parse-fn #(map io/as-file (string/split % #" "))]
   ["-p"
    "--parameters PARAMS"
    "A map of parameters to pass to every transformation. Example: {:foo 1}"
    :parse-fn edn/read-string]
   ["-h" "--help" "Show this help."]])

(defn help
  [options-summary]
  (string/join
    \newline
    ["Sigel."
     ""
     "Usage: sigel [options] action source(s)"
     ""
     "Options:"
     options-summary]))

(def user-dir (System/getProperty "user.dir"))

(defn- get-output-file
  [source]
  (let [dir (io/file user-dir (io/file "target"))]
    (.mkdir dir)
    (io/file dir (.getName source))))

(defn- validate-args
  [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) {:exit-message (help summary) :ok? true}
      errors {:exit-message errors}
      (seq arguments)
      {:action (first arguments) :sources (rest arguments) :options options}
      :else {:exit-message (help summary)})))

(defn exit
  [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [action sources options exit-message ok?]} (validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (case action
        "xslt"
        (doall
          (let [executables (map xslt/compile-edn (:edn options))]
            (map (fn [source]
                   (let [source-file (io/file source)]
                     (xslt/transform-to-file
                       (map xslt/compile-edn (:edn options))
                       (:parameters options)
                       source-file
                       (get-output-file source-file))))
                 sources)))))))

