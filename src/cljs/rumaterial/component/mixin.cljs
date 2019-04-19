(ns rumaterial.component.mixin
  (:require [rum.core :as rum]
            [rumaterial.component.state :as state]
            [rumaterial.ajax :as ajax]
            [rumaterial.routes :as routes]))

(defn refresh-form
  ([handler] (refresh-form handler 2000))
  ([handler ms]
   {:did-mount    (fn [state]
                    (let [comp (:rum/react-component state)
                          callback #(do (handler (first (:rum/args state)))
                                        (rum/request-render comp))
                          interval (js/setInterval callback ms)]
                      (assoc state ::interval interval)))
    :will-unmount (fn [state]
                    (js/clearInterval (::interval state))
                    (dissoc state ::interval))}))

(defn version-handler
  []
  (ajax/get
    (routes/path-for-backend :version)
    {:on-success (fn [{:keys [response]}]
                   (state/update-value [:version] response state/layout-cursor)
                   (state/set-value response))}))

(def retrieve-version
  {:init
   (fn [state]
     (when (nil? (state/get-value [:version])) (version-handler))
     state)})

(defn init-form
  [handler]
  {:init      (fn [state _]
                (state/reset-form)
                (handler (first (:rum/args state)))
                state)
   :did-mount (fn [state]
                (.scrollTo js/window 0 0)
                state)})