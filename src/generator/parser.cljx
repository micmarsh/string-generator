(ns generator.parser
    (:use
        [generator.utils :only [sanitize-spaces]]))

(defn- safe-rand-nth [sequence]
    (if (> (count sequence) 0)
        (rand-nth sequence)
       ""
        ))

(defn- eval-theme [map-obj, themes]
    "looks up correct item for a theme in a map, or selects a random item
    in case of multiple themes"
    (let [result (safe-rand-nth (filter identity (map #(get map-obj %) themes)))]
        (or result
            (or
                (:else map-obj)
                (:default map-obj)))))

(defn- eval-item
 [new-sequence, item, themes]
    (cond
        (set? item)
            (conj new-sequence (rand-nth (seq item)))
        (vector? item)
            (into new-sequence item)
        (map? item)
            (conj new-sequence (eval-theme item themes))
        :else
            (conj new-sequence item)))

(defn- single-vector-passthrough
    [sequence, grammar]
    (let [themes (get grammar :themes)]
        (reduce
        (fn [new-sequence item]
            (if
                (keyword? item)
                    (let [lookup (get grammar item)]
                         (eval-item new-sequence, lookup, themes))
                ;else
                    (eval-item new-sequence, item, themes)
               ))
        [] sequence )))


(defn- eval-main [grammar]
    (loop [sequence (get grammar :main)]
        (if (every? string? sequence)
            (sanitize-spaces sequence)
        ;else
            (recur (single-vector-passthrough sequence grammar)))))

(defn eval-grammar
    ([grammar] (eval-grammar grammar [ ]))
    ([grammar themes]
        (eval-main (assoc grammar :themes themes))))
