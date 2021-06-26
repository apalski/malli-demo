(defproject malli-demo "0.1"
  :description "Malli Presentation for Clojo"
  :url "https://github.com/apalski/malli-demo"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [metosin/malli "0.5.1"]
                 [borkdude/sci "0.2.5" :exclusions [org.clojure/tools.reader]]
                 [clojure.java-time "0.3.2"]
                 [clojure.java-time "0.3.2"]
                 [com.gfredericks/test.chuck "0.2.10"]]
  
  :target-path "target/%s"
  :profiles {:dev {:resource-paths ["resources"]}})