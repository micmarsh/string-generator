(ns generator.parser)

(defn- safe-rand-nth [sequence]
    "Checks size of sequence before calling rand-nth, returns nil if nothing in sequence"
    (if (> (count sequence) 0)
        (rand-nth sequence)))

(defn- eval-theme [map-obj, themes]
    (let [result (safe-rand-nth (filter identity (map map-obj themes)))]
        (or result
            ( or
                (:else map-obj)
                (:default map-obj)))))

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
                         (eval-item acc, (or lookup item), themes))
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
    (eval-loop grammar :main strings? (sanitize-spaces %))

(defn- add-front-spaces [strings]
    ())

(defn- append-and-split [strings]
    (clojure.string/split
        (apply str (map #(str % " ") strings)) #" +"))

(defn- sanitize-spaces [strings]
    (let [puncuation #{"," "." "?" "!"}]
        (apply str
            (map (fn[word](if (contains? puncuation word) word (str " " word)))
                (append-and-split strings)))))

;doesn't catch everything thanks to sticking spaces in front! Edge cases:
;the first word of every line. So we want to check to see if the word is either
;the first word of a line, or puncuation.

;this mess contains some wisdom:
;(let [list ["foo" "\n" "bar" "." "baz" "!"]]
    ;(map-indexed
        ;(fn[i item]
            ;(if (or (= i 0) (= (list i) "\n") (contains? #{"." "!"} item)) "poop" "pee")) list))
;this would be a good replacement for line 66 (with the "list", "poop", "pee" "#{"." "!"}"
; "(= (list i) "\n")" as good stuff, of course )


(defn eval-grammar [grammar]
    (eval-main (assoc grammar
                        :themes
                        (eval-themes grammar))))
