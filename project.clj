(defproject markov-chain-groupme-bot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [hiccup "1.0.5"]
                 [org.clojure/java.jdbc "0.6.0"]
                 [com.h2database/h2 "1.4.193"]
                 [clj-http "3.10.1"]
                 [ring/ring-json "0.5.0"]
                 [cheshire "5.10.0"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler markov-chain-groupme-bot.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}}
  :repl-options {:init-ns markov-chain-groupme-bot.generator})
