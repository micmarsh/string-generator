(ns generator.parser)

(defn- eval-item [acc, item]
    (cond
        (list? item)
            (conj acc (rand-nth item))
        (vector? item)
            (apply conj acc item)
        ;(string? item) or keyword!
        :else
            (conj acc item)))

(defn- single-vector-passthrough [sequence, grammar]
    (reduce
    (fn[acc, item]
        (if
            (keyword? item)
                (eval-item acc (grammar item))
            ;else
                (eval-item acc item)
           ))
    [] sequence ))

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
    (eval-loop grammar :themes keywords? #(vec %)))

(defn- eval-main [grammar]
    (eval-loop grammar :main strings? #(apply str %)))

(defn eval-grammar [grammar]
    (eval-main (assoc grammar
                        :themes
                        (eval-themes grammar))))
