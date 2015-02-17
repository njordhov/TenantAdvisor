(defproject tenantadvisor "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.8"]
                 [ring/ring-jetty-adapter "1.2.2"]
                 [ring/ring-json "0.3.1"]
                 [environ "0.5.0"]
		 [clj-http "1.0.0"]	  
                 [cheshire "5.4.0"] ; redundant?
                 [enlive "1.1.1"]
                 [hiccup "1.0.3"]
                 [clojure-csv/clojure-csv "2.0.1"]]
  :min-lein-version "2.0.0"
  :plugins [[lein-ring "0.8.3"]
            [environ/environ.lein "0.2.1"]
            [lein-swank "1.4.5"]]
  :hooks [environ.leiningen.hooks]
  :ring {:handler tenantadvisor.handler/app}
)