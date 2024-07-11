(ns learnclj.frontend.app
  (:require
   [learnclj.frontend.routes :as routing]
   [reagent.core :as r]
   [reagent.dom :as rd]
   [reitit.coercion.spec :as rss]
   [reitit.frontend :as rf]
   [reitit.frontend.easy :as rfe]))

(defonce match (r/atom nil))

(defn current-page []
  [:div
   [:ul {:class "top-menu"}
    [:li [:a {:href (rfe/href ::routing/frontpage)} "home"]]
    [:li [:a {:href (rfe/href ::routing/tasks)} "tasks"]]
    [:li [:a {:href (rfe/href ::routing/repl)} "repl"]]
    [:li [:a {:href (rfe/href ::routing/about)} "about"]]]

   (if @match
     (let [view (:view (:data @match))]
       [view @match]))
   [:div {:class "footer"}]])

(defn init! []
  (rfe/start!
   (rf/router routing/routes {:data {:coercion rss/coercion}})
   (fn [m] (reset! match m))
    ;; set to false to enable HistoryAPI
   {:use-fragment true})
  (rd/render [current-page] (.getElementById js/document "root")))

(init!)