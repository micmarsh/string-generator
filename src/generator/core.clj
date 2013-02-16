(ns generator.core)

; reserved keywords: :main, :themes

(def example {:main [:greeting  " " :second-phrase]
                :greeting [:greeting-word :middle :world]
                    :greeting-word (list "Hello" "Goodbye")
                    :middle (list " " ", ")
                    :world [ :word :punc ]
                        :word (list "World" "Mother" "Brother" "earthlings")
                        :punc (list "!" "." "?")
                :second-phrase [:emotion " to meet you, " :end-phrase :punc]
                    :emotion (list "Pleased" "Inconvenienced" "Aroused")
                    :end-phrase (list "hope you guess my name"
                                      "I need to go sleep now")
                        })

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
    (reduce #(and (checker %2) %1)
        true sequence))

(defn- strings? [sequence]
    (check-all sequence string?))

(defn- keywords? [sequence]
    (check-all sequence keyword?))

(defn eval-grammar [grammar]
    (loop [sequence (:main grammar)]
        (if (strings? sequence)
            (apply str sequence)
            (recur (single-vector-passthrough sequence grammar)))))

(defn -main [& args]
    (eval-grammar example))