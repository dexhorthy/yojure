(ns yojure.web
(:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
  [clj-http.client :as client]
  [compojure.handler :refer [site]]
  [compojure.route :as route]
  [clojure.java.io :as io]
  [ring.adapter.jetty :as jetty]
  [environ.core :refer [env]]))

(defn splash []
  {:status 200
  :headers {"Content-Type" "text/plain"}
  :body "Yo"})

(defn send-yo [username token]
  (println "sending yo with username " username " token " token)
  (try 
    (client/post "http://api.justyo.co/yo/" {:query-params {:username username :api_token token}})
    (catch Exception e 
      (println "oh noes!" (.getClass e) (.getMessage e)))))

(defn yo-handler [params]
  (println "got request with params" params)
  (send-yo (params :username) (env :yo-token))
  {:status 200 :body "whatever"})

(defroutes app
  (GET "/" []
    (splash))
  (GET "/yo" {params :params}
    (yo-handler params)))


(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 3000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))