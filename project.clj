(defproject sigel "0.1.0-SNAPSHOT"
  :description
    "Sigel «ᛋ» is a Clojure interface to the XSLT and XPath bits of Saxon."
  :url "http://github.com/eerohele/sigel"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-beta1"]
                 [org.clojure/data.xml "0.2.0-alpha2"]
                 [net.sf.saxon/Saxon-HE "9.8.0-4"]
                 [org.clojure/tools.cli "0.3.5"]]
  :main ^:skip-aot sigel.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[org.clojure/test.check "0.9.0"]
                                  [org.xmlunit/xmlunit-core "2.5.0"]
                                  [criterium "0.4.4"]]}}
  :plugins [[lein-cljfmt "0.5.6"]
            [lein-codox "0.10.3"]
            [lein-kibit "0.1.3"]
            [lein-marginalia "0.9.0"]
            [com.jakemccrary/lein-test-refresh "0.19.0"]]
  :codox {:metadata {:doc/format :markdown}
          :output-path "target/doc"})
