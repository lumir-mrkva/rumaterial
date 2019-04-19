(ns repl.user
  (:require [ring.middleware.reload :refer [wrap-reload]]
            [clojure.java.shell :refer [sh]]
            [figwheel-sidecar.repl-api :as figwheel]
            [rumaterial.server]))

;; Let Clojure warn you when it needs to reflect on types, or when it does math
;; on unboxed numbers. In both cases you should add type annotations to prevent
;; degraded performance.
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(def http-handler
  (wrap-reload #'rumaterial.server/app))

(defn run []
  (figwheel/start-figwheel!))

(def browser-repl figwheel/cljs-repl)

(run)