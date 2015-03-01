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
   [ring.util.response :as resp]
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

(def selected-tenants (atom '(
  [37.8001062,-122.4268326]
  [37.8001965,-122.4260634]
  [37.8002048,-122.4260565]
  [37.8002059,-122.4260034]
  [37.800207,-122.423874]
  [37.800281,-122.423322]
  [37.800365,-122.428826]
  [37.800392,-122.428087]
  [37.8004086,-122.4244226]
  [37.80041,-122.4244478]
  [37.800428,-122.4242992]
)))

(defn pop-tenant[]
   (let [item (first @selected-tenants)]
     (swap! selected-tenants (fn [v] (rest v)))
     {:lat (first item) :lon (second item)}))

; (pop-tenant)

(defroutes app-routes
  (GET "/" [] 
    (resp/redirect "/app"))
  (GET "/app" [] 
    (events-view))
  (GET "/api/0" [] 
    (json-response 
     {"content" "hello"}))
  (GET "/api/tenant/:id/events" [id]
     (json-response
      (tenant-events id) ))
  (GET "/api/tenant" []
     (json-response
       (pop-tenant)))
  (GET "/api/contracts" []
     (json-response
      [] ))
  (GET "/api/leases" []
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
