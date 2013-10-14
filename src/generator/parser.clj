(ns generator.parser
    (:use
        clojure.core.typed
        [generator.utils :only [sanitize-spaces]]
        [clojure.core.reducers :only [fold]]
        [marshmacros.test :only [defntest]]))

;TODO should be Seq[T] -> T or whatever
(ann safe-rand-nth [Seq -> Any])
(defntest safe-rand-nth [sequence]
    {[[]] nil
    [[42]] 42}
    (if (> (count sequence) 0)
        (rand-nth sequence)
    ;else nil
        ))

;TODO: define tighter types for evalable items
(ann eval-theme [Map Seq -> Any])
(defn- eval-theme [map-obj, themes]
    "looks up correct item for a theme in a map, or selects a random item
    in case of multiple themes"
    (let [result (safe-rand-nth (filter identity (map map-obj themes)))]
        (or result
            ( or
                (:else map-obj)
                (:default map-obj)))))

;WARNING: even more general than usual
(ann eval-item [Seq Any Vec -> Seq])
(defn- eval-item
 [new-sequence, item, themes]
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

(ann single-vector-passthrough [Vec -> Vec])
(defn- single-vector-passthrough
    [sequence, grammar]
    (let [themes (grammar :themes)];these are each b/c themes are being run through here, think
        ; of a better way to separate this shit later
        ;(println "what up" sequence)
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

(ann eval-loop [Map clojure.lang.Keyword [-> Boolean] [Vec -> Vec] -> Vec])
(defn- eval-loop [grammar, main-key, done?, finalize]
    (loop [sequence (main-key grammar)]
        (if (done? sequence)
            (finalize sequence)
        ;else
            (recur (single-vector-passthrough sequence grammar)))))

(ann eval-main [Map -> Vec])
(defn- eval-themes [grammar]
    (eval-loop grammar :themes (partial every? keyword?) identity))

(ann eval-main [Map -> Vec])
(defn- eval-main [grammar]
    (eval-loop grammar :main (partial every? string?) sanitize-spaces))

(ann eval-grammar [Map -> Vec])
(defn eval-grammar [grammar]
    (eval-main (assoc grammar
                        :themes
                        (eval-themes grammar))))
