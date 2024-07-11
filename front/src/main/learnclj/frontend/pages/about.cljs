(ns learnclj.frontend.pages.about)
(defn about-page []
  [:div
   [:img {:style {:float "right"} :src "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5d/Clojure_logo.svg/256px-Clojure_logo.svg.png"}]
   [:div
    [:h1 "About"]
    [:p "This little project was created as a exercise to learn how to make web apps with Clojure."]
    [:h2 "Stack"]
    [:h3 "Frontend"]
    [:ul
     [:li "ClojureScript"]
     [:li "shadow-cljs"]
     [:li "Reagent"]
     [:li "Reitit"]]
    [:h3 "Backend"]
    [:ul
     [:li "Clojure"]
     [:li "Ring"]
     [:li "Compojure"]
     [:li "SQLite + HoneySQL"]]]])
