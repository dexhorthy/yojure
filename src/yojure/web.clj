(ns yojure.web
(:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
  [compojure.handler :refer [site]]
  [compojure.route :as route]
  [clojure.java.io :as io]
  [ring.adapter.jetty :as jetty]
  [environ.core :refer [env]]))
(defn splash []
{:status 200
:headers {"Content-Type" "text/plain"}
:body "Yo"})




(defn yo-handler [params]
  (println "got request with params" params)
  {:status 200 :body "whatever"})

(defroutes app
  (GET "/" []
    (splash))
  (GET "/yo" {params :params}
    (yo-handler params)))


(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 3000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))
