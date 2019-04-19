(ns rumaterial.handler
  (:require [clojure.walk :refer [keywordize-keys]]
            [clojure.java.io :as io]
            [environ.core :refer [env]]
            [rumaterial.version :as version]))

(defn resp-ok
  ([] {:status 200})
  ([response] {:status 200
               :body   response}))

;;; Handler

(defmulti dispatch identity)

;; Index handler

(defmethod dispatch :index [_]
  (fn [_]
    {:status  200
     :headers {"Content-Type" "text/html"}
     :body    (slurp (io/resource "public/index.html"))}))

;; Version handler

(defmethod dispatch :version [_]
  (fn [_]
    (->> (version/info)
         (resp-ok))))