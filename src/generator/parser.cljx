(ns generator.parser
  (:use [generator.utils :only [sanitize-spaces]]))

(defn- safe-rand-nth [sequence]
  (if (> (count sequence) 0)
    (rand-nth sequence)
    ""))

(defn- eval-theme
  [themes map-obj]
  (let [result (safe-rand-nth (filter identity (map #(get map-obj %) themes)))]
    (or result
      (or
        (:else map-obj)
        (:default map-obj)))))

(defn into! [mutable-seq source]
  (reduce #(conj! %1 %2) mutable-seq source))

(defn- eval-item!
  [new-sequence item themes]
  (cond
    (set? item)
      (conj! new-sequence (rand-nth (seq item)))
    (vector? item)
      (into! new-sequence item)
    (map? item)
      (conj! new-sequence (eval-theme themes item))
    :else
      (conj! new-sequence item)))

(defn- single-vector-passthrough
  [grammar sequence]
  (let [themes (get grammar :themes)]
    (persistent!
      (reduce
        (fn [new-sequence item]
          (if (keyword? item)
            (let [lookup (get grammar item)]
              (eval-item! new-sequence lookup themes))
            (eval-item! new-sequence item themes)))
        (transient [])
        sequence))))

(defn- eval-main [grammar]
  (loop [sequence (:main grammar)]
    (if (every? string? sequence)
      (sanitize-spaces sequence)
      (recur (single-vector-passthrough grammar sequence)))))

(defn eval-grammar
  ([grammar] (eval-grammar grammar [ ]))
  ([grammar themes]
    (eval-main (assoc grammar :themes themes))))
