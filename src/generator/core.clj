(ns generator.core
    (:use [generator.parser :only [eval-grammar]]))

; reserved keywords: :main, :themes

(def example {
            :main [:greeting  " " :second-phrase]
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

(def theme-test {
        :themes [:funny :romantic]
        :main [{:funny "hahahaha" :sad "boohoohoo"} " " :end]
            :end {}
    })


(defn -main [& args]
    (eval-grammar example))