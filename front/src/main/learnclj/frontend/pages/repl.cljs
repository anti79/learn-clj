(ns learnclj.frontend.pages.repl
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require ["highlighted-code" :as highlighted-code]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [learnclj.frontend.config :refer [api-root]]
            [learnclj.frontend.utils.json :refer [parse-response]]
            [reagent.core :as reagent]))

(defonce state (reagent/atom {:output ""}))

(defn load-editor []

  (js/CodeMirror.fromTextArea (js/document.getElementById "code-input")
                              {:mode "clojure"
                               :theme "dracula"
                               :lineNumbers "true"
                               :indentUnit 2
                               :autoCloseBrackets true
                               :matchBrackets true}))

(defn evaluate [expr]
  (go (let [response (<! (http/post (str api-root "/evaluate")
                                    {:with-credentials? false
                                     :json-params {:expr expr}}))]
        (let [response-body (parse-response response)]
          (if (= (:status response-body) "success")
            (swap! state update :output #(str % "repl=> " (:result response-body) "\n"))
            (swap! state update :output #(str % (:message response-body) "\n")))))))

(defn repl-page []
  [:div [:h1 "REPL"]
   [:div {:class "panes"}
    [:div {:class "textarea-container"}
     [:textarea {:id "code-input" :language "clojure" :is "highlighted-code" :placeholder "Enter code here and click on the 'run' button to get output" :auto-height true}]
     [:button {:class "eval-btn" :on-click #((evaluate (.-value (js/document.getElementById "code-input"))))}]]
    [:div {:class "textarea-container"}
     [:textarea {:id "replOutput" :language "clojure-repl" :is "highlighted-code"  :spellCheck "false" :value (:output @state) :readOnly true :auto-height true}]]]])

(defn load-highlighted-code []
  (let [{:keys [default]} highlighted-code]
    (.useTheme highlighted-code "default")))

(load-highlighted-code)