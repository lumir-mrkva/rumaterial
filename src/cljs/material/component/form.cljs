(ns material.component.form
  (:refer-clojure :exclude [comp])
  (:require [material.components :as cmp]
            [material.icon :as icon]
            [sablono.core :refer-macros [html]]))

(defn item [name value]
  (html
    [:div {:class "rumaterial-row-space"
           :key   (str "sri-" name)}
     [:span name]
     [:span value]]))

(defn message [comp]
  (html
    [:span.rumaterial-message
     (icon/info {:style {:marginRight "8px"}})
     [:span comp]]))

(defn item-icon [icon comp]
  (html
    [:span.rumaterial-message
     (icon {:style {:marginRight "8px"
                    :fontSize    "16px"}})
     [:span comp]]))

(defn item-id [id]
  (html
    [:div.rumaterial-form-card-icon-item
     (icon/fingerprint
       {:className "rumaterial-form-card-icon"})
     [:span.rumaterial-form-card-icon-item-id
      (cmp/typography
        {:color     "textSecondary"
         :className "rumaterial-form-card-icon-text"} id)]]))

(defn item-labels [labels]
  (html
    [:div {:class "rumaterial-form-card-labels"
           :key   "item-labels"}
     labels]))

(defn section
  ([name]
   (section name nil))
  ([name button]
   (html
     [:div {:class "rumaterial-form-section"
            :id    name}
      [:div
       (cmp/typography
         {:variant "h6"} name)]
      [:div button]])))

(defn subsection
  ([name]
   (subsection name nil))
  ([name button]
   (html
     [:div.rumaterial-form-section
      [:div
       (cmp/typography
         {:variant "subtitle1"} name)]
      [:div button]])))

(defn open-in-new [text href]
  (html
    [:a {:href      href
         :className "rumaterial-new-tab"
         :target    "_blank"}
     [:div text]
     [:div (icon/open-in-new
             {:className "rumaterial-new-tab-ico"})]]))