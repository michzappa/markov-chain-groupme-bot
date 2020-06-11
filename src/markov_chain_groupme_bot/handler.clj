(ns markov-chain-groupme-bot.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clj-http.client :as client]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [cheshire.core :refer :all]
            [markov-chain-groupme-bot.generator :as generator]))

; body going into here is a map (JSON object)
(defn received-message [body]
  (let [text (clojure.string/lower-case (str (get body "text")))
        words (clojure.string/split text #"[\s|\n]")]
    (if (= (count words) 2)
      (if (= (first words) "bot")
        (let [seedWord (second words)]
          (generator/make-text seedWord))
        (str "not valid bot command"))
      (if (and (= (count words) 1) (= (first words) "bot"))
        (generator/make-text)
        (str "not valid bot command")))))

(defroutes app-routes
           (GET "/" [] (str "GET requests do nothing"))
           (POST "/" {body :body} (received-message body))
           (route/not-found "Not Found"))

(def app (-> app-routes (wrap-json-body) (wrap-json-response)))
