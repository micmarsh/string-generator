(ns generator.parser
    (:use [generator.utils :only [sanitize-spaces]]
            [clojure.core.reducers :only [fold]]
            [marshmacros.test :only [defntest]]))

(defntest safe-rand-nth [sequence]
    {[[]] nil
    [[42]] 42}
    (if (> (count sequence) 0)
        (rand-nth sequence)
    ;else nil
        ))

(defn- eval-theme [map-obj, themes]
    "looks up correct item for a theme in a map, or selects a random item
    in case of multiple themes"
    (let [result (safe-rand-nth (filter identity (map map-obj themes)))]
        (or result
            ( or
                (:else map-obj)
                (:default map-obj)))))

(defn- eval-item [new-sequence, item, themes]
    (cond
        (list? item)
            (conj new-sequence (rand-nth item))
        (vector? item)
            (into new-sequence item)
        (map? item)
            (conj new-sequence (eval-theme item themes))
        ;(string? item) or keyword!
        :else
            (conj new-sequence item)))

(defn- single-vector-passthrough [sequence, grammar]
    (let [themes (grammar :themes)];these are each b/c themes are being run through here, think
        ; of a better way to separate this shit later
        (reduce ;(partial fold  concat)
        (fn [new-sequence, item]
            (if
                (keyword? item)
                    (let [lookup (grammar item)]
                         (eval-item new-sequence, (or lookup item), themes))
                ;else
                    (eval-item new-sequence, item, themes)
               ))
        [] sequence )))


(defn- eval-loop [grammar, main-key, check?, finalize]
    (loop [sequence (main-key grammar)]
        (if (check? sequence)
            (finalize sequence)
        ;else
            (recur (single-vector-passthrough sequence grammar)))))

(defn- eval-themes [grammar]
    (eval-loop grammar :themes (partial every? keyword?) identity))

(defn- eval-main [grammar]
    (eval-loop grammar :main (partial every? string?) sanitize-spaces))

(defn eval-grammar [grammar]
    (eval-main (assoc grammar
                        :themes
                        (eval-themes grammar))))
