(ns learnclj.frontend.pages.lesson
  (:require [cljs-http.client :as http]
            [clojure.core.async :refer [<! go]]
            [learnclj.frontend.components.task :refer [task-component]]
            [learnclj.frontend.config :refer [api-root]]
            [learnclj.frontend.routes :as-alias routes]
            [learnclj.frontend.utils.json :refer [parse-response]]
            [learnclj.frontend.utils.localstorage :as ls]
            [reagent.core :as reagent]
            [reitit.frontend.easy :as rfe]))

(defonce state (reagent/atom {:lesson nil
                              :tasks nil}))

@state

(defn mark-complete [lesson]
  (ls/set-item! (str "lesson-" (:id lesson)) "complete"))

(defn is-complete? [lesson]
  (= (ls/get-item (str "lesson-" (:id lesson))) "complete"))

(defn get-lesson [id] (go (let [response (<! (http/get (str api-root "/lesson/" id)
                                                       {:with-credentials? false}))]
                            (let [response (parse-response response)
                                  lesson (if (is-complete? (:lesson @state)) (assoc response :complete true)
                                             (assoc response :complete false))]

                              (swap! state assoc :lesson lesson)))))

(defn get-tasks [id] (go (let [response (<! (http/get (str api-root "/tasks/by-lesson/" id)
                                                      {:with-credentials? false}))]
                           (let [response-body (parse-response response)]
                             (swap! state assoc :tasks response-body)))))

(defn nav-prev []
  (rfe/replace-state ::routes/lesson {:id (dec (:id (:lesson @state)))}))

(defn nav-next []
  (rfe/replace-state ::routes/lesson {:id (inc (:id (:lesson @state)))}))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn lesson-page [match]

  (let [{:keys [path query]} (:parameters match)
        {:keys [id]} path]

    (if (not (= id (:id (:lesson @state))))
      (do
        (get-lesson id)
        (get-tasks id)))

    [:div
     (if @state
       [:div
        [:h1 (str "Lesson " (:id (:lesson @state)) ": " (:title (:lesson @state)))]
        [:div {:dangerouslySetInnerHTML {:__html (:text (:lesson @state))}}]

        (if (seq (:tasks @state))
          [:div
           [:h1 "Tasks"]
           [:h3 (:name (first (:tasks @state)))]
           (task-component (first (:tasks @state)))]           
          [:div])

        (if (> (:id (:lesson @state)) 1) [:button {:class "light-btn lesson-nav-btn-prev" :on-click #(nav-prev)} "<- previous lesson"])
        [:div {:class "right-btns"}
         (if (< (:id (:lesson @state)) 10) [:button {:class "light-btn lesson-nav-btn-next" :on-click #(nav-next)} "next lesson ->"])]]
       [:div "Loading lesson..."])]))

(defn lesson-wrapper [match]
  (reagent/create-class
   {:component-did-mount (fn []
                           (.highlightAll js/hljs))
    :component-did-update (fn []
                            (.highlightAll js/hljs))

    :reagent-render (fn [match] (lesson-page match))}))