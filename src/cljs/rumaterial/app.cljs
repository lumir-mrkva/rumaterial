(ns rumaterial.app
  (:require [rumaterial.router :as router]
            [rumaterial.component.layout :as layout]
            [rumaterial.component.message :as message]))

;; Starting router

(router/start)

;; Mounting layout

(layout/mount!)

;; Mounting message

(message/mount!)
