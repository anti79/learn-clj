(ns learnclj.frontend.components.meter
  (:require [cljs-http.client :as http]
            [clojure.core.async :refer [<! go]]
            [learnclj.frontend.config :refer [api-root]]
            [learnclj.frontend.utils.localstorage :as ls]
            [reagent.core :as reagent]))

(defonce state (reagent/atom {:total nil}))

(defn get-total-count! [] (go (let [response (<! (http/get (str api-root "/tasks/total") {:with-credentials? false}))]
                                (swap! state assoc :total (js/parseInt (:body response))))))
(get-total-count!)

;(count (ls/get-set "tasks-solved"))

(defn meter []
  [:div
   [:span {:class "bold"} "Solved: "]
   [:meter {:min 0 :max (:total @state) :value (count (ls/get-set "tasks-solved"))}]
   [:span (str (count (ls/get-set "tasks-solved")) "/" (:total @state))]])

@state