(ns learnclj.frontend.utils.json)
(defn parse-response [response]
  (js->clj (js/JSON.parse (:body response)) {:keywordize-keys true}))

;(parse-response {:status 200, :success true, :body "[{\"id\":1,\"title\":\"Introduction to Clojure\",\"text\":\"Clojure is a dynamic development environment where you interact with your program while you write it, growing and adding to it while it\\u2019s running. To work with Clojure you need an editor that supports evaluation in source files and structural editing (working with nested forms in addition to character editing).\\n\"},{\"id\":2,\"title\":\"Basic Syntax and Data Types\",\"text\":null},{\"id\":3,\"title\":\"Collections in Clojure\",\"text\":null},{\"id\":4,\"title\":\"Functions\",\"text\":null},{\"id\":5,\"title\":\"Conditionals and Looping\",\"text\":null},{\"id\":6,\"title\":\"Namespaces and Libraries\",\"text\":null},{\"id\":7,\"title\":\"Immutability and Persistent Data Structures\",\"text\":null},{\"id\":8,\"title\":\"Macros and Metaprogramming\",\"text\":null},{\"id\":9,\"title\":\"Concurrency\",\"text\":null},{\"id\":10,\"title\":\"Interacting with Java\",\"text\":null}]", :headers {"content-type" "text/html;charset=utf-8"}, :trace-redirects ["http://localhost:3000/api/lessons" "http://localhost:3000/api/lessons"], :error-code :no-error, :error-text ""})