(ns learnclj.frontend.routes
  (:require [learnclj.frontend.pages.about :as about]
            [learnclj.frontend.pages.homepage :as homepage]
            [learnclj.frontend.pages.lesson :as lesson]
            [learnclj.frontend.pages.repl :as repl]
            [learnclj.frontend.pages.task :refer [task-page]]
            [learnclj.frontend.pages.tasks :refer [tasks-page]]
            [reitit.coercion.spec :as rss]
            [reitit.frontend :as rf]))

(def highlight-interceptor
  {:name :fetch-lessons
   :enter (fn [context]
            (.highlightAll js/hljs)
            context)})

(def routes
  [["/"
    {:name ::frontpage
     :view homepage/home-page}]

   ["/about"
    {:name ::about
     :view about/about-page}]

   ["/repl"
    {:name ::repl
     :view repl/repl-page}]

   ["/tasks"
    {:name ::tasks
     :view tasks-page}]

   ["/task/:id"
    {:name ::task
     :view task-page
     :parameters {:path {:id int?}}}]

   ["/lesson/:id"
    {:name ::lesson
     :view lesson/lesson-wrapper
     :parameters {:path {:id int?}}}]])

(def router (rf/router routes {:data {:coercion rss/coercion}}))