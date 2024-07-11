(ns learnclj.frontend.pages.tasks
  (:require [cljs-http.client :as http]
            [clojure.core.async :refer [<! go]]
            [learnclj.frontend.components.meter :refer [meter]]
            [learnclj.frontend.config :refer [api-root]]
            [learnclj.frontend.routes :as-alias routes]
            [learnclj.frontend.utils.json :refer [parse-response]]
            [learnclj.frontend.utils.localstorage :as ls]
            [reagent.core :as reagent]
            [reitit.frontend.easy :as rfe]))

(defonce state (reagent/atom {}))
(defn get-tasks [] (go (let [response (<! (http/get (str api-root "/tasks")
                                                    {:with-credentials? false}))]
                         (let [response-body (parse-response response)]
                           (swap! state assoc :tasks response-body)))))
(get-tasks)
(defn tasks-page []

  [:div
   [:h1 "All tasks"]
   (meter)
   [:ul {:class "lesson-list"}
    (map
     (fn [task]
       [:li {:key (:id task)}
        [:a {:href (rfe/href ::routes/task {:id (:id task)})} (:name task)]

        (if (contains? (ls/get-set "tasks-solved") (:id task))
          " ✔" "")])

     (:tasks @state))]])