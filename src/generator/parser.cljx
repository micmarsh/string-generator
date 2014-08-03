(ns generator.parser
  (:use [generator.utils :only (sanitize-spaces)]))

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
  [new-sequence themes item]
  (cond
    (set? item)
      (conj! new-sequence (rand-nth (seq item)))
    (vector? item)
      (into! new-sequence item)
    (map? item)
      (conj! new-sequence (eval-theme themes item))
    :else
      (conj! new-sequence item)))

(defn single-vector-passthrough
  [eval-item! grammar sequence]
  (let [themes (get grammar :themes)]
    (persistent!
      (reduce
        (fn [new-sequence item]
          (if (keyword? item)
            (let [lookup (get grammar item)]
              (eval-item! new-sequence themes lookup))
            (eval-item! new-sequence themes item)))
        (transient [])
        sequence))))

(defn eval-main [done? eval-item! grammar]
  (loop [sequence (:main grammar)]
    (if (done? sequence)
      (sanitize-spaces sequence)
      (recur
        (single-vector-passthrough
          eval-item! grammar sequence)))))

(defn eval-grammar
  ([grammar] (eval-grammar grammar [ ]))
  ([grammar themes]
    (eval-main
      (partial every? string?)
      eval-item!
      (assoc grammar :themes themes))))
