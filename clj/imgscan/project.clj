(defproject imgscan "0.1.0-SNAPSHOT"

  :description
  "Just an exercise: passes an image to Google Vision and gets labels 
from it"
  
  :url "https://github.com/dkick/imgscan"

  :dependencies [[ch.qos.logback/logback-classic "1.2.11"]
                 [cheshire "5.10.2"]
                 [clojure.java-time "0.3.3"]
                 [com.cognitect/transit-clj "1.0.329"]
                 [com.fasterxml.jackson.core/jackson-core "2.13.2"]
                 [com.fasterxml.jackson.core/jackson-databind "2.13.2.2"]
                 [com.google.cloud/google-cloud-vision "2.0.27"]
                 [com.google.protobuf/protobuf-java "3.20.0"]
                 [com.h2database/h2 "2.1.212"]
                 [conman "0.9.4"]
                 [cprop "0.1.19"]
                 [expound "0.9.0"]
                 [funcool/struct "1.4.0"]
                 [luminus-http-kit "0.1.9"]
                 [luminus-migrations "0.7.2"]
                 [luminus-transit "0.1.5"]
                 [luminus/ring-ttl-session "0.3.3"]
                 [markdown-clj "1.11.0"]
                 [metosin/jsonista "0.3.5"]
                 [metosin/muuntaja "0.6.8"]
                 [metosin/reitit "0.5.18"]
                 [metosin/ring-http-response "0.9.3"]
                 [mount "0.1.16"]
                 [nrepl "0.9.0"]
                 [org.clojars.dkick/encaje "0.1.0"]
                 [org.clojure/clojure "1.11.1"]
                 [org.clojure/core.rrb-vector "0.1.2"]
                 [org.clojure/java.data "1.0.95"]
                 [org.clojure/tools.cli "1.0.206"]
                 [org.clojure/tools.logging "1.2.4"]
                 [org.clojure/tools.reader "1.3.6"]
                 [org.slf4j/slf4j-api "1.7.36"]
                 [org.webjars.npm/bulma "0.9.3"]
                 [org.webjars.npm/material-icons "1.7.1"]
                 [org.webjars/webjars-locator "0.45"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.9.5"]
                 [ring/ring-defaults "0.3.3"]
                 [selmer "1.12.50"]]

  :min-lein-version "2.0.0"
  
  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :main ^:skip-aot imgscan.core

  :plugins [[lein-ancient "1.0.0-RC3"]]

  :profiles
  {:uberjar {:omit-source true
             :aot :all
             :uberjar-name "imgscan.jar"
             :source-paths ["env/prod/clj" ]
             :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:jvm-opts ["-Dconf=dev-config.edn" ]
                  :dependencies [[pjstadig/humane-test-output "0.11.0"]
                                 [prone "2021-04-23"]
                                 [ring/ring-devel "1.9.5"]
                                 [ring/ring-mock "0.4.0"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.24.1"]
                                 [jonase/eastwood "0.3.5"]] 
                  
                  :source-paths ["env/dev/clj" ]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user
                                 :timeout 120000}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:jvm-opts ["-Dconf=test-config.edn" ]
                  :resource-paths ["env/test/resources"] }
   :profiles/dev {}
   :profiles/test {}})
