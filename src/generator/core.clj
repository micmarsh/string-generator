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
        :themes [ (list :sad :romantic :funny) ]
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
                    :sad [ " The funeral will be at " (list "4" "6") "`pm on "
                            (list "Tues" "Wednes") "`day at the home"
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

;default theme combos: [ (list :mainstream :radical) (list :intellectual :frothing)]
(def politcal-speech {
        :themes [ (list :mainstream :radical) (list :intellectual :frothing)]
        :main [:introduction :problem  :take-action :closing]
            :introduction [:address :follow-up :loaded-question]
                :address ["My " {:intellectual "trusted" :frothing "outraged" :else "fellow"}
                            {:intellectual "colleagues" :frothing "comrades"
                            :radical "brothers and sisters in solidarity"
                            :else "Americans"} ", " ]
                :follow-up [ (list "2" "6") (list "years" "weeks") "ago today," :enemy :did-bad-thing "."]
                    :enemy {
                        :mainstream (list "my opponent" :other-party)
                        :radical "our capitalist oppressors"
                        :else :other-party
                    }
                        :other-party "the other party"
                    :did-bad-thing [:cause "that " :effect ]
                        :cause {
                            :mainstream (list "signed a bill" "won a primary election" )
                            :radical (list "issued an edict" "hatched a plot")
                            :else "did something"
                        }
                        :effect {
                            :intellectual "changed the course of history"
                            :frothing ["destroyed" (list "your wages" "your future")]
                            :else "changed everything"
                        }
                :loaded-question [ (list ["Did you know " :upsetting-fact]
                                    ["How long will " :be-passive]) "?"]
                    :upsetting-fact {
                        :mainstream {
                            :intellectual ["this nation "
                                        "exports fewer horse-drawn carraiges now than"
                                        (list "at any point in history" "in 1969")]
                            :frothing ["the " (list "greedy" "fat-cat")" 1%"
                                        (list "has more money than you"
                                            "made record profits last year")]
                        }
                        :radical ["our" :unfortunate-circumstance
                                    "is a direct result of" :evil-institution]
                        :else "this"

                    }
                        :unfortunate-circumstance (list [(list "crippling" "degrading")
                                                        (list "unemployment" "oppression")]
                                                    "having to expend effort to survive")
                        :evil-institution (list "the Patriarchy" "modern civilization" "rich people")
                    :be-passive {
                        :mainstream "they continue to vote against the middle class"
                        :intellectual "they deny the neccesity of unfettered Keynesianism"
                        :radical (list [ "you continue to be robbed of your"
                                    (list "freedoms" "dignity" "marijuana")]
                                    "you stand by in blissful ignorance with the rest of the sheeple")
                        :else "they do nothing"
                    }
            :problem [""]
            :take-action [""]
            :closing [""]


    })


(defn -main [& args]
   (println (eval-grammar politcal-speech) ))