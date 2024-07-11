(ns learnclj.frontend.pages.homepage
  (:require [cljs-http.client :as http]
            [clojure.core.async :refer [<! go]]
            [learnclj.frontend.config :refer [api-root]]
            [learnclj.frontend.routes :as-alias routes]
            [learnclj.frontend.utils.json :refer [parse-response]]
            [reagent.core :as reagent]
            [reitit.frontend.easy :as rfe]))

(defonce state (reagent/atom {}))

(defn get-lessons [] (go (let [response (<! (http/get (str api-root "/lessons")
                                                      {:with-credentials? false}))]
                           (let [response-body (parse-response response)]
                             (swap! state assoc :lessons response-body)))))

(get-lessons)
(defn home-page []
  [:div
   [:h1 [:span {:class "clj-green"} "(learn"] [:span {:class "clj-blue"}  "-clj)"]]

   [:ul {:class "lesson-list"} (map
                                (fn [lesson]
                                  [:li {:key (:id lesson)} [:a {:href (rfe/href ::routes/lesson {:id (:id lesson)})} (:title lesson)]])
                                (:lessons @state))]])