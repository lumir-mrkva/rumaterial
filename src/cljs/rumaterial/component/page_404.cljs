(ns rumaterial.component.page-404
  (:require [rum.core :as rum]
            [sablono.core :refer-macros [html]]
            [material.components :as comp]
            [rumaterial.component.state :as state]
            [rumaterial.url :refer [dispatch!]]
            [rumaterial.routes :as routes]))

(enable-console-print!)

(rum/defc form < rum/static []
  (let [params (state/get-value [:route :params])
        id (or (get-in params [:origin :params :id])
               (get-in params [:origin :params :name]))
        message (or (get-in params [:error :error])
                    "What you are looking for, I do not have.")]
    (comp/mui
      (html
        [:div.rumaterial-form
         [:div.rumaterial-form-context
          [:div.rumaterial-form-paper
           (comp/typography
             {:variant   "h5"
              :key       "title"
              :className "rumaterial-form-title"}
             "Not found")
           (comp/typography
             {:variant   "body1"
              :key       "subtitle"
              :className "rumaterial-form-subtitle"}
             id)
           [:div.rumaterial-user-form
            (comp/grid
              {:container true
               :className "rumaterial-form-main-grid"
               :spacing   24}
              (comp/grid
                {:item true
                 :xs   12}
                (html [:p message]))
              (comp/grid
                {:item true
                 :xs   12}
                (html
                  [:div.rumaterial-form-buttons
                   (if (some? params)
                     (comp/button
                       {:onClick #(.back js/window.history)
                        :color   "outlined"
                        :variant "contained"}
                       "Go back")
                     (comp/button
                       {:href    (routes/path-for-frontend :index)
                        :color   "outlined"
                        :variant "contained"}
                       "go home"))])))]]]]))))
