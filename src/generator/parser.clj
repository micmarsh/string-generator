(ns generator.parser
    (:use
        clojure.core.typed
        [generator.utils :only [sanitize-spaces]]
        [clojure.core.reducers :only [fold]]))

(ann ^:no-check clojure.core/rand-nth [(Seqable Parsable) -> Parsable])
(def-alias Keyword clojure.lang.Keyword)
;TODO should be Seq[T] -> T or whatever
(ann safe-rand-nth [(Seqable Parsable) -> Parsable])
(defn- safe-rand-nth [sequence]
    (if (> (count sequence) 0)
        (rand-nth sequence)
       ""
        ))

;TODO: define tighter types for evalable items
(ann clojure.lang.RT/get [Map Keyword -> Parsable])
(ann eval-theme [(Map Keyword Parsable) (Vec Keyword) -> Parsable])
(defn- eval-theme [map-obj, themes]
    "looks up correct item for a theme in a map, or selects a random item
    in case of multiple themes"
    (let [result (safe-rand-nth (filter identity (map #(get map-obj %) themes)))]
        (or result
            (or
                (:else map-obj)
                (:default map-obj)))))

(def-alias Parsable
    "Any element of a template"
    (U (Set Parsable) (Vec Parsable) Map String Keyword))
;WARNING: even more general than usual
(ann eval-item [(Seqable Parsable) Parsable Vec -> Seq])
(defn- eval-item
 [new-sequence, item, themes]
    (cond
        (set? item)
            (conj new-sequence (rand-nth (seq item)))
        (vector? item)
            (into new-sequence item)
        (map? item)
            (conj new-sequence (eval-theme item themes))
        ;(string? item) or keyword!
        :else
            (conj new-sequence item)))

;(ann single-vector-passthrough [Vec -> Vec])
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

;(ann eval-loop [Map clojure.lang.Keyword
;    [(U (clojure.lang.Seqable Nothing) nil) -> Boolean]
;    (U [Seq String -> Seq] [Any -> Any]) -> Vec])
(defn- eval-loop [grammar, main-key, done?, finalize]
    (loop [sequence (main-key grammar)]
        (if (done? sequence)
            (finalize sequence)
        ;else
            (recur (single-vector-passthrough sequence grammar)))))

;(ann eval-themes [Map -> Vec])
(defn- eval-themes [grammar]
    (eval-loop grammar :themes (partial every? keyword?) identity))

;(ann eval-main [Map -> Vec])
(defn- eval-main [grammar]
    (eval-loop grammar :main (partial every? string?) sanitize-spaces))

;(ann eval-grammar [Map -> Vec])
(defn eval-grammar [grammar]
    (eval-main (assoc grammar
                        :themes
                        (eval-themes grammar))))
