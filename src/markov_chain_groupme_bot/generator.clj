(ns markov-chain-groupme-bot.generator
  (:require [clojure.set]
            [clj-http.client :as client]))

; string of all lowercase characters in the specified file
(def processed-file
  (clojure.string/lower-case (slurp (clojure.java.io/resource "<FILENAME IN RESOURCES FOLDER GOES HERE>.txt"))))

; a list of all the words in the above string from the file
(def list-of-words
  (filter #(not (clojure.string/blank? %)) (clojure.string/split processed-file #"[\s|\n]")))

; turns the list of words into a markov chain, represented by a hashmap.
; first generates a list of all the word transitions (what is followed by what) for each word
; and then passes that to the function to generate the hashmap by combining these pairs
(def word-chain
  (let [words list-of-words
        word-transitions (partition-all 2 1 words)]
    (reduce (fn [r t] (merge-with clojure.set/union r
                                  (let [[a c] t]
                                    {a (if c #{c} #{})})))
            {}
            word-transitions)))

; walks through the markov chain adding words until 140 characters is reached
(defn walk-chain [prefix chain result]
  (let [suffixes (get chain prefix)]
    (if (empty? suffixes)
      result
      (let [suffix (first (shuffle suffixes))
            new-prefix suffix
            result-char-count (count result)
            suffix-char-count (inc (count suffix))
            new-result-char-count (+ result-char-count suffix-char-count 1)]
        (if (>= new-result-char-count 140)
          result
          (recur new-prefix chain (str result " " suffix)))))))

; sends groupme API request with the generated text
(defn send-request [text]
  (client/post "https://api.groupme.com/v3/bots/post"
               {:basic-auth         ["user" "pass"]
                :body               (str "{\"bot_id\": \"<GROUPME BOT ID GOES HERE>\", \"text\": \"" text "\"}")
                :headers            {"X-Api-Version" "2"}
                :content-type       :json
                :socket-timeout     1000                    ;; in milliseconds
                :connection-timeout 1000                    ;; in milliseconds
                :accept             :json}))

; generates the text by finding the markov chain and walking through it until 140 characters are reached
(defn generate-text
  [start-phrase]
  (let [word-chain word-chain
        prefix start-phrase
        result-text (walk-chain prefix word-chain prefix)]
    (if (clojure.string/includes? result-text " ")
      (send-request result-text)
      (str "This word was not in the training text"))))

; takes one word to generate text starting from that work according to the markov model,
; or takes no words to randomly choose the starting word
(defn make-text
  ([] (make-text (-> list-of-words shuffle first)))
  ([seedWord] (generate-text seedWord)))

