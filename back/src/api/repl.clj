(ns api.repl
  (:require [sci.core :as sci]
            [clojure.data.json :as json]
         			))

(defn evaluate [body]
  (let [expr (:expr body)
        env (if (:env body) (:env body) {})
        ] 
    (try
      (let [result (sci/eval-string expr {:bindings env})]
        {:status "success" :result result})
      (catch Exception e
        {:status "error" :message (.getMessage e)}))))


(= 
(:result (evaluate {:expr "(defn fizzbuzz [n]
  (map (fn [i]
         (cond
           (and (zero? (mod i 3)) (zero? (mod i 5))) \"FizzBuzz\"
           (zero? (mod i 3)) \"Fizz\"
           (zero? (mod i 5)) \"Buzz\"
           :else i))
       (range 1 (inc n))))
(fizzbuzz 15)" :env {'n 15}}))
 
 (read-string "(1 2 \"Fizz\" 4 \"Buzz\" \"Fizz\" 7 8 \"Fizz\" \"Buzz\" 11 \"Fizz\" 13 14 \"FizzBuzz\")")
)