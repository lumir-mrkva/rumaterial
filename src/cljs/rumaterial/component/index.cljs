(ns rumaterial.component.index
  (:require [material.components :as comp]
            [rumaterial.component.state :as state]
            [rumaterial.component.mixin :as mixin]
            [rumaterial.url :refer [dispatch!]]
            [rum.core :as rum]
            [sablono.core :refer-macros [html]]
            [rumaterial.component.progress :as progress]))

(enable-console-print!)


(rum/defcs form < rum/reactive
                  mixin/retrieve-version [_]
  (let [version (state/react [:version])]
    [:div.rumaterial-page
     [:div.rumaterial-login-layout
      (comp/mui
        (comp/paper
          {:className "rumaterial-login-paper"}
          (html
            [:img {:src    "img/rumaterial.png"
                   :width  "100%"
                   :height "100%"}])
          (html
            (progress/form (nil? version)
                           {:height "200px"}
                           (html [:h2 "rumaterial " version])))))]]))
