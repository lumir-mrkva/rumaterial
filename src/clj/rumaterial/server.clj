(ns rumaterial.server
  (:gen-class)
  (:import (clojure.lang ExceptionInfo))
  (:require [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [rumaterial.handler :as handler :refer :all]
            [org.httpkit.server :refer [run-server]]
            [bidi.ring :refer [make-handler]]
            [bidi.bidi :refer [match-pair match-route*]]
            [clojure.tools.logging :as log]
            [rumaterial.routes :as routes]))

(defn wrap-client-exception
  [handler]
  (fn [request]
    (try
      (handler request)
      (catch ExceptionInfo e
        (dissoc (ex-data e) :headers)))))

(defn wrap-fallback-exception
  [handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception e
        {:status 500
         :body   (Throwable->map e)}))))

(defn wrap-path-params
  [handler]
  (fn [{:keys [uri] :as request}]
    (let [{:keys [route-params]} (match-route* routes/backend uri request)]
      (handler
        (assoc request :path-params route-params)))))

(def app
  (-> (make-handler routes/backend handler/dispatch)
      (wrap-resource "public")
      wrap-client-exception
      wrap-fallback-exception
      wrap-path-params
      wrap-json-params
      wrap-json-response
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
      wrap-gzip))

(defn -main [& [port]]
  (log/info "Rumaterial is starting...")
  (let [port (or port 8080)]
    (run-server app {:port port} :thread 8 :queue-size 300000)
    (log/info "Rumaterial running on port" port)))
