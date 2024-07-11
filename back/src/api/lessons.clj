(ns api.lessons
  (:require [data.lessons])
  )
(defn lessons []  
  (let [lessons (data.lessons/get-lessons)]
    lessons 
    ))

(defn lesson [id]
  (let [lessons (data.lessons/get-lesson id)]
     lessons))
  

