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
;da rules: given a :key:
; map [vec...] (fn[item]( (if list? item) (rand-nth item) ()  ))


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

(defn -main [& args]
    (follow-keyword :main example))