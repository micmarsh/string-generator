(ns generator.parser)

(defn- eval-theme [map-obj, themes]
    (rand-nth (filter identity (map map-obj themes))))

(defn- eval-item [acc, item, themes]
    (cond
        (list? item)
            (conj acc (rand-nth item))
        (vector? item)
            (apply conj acc item)
        (map? item)
            (conj acc (eval-theme item themes))
        ;(string? item) or keyword!
        :else
            (conj acc item)))

(defn- single-vector-passthrough [sequence, grammar]
    (let [themes (grammar :themes)];these are each b/c themes are being run through here, think
        ; of a better way to separate this shit later
        (reduce
        (fn [acc, item]
            (if
                (keyword? item)
                    (let [lookup (grammar item)]
                         (eval-item acc, (if lookup lookup item), themes))
                ;else
                    (eval-item acc, item, themes)
               ))
        [] sequence )))


(defn- check-all [sequence, checker]
    "Checks to see a a condition is true of every element in a sequence. May already exist in clojure"
    (reduce #(and (checker %2) %1)
        true sequence))

(defn- strings? [sequence]
    (check-all sequence string?))

(defn- keywords? [sequence]
    (check-all sequence keyword?))

(defn- eval-loop [grammar, main-key, check?, finalize]
    (loop [sequence (main-key grammar)]
        (if (check? sequence)
            (finalize sequence)
        ;else
            (recur (single-vector-passthrough sequence grammar)))))

(defn- eval-themes [grammar]
    (eval-loop grammar :themes keywords? identity))

(defn- eval-main [grammar]
    (eval-loop grammar :main strings? #(apply str %)))

(defn eval-grammar [grammar]
    (eval-main (assoc grammar
                        :themes
                        (eval-themes grammar))))
