(ns generator.templates)

; reserved keywords: :main, :themes
; reserved themes: :else, :default

(def symbol-table (atom { }))

(defn- template? [object]
    (cond
    (symbol? object)
        (let [evaled (@symbol-table object)]
            (and
                (map? evaled)
                (:main evaled)))
    (map? object)
        (:main object)
    :else
        false))

(defn- assoc-template [final-map [key value]]
    (if (template? value)
        (let [to-map (@symbol-table value)
              main (:main to-map)
              value-without-main (dissoc to-map :main)
              map-with-main (assoc final-map key main)]
        (merge map-with-main value-without-main))
    ;else
        (assoc final-map key value)))

(defn- make-template-map [pairs]
    (loop [ final-map { }
            pairs (partition 2 pairs)]
    (if (= 0 (count pairs))
        final-map
    ;else
        (let [pair (first pairs)]
            (recur (assoc-template final-map pair) (rest pairs))))))

(defmacro deftemplate [name & args]
    (let [needs-main (odd? (count args))
          new-args (if needs-main (cons :main args) args)
          template-map (make-template-map new-args)]
        (swap! symbol-table #(assoc % name template-map))
        `(def ~name ~template-map)))

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


(deftemplate paragraph-opener
    {
    :sad "I regret to inform you"
    :romantic :romantic-opener
    :funny ["It is the opinion of " :funny-entity]
    }
    :romantic-opener [ "I " :romantic-feeling " writing you" ]
    :romantic-feeling (list "tingle with excitement"
                            "rejoice in")
    :funny-entity (list "The Ministry of Silly walks"
                    "your mother")
)
(deftemplate paragraph-statement [" that " :statement-map "."]
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
)
(deftemplate paragraph-closing
     {
        :romantic (list " I miss you and anticipate seeing you soon!"
                        " Take care, love, until my return!")
        :sad [ " The funeral will be at " (list "4" "6") "`pm on "
                (list "Tues" "Wednes") "`day at the home"
                " on " (list "fleet" "maple") " street."]
        :funny " Clearly, you've made a huge mistake."
    }
)

(deftemplate paragraph
    [:opener " " :statement " " :closing]
    :opener paragraph-opener
    :statement paragraph-statement
    :closing paragraph-closing
)

;possible themes: funny, sad, romantic
(deftemplate letter
    [:salutation "\n\n" :paragraph "\n\n" :signature]
    :themes [ (list :sad :romantic :funny) ]
        :salutation [{:romantic "My Darling" :else "To Whom It May Concern"} ","]
        :paragraph paragraph
        :signature ["With " :with-thing ", " "\n" "Michael Marsh"]
            :with-thing {
                :romantic "Love"
                :funny "Lulz"
                :sad "Deepest Regrets"
            }
)

(deftemplate speech-intro [:address :follow-up :loaded-question]
:address ["My " {:mainstream "trusted" :else "fellow"}
            {:mainstream "citizens"
            :radical "brothers and sisters in solidarity"
            :else "Americans"} ", " ]
:follow-up [ (list "2" "6") (list "years" "weeks") "ago today," :enemy :did-bad-thing "."]
    :enemy {
        :mainstream (list "my opponent" :other-party)
        :radical (list "the 1%" "our capitalist oppressors")
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
            :mainstream "changed the face of our country"
            :radical ["destroyed" (list "your wages" "your future")]
            :else "changed everything"
        }
:loaded-question [ (list ["Did you know " :upsetting-fact]
                    ["How long will " :be-passive]) "?"]
    :upsetting-fact {
        :mainstream ["this nation "
                        "exports fewer horse-drawn carraiges now than"
                        (list "at any point in history" "in 1969")]

        :radical ["our" :unfortunate-circumstance
                    "is a direct result of" :evil-institution]
        :else "this"

    }
        :unfortunate-circumstance (list [(list "crippling" "degrading")
                                        (list "unemployment" "oppression")]
                                    "having to expend effort to survive")
        :evil-institution (list "the Patriarchy" "modern civilization" "the 1%")
    :be-passive {
        :mainstream "they continue to vote against the middle class"
        :radical (list [ "you continue to be robbed of your"
                    (list "freedoms" "dignity" "marijuana")]
                    "you stand by in blissful ignorance with the rest of the sheeple")
        :else "they do nothing"
    }
)

(deftemplate political-speech
    (map #(identity ["\t" %])
        [:introduction "\n" :problem  "\n" :take-action "\n" :closing])
    :themes [ (list :mainstream :radical )]
        :introduction speech-intro
        :problem [:lead-in "." :blame-game "." :indignant-statement
                {:mainstream "." :radical "!"}]
            :lead-in ["As " (list "you" "we all") "know, " :new-fact]
                :new-fact {
                    ;if this could be localized, that would be great
                    :mainstream ["times are tough for"
                                (list "all Americans" "middle-class folks")]
                    :radical ["the corporations have"
                                (list "defiled" "repressed")
                                (list "mother nature" "the workers") "for too long"]
                }
            :blame-game {
                :mainstream ["In fact, the economic problems caused by"
                    (list :foreigners :bankers :politicians)
                    "still continue to make life difficult today"]

                :radical ["While you have struggled "
                    (list "to make use of your sociology degrees"
                        "under the heal of your employers"
                        "for dignity in a capitalist society") ","
                    (list :bankers :politicians)
                    " have grown fat and rich"
                ]
            }
                :foreigners [(list "Chinese" "Mexican" "Arab") "immigrants"]
                :bankers [{:mainstream "irresponsible" :radical "greedy"} "bankers"]
                :politicians [(list "Republican" "Democratic") "politicians"]
            :indignant-statement {
                :mainstream [ "It's time for real changes to be made,"
                            "changes that will help " :who :do-something]
                :radical ["The time has come to " (list "rise up" "mobilize") "and"
                        :do-something-radical ]
            }
                :who ["the" :mainstream-heroes
                     "of this country"]
                    :mainstream-heroes [(list "hard-working" "honest")
                                        (list "families" "workers" "teachers")]
                :do-something (list "make a decent living" "restore our global competitiveness")
                :do-something-radical ["take back our" (list "factories" "cities" "society")]

        :take-action [:acknowledge-now :offer-hope :stir-the-pot]
            :acknowledge-now [(list "Now" "Currently") ", we all "
                            (list "know" "are aware") "of the"
                            {
                                :mainstream [(list "sacrifices" "hard choices")
                                "that have to be made to keep this country together"]
                                :radical (list ["crimes of " :villains]
                                                ["struggles of " :heroes])
                            }
            "."]
                        :villains ["the" (list "bourgeois" :bankers)
                                "and their all-consuming decadence"]
                        :heroes ["the "(list "wage" "debt") "`-slave " (list "proletariat" "students")
                                "and their hopeless victimhood"]
            :offer-hope {
                :mainstream ["However, we have faced "
                            (list "hard" "difficult") (list "times" "challenges")
                            " before, and have always" :perservered "."]
                :radical ["But there is unrest among the people, "
                "hope in the" (list "universities" "factories" "unions") "!"
                "The " (list "intellectuals" "senators" "workers") "are " :getting-info "!"]
            }
                            :perservered [
                            (list "pulled through" "survived")
                            "due to " (list ["massive expansions of your favorite"
                                            "social programs"]
                                            "our unshakeable national unity"
                                            "the guiding hand of a strong leader")
                            ]
                            :getting-info [
                                (list "waking up to" "studying")
                                "the " (list "philosophy" "lifestyle") "of"
                                (list "anarcho" ["post" (list "`modern" "`-structural")])
                                (list "chomsky" "dada" "social") "`ism"
                            ]
            :stir-the-pot [:lead-in-closing :more-upsetting-facts :maybe-an-action]
                :lead-in-closing [{:mainstream "Citizens" :radical "Comrades"}
                        ", the time " (list "is now" "has never been better"
                            "is quickly approaching") "for" :useless-action "!"]
                        :useless-action {
                            :mainstream (list "vigorous letter-writing campaigns"
                                            "getting out the vote"
                                            "doing your part")
                            :radical (list "organizing a general strike"
                                            "spreading the ideals of the revolution"
                                            "staging a protest")
                        }
                :more-upsetting-facts ["While " :enemy {:mainstream "gets votes and supporters"
                            :radical :bad-radical-actions} ", the situation gets worse for "
                            {:mainstream :mainstream-heroes :radical :heroes}
                            "every day."]
                        :bad-radical-actions (list "exploits your labor"
                                                "saddles you with debt"
                                                "earns more money than you")
                :maybe-an-action ""
        :closing [""]
)

