(ns generator.core
    (:use [generator.parser :only [eval-grammar]]))

; reserved keywords: :main, :themes
; reserved themes: :else, :default

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
        :themes [:romantic :funny :questioning]
        :main [ :start " " :end ]
            :start {:funny "hahahaha" :sad "boohoohoo" :romantic "lovelovelove"}
            :end {:funny ["lulz lulz lulz" :punc] :sad ":-(" :romantic "I love you!"}
                :punc {:questioning "?" :else "." }
    })


;possible themes: funny, sad, romantic
(def letter {
        :themes [:funny]
        :main [:salutation "\n\n" :paragraph "\n\n" :signature]
            :salutation[{:romantic "My Darling" :else "To Whom It May Concern"} ","]
            :paragraph [:opener " " :statement " " :closing]
                :opener {
                    :sad "I regret to inform you"
                    :romantic :romantic-opener
                    :funny ["It is the opinion of" :funny-entity]
                    }
                    :romantic-opener [ "I " :romantic-feeling " writing you" ]
                        :romantic-feeling (list "tingle with excitement"
                                                "rejoice")
                    :funny-entity (list "The Ministry of Silly walks"
                                        "Your mother")
                :statement [" that " :statement-map "."]
                    :statement-map {
                                    :romantic "my love for you grows with every passing day"
                                    :sad ["your " (list "goldfish" "dog") " has died"]
                                    :funny (list [(str "you tried to microwave a ding-dong while "
                                                        "it was still in the wrapper " )
                                                    (list "twice" "thrice")]
                                                 (str "you prematurely shot your wad on what was supposed "
                                                    "to be a dry run, and now you have a bit of a mess"
                                                    " on your hands"))
                    }
                :closing "..."
            :signature "..."

    })


(defn -main [& args]
    (eval-grammar letter))