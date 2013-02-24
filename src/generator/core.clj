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

;IDEA! TODO: Stick all words into a "words" function that
;automagically sticks spaces where they need to be
;(or appends a space to every word passed in, then being passed in
; separately. This needs figuring out)

;possible themes: funny, sad, romantic
(def letter {
        :themes [:sad :funny]
        :main [:salutation "\n\n" :paragraph "\n\n" :signature]
            :salutation[{:romantic "My Darling" :else "To Whom It May Concern"} ","]
            :paragraph [:opener " " :statement " " :closing]
                :opener {
                    :sad "I regret to inform you"
                    :romantic :romantic-opener
                    :funny ["It is the opinion of " :funny-entity]
                    }
                    :romantic-opener [ "I " :romantic-feeling " writing you" ]
                        :romantic-feeling (list "tingle with excitement"
                                                "rejoice in")
                    :funny-entity (list "The Ministry of Silly walks"
                                        "your mother")
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
                :closing {
                    :romantic (list " I miss you and anticipate seeing you soon!"
                                    " Take care, love, until my return!")
                    :sad [ " The funeral will be at " (list "4" "6") "pm on "
                            (list "Tues" "Wednes") "day at the home"
                            " on " (list "fleet" "maple") " street."]
                    :funny " Clearly, you've made a huge mistake."
                }
            :signature ["With " :with-thing ", " "\n" "Michael Marsh"]
                :with-thing {
                    :romantic "Love"
                    :funny "Lulz"
                    :sad "Deepest Regrets"
                }

    })


(defn -main [& args]
   (println (eval-grammar letter)))