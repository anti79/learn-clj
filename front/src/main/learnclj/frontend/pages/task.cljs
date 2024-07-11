
(ns learnclj.frontend.pages.task
  (:require [cljs-http.client :as http]
            [clojure.core.async :refer [<! go]]
            [learnclj.frontend.components.task :refer [task-component]]
            [learnclj.frontend.config :refer [api-root]]
            [learnclj.frontend.routes :as-alias routes]
            [learnclj.frontend.utils.json :refer [parse-response]]
            [reagent.core :as reagent]))

(defonce state (reagent/atom {:task nil}))
(defn get-task [id] (go (let [response (<! (http/get (str api-root "/tasks/" id)
                                                     {:with-credentials? false}))]
                          (let [response-body (parse-response response)]
                            (swap! state assoc :task response-body)))))

(defn task-page [match]

  (let [{:keys [path query]} (:parameters match)
        {:keys [id]} path]
    (if (not (= id (:id (:task @state)))) (get-task id))

    [:div
     [:h1 (:name (:task @state))]
     (task-component (:task @state))]))

