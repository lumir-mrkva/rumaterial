(ns rumaterial.routes
  (:require [bidi.bidi :as b]
    #?(:clj
            [environ.core :refer [env]])
            [cemerick.url :refer [map->query]]))

(def backend
  ["/" [["" {:get :index}]
        ["version" {:get :version}]
        [true {:get :index}]]])

(def frontend ["" {"/"                 :index}])

(defn- path
  [routes prefix handler params query]
  (if (some? query)
    (str prefix (b/unmatch-pair routes {:handler handler
                                        :params  params}) "?" (map->query query))
    (str prefix (b/unmatch-pair routes {:handler handler
                                        :params  params}))))

(defn path-for-frontend
  ([handler] (path-for-frontend handler {} nil))
  ([handler params] (path-for-frontend handler params nil))
  ([handler params query] (path frontend "" handler params query)))

(defn path-for-backend
  ([handler] (path-for-backend handler {} nil))
  ([handler params] (path-for-backend handler params nil))
  ([handler params query] (path backend "" handler params query)))
