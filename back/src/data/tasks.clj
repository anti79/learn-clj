(ns data.tasks
  (:require [next.jdbc :as jdbc]
            [data.db :as db]
            [honey.sql :as sql]))

(defn get-by-lesson [lesson-id] (jdbc/execute! db/db (sql/format {:select [:*] :from [:tasks] :where [:= :lesson_id :?lesson_id]} {:params {:lesson_id lesson-id}})))
(defn get-all [] (jdbc/execute! db/db (sql/format {:select [:*] :from [:tasks] })))
(defn get-by-id [task-id] (first (jdbc/execute! db/db (sql/format {:select [:*] :from [:tasks] :where [:= :id :?task_id]} {:params {:task_id task-id}}))))
(defn get-tests [task-id ] (jdbc/execute! db/db (sql/format {:select [:*] :from [:tests] :where [:= :task_id :?task_id]} {:params {:task_id task-id}})))
(defn count-tasks [] (second (first (first (jdbc/execute! db/db ["SELECT COUNT(*) FROM tasks;"])))))
