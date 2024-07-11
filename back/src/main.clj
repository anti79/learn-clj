(ns main
  (:require [compojure.core :as cj]
            [compojure.route :as route]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [clojure.data.json :as json]
            [ring.middleware.cors :refer [wrap-cors]])

  (:require [api.repl]
            [api.lessons]
            [api.tasks])

  (:require [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :refer [response]]))



(cj/defroutes api-routes
  (cj/POST "/api/evaluate" req (json/write-str (api.repl/evaluate (:body req))))
  (cj/POST "/api/solve" req (json/write-str (api.tasks/solve-task (:task-id (:body req)) (:expr (:body req)))))
  ;(cj/POST "/api/evaluate" req (api.repl/evaluate (:body req)))

  (cj/GET "/api/lessons" [] (json/write-str (api.lessons/lessons)))
  (cj/GET "/api/lesson/:id" [id] (json/write-str (api.lessons/lesson (Integer/parseInt id))))
  (cj/GET "/api/tasks" [] (json/write-str (api.tasks/all-tasks)))
  (cj/GET "/api/tasks/by-lesson/:id" [id] (json/write-str (api.tasks/tasks-by-lesson (Integer/parseInt id))))
  (cj/GET "/api/tasks/total" [] (str (api.tasks/count-tasks)))
  (cj/GET "/api/tasks/:id" [id] (json/write-str (api.tasks/task-by-id (Integer/parseInt id)))))
  
  



;;;;;;;; CORS 



(def cors-headers
  "Generic CORS headers"
  {"Access-Control-Allow-Origin"  "http://localhost:8080"
   "Access-Control-Allow-Headers" "*"
   "Access-Control-Allow-Credentials" "false"
   "Access-Control-Allow-Methods" "*"})

(defn preflight?
  "Returns true if the request is a preflight request"
  [request]
  (= (request :request-method) :options))

(defn all-cors
  "Allow requests from all origins - also check preflight"
  [handler]
  (fn [request]
    (if (preflight? request)
      {:status 200
       :headers cors-headers
       :body "preflight complete"}
      (let [response (handler request)]
        (update-in response [:headers]
                   merge cors-headers)))))

;;;;;;;

(def api-app
  (-> api-routes
      (wrap-resource "public")
      (wrap-content-type)
      (wrap-json-body {:keywords? true :bigdecimals? true})
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))

(defn -main []
  (let [port 3000]
    (println "Starting server on port" port)
    (run-jetty (all-cors api-app) {:port port})))

(-main)