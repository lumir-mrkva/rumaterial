(ns rumaterial.component.layout
  (:require [rum.core :as rum]
            [rumaterial.view :as view]
            [rumaterial.component.state :as state]))

(enable-console-print!)

(def single-pages
  #{:login :error :unauthorized})

(defn- page-layout?
  [handler]
  (not (contains? single-pages handler)))

(defn- document-title
  [page-title]
  (set! (-> js/document .-title)
        (str page-title)))

(rum/defc page-single < rum/static [route]
  (view/dispatch route))

(rum/defc page-layout < rum/reactive [route]
  (document-title "rumaterial")
  [:div.rumaterial-root
   [:main.rumaterial-context
    [:div.rumaterial-route (view/dispatch route)]]])

(rum/defc layout < rum/reactive []
  (let [{:keys [handler] :as route} (state/react state/route-cursor)]
    (if (page-layout? handler)
      (page-layout route)
      (page-single route))))

(defn mount!
  []
  (rum/mount (layout) (.getElementById js/document "layout")))
