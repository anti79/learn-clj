(ns data.lessons
(:require [next.jdbc :as jdbc]
          [data.db :as db]
      	  [honey.sql :as sql])  
  )
(defn get-lessons [] (jdbc/execute! db/db (sql/format {:select [:*] :from [:lessons]})))
(defn get-lesson [id] (first (jdbc/execute! db/db (sql/format {:select [:*] :from [:lessons] :where [:= :id :?id]} {:params {:id id}}))))

