(ns rumaterial.view
  (:require [rumaterial.component.index :as index]
            [rumaterial.component.page-403 :as page-403]
            [rumaterial.component.page-404 :as page-404]
            [rumaterial.component.page-error :as page-error]))

(defmulti dispatch (fn [route] (:handler route)))

(defmethod dispatch nil
  [_]
  (page-404/form))

(defmethod dispatch :not-found
  [_]
  (page-404/form))

(defmethod dispatch :unauthorized
  [_]
  (page-403/form))

(defmethod dispatch :error
  [route]
  (page-error/form route))

(defmethod dispatch :index
  [_]
  (index/form))
