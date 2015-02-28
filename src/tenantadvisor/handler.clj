(ns tenantadvisor.handler
  (:use compojure.core)
  (:require 
   [compojure.core :refer [context]]
   [compojure.handler :as handler]
   [compojure.route :as route]
   [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
   [ring.adapter.jetty :as jetty]
   [environ.core :refer [env]]
   [cheshire.core :as json]
   [tenantadvisor.views :refer [events-view]]
   [tenantadvisor.tenant :refer [tenant-events]]))

; see http://www.jayway.com/2014/09/08/combining-a-site-and-api-in-compojure/

(defn json-response [body]
  (let [body (json/generate-string body)] 
    {:status 200
     :headers {"Content-Type" "application/json; charset=utf-8"
               "Content-Length" (str (count body))
               "Access-Control-Allow-Origin" "*"} ; security risk...
     :body body}))

(defroutes app-routes
  (GET "/" []  
    (events-view))
  (GET "/api/0" [] 
    (json-response 
     {"content" "hello"}))
  (GET "/api/tenant/:id/events" [id]
     (json-response
      (tenant-events id) ))
  (GET "/api/contracts" []
     (json-response
      [] ))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (handler/site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
