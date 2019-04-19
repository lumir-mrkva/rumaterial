(ns rumaterial.utils
  (:require [clojure.walk]
            #?(:clj [flatland.ordered.map :refer [ordered-map]])
            #?(:clj [clojure.math.numeric-tower :refer [floor expt]])))


#?(:cljs (def ^:private floor (.-floor js/Math)))
#?(:cljs (def ^:private expt (.-pow js/Math)))
#?(:cljs (def ^:private rounding-const 1000000))
#?(:clj  (def ^:private log10 #(java.lang.Math/log10 %))
   :cljs (def ^:private log10 (or (.-log10 js/Math)
                                  #(/ (.round js/Math
                                              (* rounding-const
                                                 (/ (.log js/Math %)
                                                    js/Math.LN10)))
                                      rounding-const))))

(defn remove-el
  "Remove element in `vector` on given `index`"
  [vector index]
  (vec (concat
         (subvec vector 0 index)
         (subvec vector (inc index)))))

(defn select-keys*
  "Better select-keys supporting also nested maps. E.g. [[:name] [:node :nodeName]]"
  [m paths]
  (->> paths
       (map (fn [p]
              [(last p) (get-in m p)]))
       (into {})))

(defn merge-data
  "Recursively merge map delta to current data structure."
  [m delta]
  (cond (and (map? m)
             (map? delta)) (merge-with merge-data m delta)
        :else delta))

(defn map-values
  "Walk the map and return all values as set"
  [m]
  (remove nil?
          (set
            (flatten
              (clojure.walk/prewalk
                (fn [x]
                  (cond
                    (map? x) (vals x)
                    (number? x) (str x)
                    (boolean? x) (str x)
                    :else (identity x))) m)))))

(defn empty-or-nil?
  [val]
  (if (coll? val)
    (empty? val)
    (nil? val)))

(defn clean
  "remove pairs of key-value that are nil or empty from a (possibly nested) map."
  [map]
  (clojure.walk/postwalk
    #(if (map? %)
       (let [nm #?(:clj  (if (instance? flatland.ordered.map.OrderedMap %)
                           (ordered-map)
                           {})
                   :cljs {})
             m (into nm (remove (comp empty-or-nil? val) %))]
         (when (seq m) m))
       %)
    map))

(defn name-value->map
  [name-value-coll]
  (->> name-value-coll
       (map #(hash-map (keyword (:name %)) (:value %)))
       (into {})))

(defn map->name-value
  [map-col]
  (->> map-col
       (map (fn [l] {:name  (name (key l))
                     :value (val l)}))
       (into [])))

(defn ->nano
  [number]
  (when number (* number 1000000000)))

(defn nano->
  [number]
  (when number (/ number 1000000000)))

(def ^:private pows [[100 "googol"]
                     [33 "d"]
                     [30 "n"]
                     [27 "o"]
                     [24 "S"]
                     [21 "s"]
                     [18 "Q"]
                     [15 "q"]
                     [12 "t"]
                     [9 "B"]
                     [6 "M"]
                     [3 "K"]
                     [0 ""]])

(defn shortint
  [num]
  (let [base-pow (int (floor (log10 num)))
        [base-pow suffix] (first (filter (fn [[base _]] (>= base-pow base)) pows))
        value (int (float (/ num (expt 10 base-pow))))]
    (str value suffix)))
