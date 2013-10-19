(ns generator.parser
    (:use
        clojure.core.typed
        [generator.types :only [Keyword Parsable Template ThemedTemplate]]
        [generator.utils :only [sanitize-spaces]]
        [clojure.core.reducers :only [fold]]))

(ann ^:no-check clojure.core/rand-nth [(Seqable (Option Parsable))  -> Parsable])
;TODO should be Seq[T] -> T or whatever
(ann safe-rand-nth [(Seqable (Option Parsable)) -> Parsable])
(defn- safe-rand-nth [sequence]
    (if (> (count sequence) 0)
        (rand-nth sequence)
       ""
        ))

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

(ann clojure.core/conj [(Seqable Parsable) Parsable -> (Seqable Parsable)])
(ann clojure.core/seq [Parsable -> (Seqable Parsable)])
(ann clojure.core/into [(Seqable Parsable) Parsable -> (Seqable Parsable)])

(ann eval-item [(Seqable Parsable) Parsable (Vec Keyword) -> (Seqable Parsable)])
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

(ann single-vector-passthrough [(Seqable Parsable) ThemedTemplate -> (Seqable Parsable)])
(defn- single-vector-passthrough
    [sequence, grammar]
    (let [themes (get grammar :themes)]
        (reduce
        (fn> [new-sequence :- (Seqable Parsable)
             item :- Parsable]
            (if
                (keyword? item)
                    (let [lookup (get grammar item)]
                         (eval-item new-sequence, lookup, themes))
                ;else
                    (eval-item new-sequence, item, themes)
               ))
        [] sequence )))


(ann eval-main [ThemedTemplate -> String])
(defn- eval-main [grammar]
    (loop> [sequence :- (Seqable Parsable)
                (get grammar :main)]
        (if (every? string? sequence)
            (sanitize-spaces sequence)
        ;else
            (recur (single-vector-passthrough sequence grammar)))))

(ann eval-grammar [Template (Vec Keyword) -> String])
(defn eval-grammar [grammar, themes]
    (eval-main (assoc grammar
                        :themes themes)))
