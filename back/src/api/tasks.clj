(ns api.tasks
  (:require [data.tasks]
            [clojure.data.json :as json]
            [api.repl :refer [evaluate]]
            ))


(defn tasks-by-lesson [lesson-id]
  (let [tasks (data.tasks/get-by-lesson lesson-id)]
    (map (fn [task] 
           (prn (:tasks/id task))
           (assoc task :tests (data.tasks/get-tests (:tasks/id task)))) tasks)
    ))



(defn task-by-id [id] (let [task (data.tasks/get-by-id id)]
 (assoc task :tests (data.tasks/get-tests id))))

(defn check-test [test expr] 
  (let [env (json/read-str (:tests/env test) :key-fn symbol)
        evaluation (evaluate {:expr expr :env env})
        ]
    (prn evaluation (read-string (:tests/output test)))
    (if (= (:status evaluation) "error") {:status "error" :message (:message evaluation)} 
        (if (= (:result evaluation) (read-string (:tests/output test))) {:status "pass"}
            {:status "fail"})
        )))


(defn solve-task [task-id expr] (let 
                                [tests (data.tasks/get-tests task-id)] 
                                  (map #(check-test % expr) tests)))

(defn all-tasks [] (data.tasks/get-all))
(defn count-tasks [] (data.tasks/count-tasks))



