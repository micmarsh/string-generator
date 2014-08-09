(ns generator.parser
  (:use [generator.utils :only (sanitize-spaces)]))

(defn- safe-rand-nth [sequence]
  (if (> (count sequence) 0)
    (rand-nth sequence)
    ""))

(defn- eval-theme [themes map-obj]
  (let [result (->> themes
                (map map-obj)
                (filter identity)
                safe-rand-nth)]
    (or result
      (or
        (:else map-obj)
        (:default map-obj)))))

(defn into! [mutable-seq source]
  (reduce #(conj! %1 %2) mutable-seq source))

(defn- eval-item! [themes new-sequence item]
  (cond
    (set? item)
      (conj! new-sequence (rand-nth (seq item)))
    (vector? item)
      (into! new-sequence item)
    (map? item)
      (conj! new-sequence (eval-theme themes item))
    :else
      (conj! new-sequence item)))

(defn single-vector-passthrough [themes sequence]
  (persistent!
    (reduce
      (partial eval-item! themes)
      (transient [])
      sequence)))

(defn eval-grammar
  ([grammar] (eval-grammar grammar [ ]))
  ([grammar themes]
    (loop [result grammar]
      (if (every? string? result)
        (sanitize-spaces result)
        (recur
          (single-vector-passthrough themes result))))))
