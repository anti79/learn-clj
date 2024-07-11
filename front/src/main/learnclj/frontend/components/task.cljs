(ns learnclj.frontend.components.task
  (:require ["highlighted-code" :as highlighted-code]
            [cljs-http.client :as http]
            [clojure.core.async :refer [<! go]]
            [learnclj.frontend.config :refer [api-root]]
            [learnclj.frontend.utils.json :refer [parse-response]]
            [learnclj.frontend.utils.localstorage :as ls]
            [reagent.core :as reagent]))

(defonce state (reagent/atom {:tests nil :task-id nil}))

(defn solved? [tests]
  (and (> (count tests) 0) (every? #(= (get % :status) "pass") tests)))

(defn solve! [task-id expr] (go (let [response (<! (http/post (str api-root "/solve")
                                                              {:with-credentials? false
                                                               :json-params {:expr expr :task-id task-id}}))]
                                  (let [body (parse-response response)]
                                    (doall (swap! state update :tests #(map merge (:tests @state) body)))

                                    (if (not (ls/get-set "tasks-solved"))
                                      (ls/set-item! "tasks-solved" (set [])))
                                    (if (solved? (:tests @state))
                                      (ls/add-to-set! "tasks-solved" task-id)
                                      (ls/remove-from-set! "tasks-solved" task-id))))))

(defn get-solution! [task]
  (set! (.-value (js/document.getElementById "code-input")) (:solution task)))

(defn make-editor []
  (let [textarea (.getElementById js/document "code-input")]
    (js/CodeMirror.fromTextArea textarea
                                (clj->js {:mode "clojure"
                                          :theme "default"
                                          :lineNumbers true
                                          :indentUnit 2
                                          :autoCloseBrackets true
                                          :matchBrackets true}))))

(defn task-component [task]
  (if (not (= (:id task) (:task-id @state)))
    (do
      (swap! state assoc :tests (:tests task))
      (swap! state assoc :task-id (:id task))))
  (prn task)
  [:div {:key (:id task)}
   [:div {:class "desc"}
    [:span {:class "bold"} "Description: "] [:span {:dangerouslySetInnerHTML {:__html (:description task)}}]]
   [:span {:class "bold"} "Solution: "] [:button {:class "solution-btn" :on-click #(get-solution! task)} "click to reveal"]

   [:div {:class "textarea-container"}
    [:textarea {:id "code-input" :language "clojure" :is "highlighted-code"}]
    [:button {:class "eval-btn" :on-click #((solve! (:id task) (.-value (js/document.getElementById "code-input"))))}]]
   [:div
    [:ul {:class "test-list"}
     (map
      (fn [test]
        [:li  ;test item
         {:key (:id test),
          :class (cond
                   (= (:status test) "error") "yellow"
                   (= (:status test) "pass") "green"
                   (= (:status test) "fail") "red")}
         (str "Test " (inc (.indexOf (:tests @state) test))
              (cond
                (= (:status test) "error") (str " - Error: \"" (:message test) "\"")
                (= (:status test) "pass") (str " - Passed: " (js->clj (:env test) :keywordize-keys true) ". Output: " (:output test))
                (= (:status test) "fail") " - Failed"))])
      (:tests @state))
     (if
      (or
       (solved? (:tests @state))
       (contains? (ls/get-set "tasks-solved") (:id task)))

       [:div {:class "solved-message"} [:b "Task solved! ✔"]])]]])

(defn load-highlighted-code []
  (let [{:keys [default]} highlighted-code]
    (.useTheme highlighted-code "default")))

(load-highlighted-code)


