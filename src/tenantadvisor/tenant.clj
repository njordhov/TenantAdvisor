(ns tenantadvisor.tenant
  (:require [clojure-csv.core :as csv])
  (:require
     [clj-http.client :as client]
     [net.cgrand.enlive-html :as html]))


(def url "https://docs.google.com/spreadsheets/d/1k1GX_CR0Fv8G3hPi2RCNkmU8t7CGbzyGzswS6GC14G4/pubhtml")

(defn get-info [url]
  (->> (html/select (html/html-resource (java.net.URL. url)) [:table.waffle :tr :td])
       (map :content)
       (remove nil?)
       (partition 4)
       (rest)))

(defn labeled-info [url]
  (map
   #(zipmap
     [:label :name :description :advice]
     (map first %))
   (get-info url)))

(defn tenant-events [id]
  (labeled-info url))

; (tenant-events 123)
  
  