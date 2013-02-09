(ns generator.core)

;reserved keywords: :main
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
                                      "I need to go poop now")
                        })

(def bigtest {:main [:first " " :second "! " :third " " :fourth :fifth :sixth]
                    :first (list "foo" "var" "val" "bar" "baz")
                    :second [:first :third]
                    :third [:woot :yeah "wooooo"]
                        :woot (list "woot" "foo" "baz")
                        :yeah [:oh :shit]
                            :oh (list "string1" "a" "b")
                            :shit (list "fuck" "shit" "damn")
                    :fourth [:fifth " nope, incorrect " :third :last :last1]
                        :last ["the last one " :last1 :first :yeah :moar]
                        :last1 (list "1" "2" "3" "four")
                        :moar [:first :second]
                    :fifth [:shit " yeah "]
                    :sixth [:first "! " :second "third" :fourth :fifth :second :second]
                    })
;da rules: given a :key:
; map [vec...] (fn[item]( (if list? item) (rand-nth item) ()  ))

;preserve this for benchmarks
(defn follow-keyword [k, grammar]
    (let [value (grammar k)
          proc-vector (fn[v, grammar](map
            (fn[item](cond
                (keyword? item)
                        (follow-keyword item grammar)
                (string? item)
                        (str item))) v) )]
        (cond
            (vector? value)
                (apply str (proc-vector value grammar))
            (list? value)
                (rand-nth value))))
;*for "replacing" shit, having a reduction with (apply conj [accumulator] [vec to conj])
; somewhere in there would be the shit
;loop through the whole thing, replacing keywords with either their list or
;the things in their vector AND evaling list AT THE SAME TIME:
; [:greeting " " ...] -> [:greeting-word :middle :world " " ...]
;-> [(list ....) (list ....) :word :punc " " ...] -> ["Goodbye" ", " "Brother" (list ... ) (list ...) " " ...]
;when everything's a string, kill loop and (apply str [...])
(defn single-vector-passthrough [sequence, grammar]
    (reduce (fn[acc, item]
        (cond
            (keyword? item)
                (let [value (grammar item)]
                    (cond
                        (vector? value)
                            (apply conj acc value)
                        (list? value)
                            (conj acc value)))
            (list? item)
                (conj acc (rand-nth item))
            (string? item)
                (conj acc item)))
    [] sequence ))

(defn strings? [sequence]
    (reduce #(and (string? %2) %1)
        true sequence))

(defn eval-grammar [grammar]
    (loop [sequence (:main grammar)]
        (if (strings? sequence)
            (apply str sequence)
            (recur (single-vector-passthrough sequence grammar)))))

(defn no-stack-eval [grammar]
    (loop [sequence (:main grammar)]
        (if (reduce #(and (string? %2) %1)
                true sequence)
            (apply str sequence)
            (recur  (reduce (fn[acc, item]
                    (cond
                        (keyword? item)
                            (let [value (grammar item)]
                                (cond
                                    (vector? value)
                                        (apply conj acc value)
                                    (list? value)
                                        (conj acc value)))
                        (list? item)
                            (conj acc (rand-nth item))
                        (string? item)
                            (conj acc item)))
                [] sequence )))))


(defn -main [& args]
    (let [argv (vec args)]
        (cond
            (< (count argv) 1)
                (println "You didn't specify a type: 'recur', 'loop', or 'nostack'")
            (= (argv 0) "recur")
                (follow-keyword :main bigtest)
            (= (argv 0) "loop")
                (eval-grammar bigtest)
            (= (argv 0) "nostack")
                (no-stack-eval bigtest))))