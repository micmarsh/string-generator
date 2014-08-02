(ns generator.templates)

; reserved keywords: :main, :themes
; reserved themes: :else, :default

(defn- template? [object]
    (cond
    (symbol? object)
        (let [evaled (eval object)]
            (and
                (map? evaled)
                (:main evaled)))
    (map? object)
        (:main object)
    :else
        false))

(defn- assoc-template [final-map [key value]]
    (if (template? value)
        (let [to-map (eval value)
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
        `(def ~name ~template-map)))

(def example {
        :main [:greeting  " " :second-phrase]
            :greeting [:greeting-word :middle :world]
                :greeting-word (hash-set "Hello" "Goodbye")
                :middle (hash-set " " ", ")
                :world [ :word :punc ]
                    :word (hash-set "World" "Mother" "Brother" "earthlings")
                    :punc (hash-set "!" "." "?")
            :second-phrase [:emotion " to meet you, " :end-phrase :punc]
                :emotion (hash-set "Pleased" "Inconvenienced" "Aroused")
                :end-phrase (hash-set "hope you guess my name"
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
    :romantic-feeling (hash-set "tingle with excitement"
                            "rejoice in")
    :funny-entity (hash-set "The Ministry of Silly walks"
                    "your mother")
)
(deftemplate paragraph-statement [" that " :statement-map "."]
    :statement-map {
        :romantic "my love for you grows with every passing day"
        :sad ["your " (hash-set "goldfish" "dog") " has died"]
        :funny (hash-set [(str "you tried to microwave a ding-dong while "
                            "it was still in the wrapper " )
                        (hash-set "twice" "thrice")]
                     (str "you prematurely shot your wad on what was supposed "
                        "to be a dry run, and now you have a bit of a mess"
                        " on your hands"))
    }
)
(deftemplate paragraph-closing
     {
        :romantic (hash-set " I miss you and anticipate seeing you soon!"
                        " Take care, love, until my return!")
        :sad [ " The funeral will be at " (hash-set "4" "6") "`pm on "
                (hash-set "Tues" "Wednes") "`day at the home"
                " on " (hash-set "fleet" "maple") " street."]
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
    :themes [ (hash-set :sad :romantic :funny) ]
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
    :follow-up [ (hash-set "2" "6") (hash-set "years" "weeks") "ago today," :enemy :did-bad-thing "."]
        :enemy {
            :mainstream (hash-set "my opponent" :other-party)
            :radical (hash-set "the 1%" "our capitahash-set oppressors")
            :else :other-party
        }
            :other-party "the other party"
        :did-bad-thing [:cause "that " :effect ]
            :cause {
                :mainstream (hash-set "signed a bill" "won a primary election" )
                :radical (hash-set "issued an edict" "hatched a plot")
                :else "did something"
            }
            :effect {
                :mainstream "changed the face of our country"
                :radical ["destroyed" (hash-set "your wages" "your future")]
                :else "changed everything"
            }
    :loaded-question [ (hash-set ["Did you know " :upsetting-fact]
                        ["How long will " :be-passive]) "?"]
        :upsetting-fact {
            :mainstream ["this nation "
                            "exports fewer horse-drawn carraiges now than"
                            (hash-set "at any point in history" "in 1969")]

            :radical ["our" :unfortunate-circumstance
                        "is a direct result of" :evil-institution]
            :else "this"

        }
            :unfortunate-circumstance (hash-set [(hash-set "crippling" "degrading")
                                            (hash-set "unemployment" "oppression")]
                                        "having to expend effort to survive")
            :evil-institution (hash-set "the Patriarchy" "modern civilization" "the 1%")
        :be-passive {
            :mainstream "they continue to vote against the middle class"
            :radical (hash-set [ "you continue to be robbed of your"
                        (hash-set "freedoms" "dignity" "marijuana")]
                        "you stand by in blissful ignorance with the rest of the sheeple")
            :else "they do nothing"
        }
)

(deftemplate speech-closing [""])

(deftemplate political-speech
    (map #(identity ["\t" %])
        [:introduction "\n" :problem  "\n" :take-action "\n" :closing])
        :introduction speech-intro
        :problem [:lead-in "." :blame-game "." :indignant-statement
                {:mainstream "." :radical "!"}]
            :lead-in ["As " (hash-set "you" "we all") "know, " :new-fact]
                :new-fact {
                    ;if this could be localized, that would be great
                    :mainstream ["times are tough for"
                                (hash-set "all Americans" "middle-class folks")]
                    :radical ["the corporations have"
                                (hash-set "defiled" "repressed")
                                (hash-set "mother nature" "the workers") "for too long"]
                }
            :blame-game {
                :mainstream ["In fact, the economic problems caused by"
                    (hash-set :foreigners :bankers :politicians)
                    "still continue to make life difficult today"]

                :radical ["While you have struggled "
                    (hash-set "to make use of your sociology degrees"
                        "under the heal of your employers"
                        "for dignity in a capitahash-set society") ","
                    (hash-set :bankers :politicians)
                    " have grown fat and rich"
                ]
            }
                :foreigners [(hash-set "Chinese" "Mexican" "Arab") "immigrants"]
                :bankers [{:mainstream "irresponsible" :radical "greedy"} "bankers"]
                :politicians [(hash-set "Republican" "Democratic") "politicians"]
            :indignant-statement {
                :mainstream [ "It's time for real changes to be made,"
                            "changes that will help " :who :do-something]
                :radical ["The time has come to " (hash-set "rise up" "mobilize") "and"
                        :do-something-radical ]
            }
                :who ["the" :mainstream-heroes
                     "of this country"]
                    :mainstream-heroes [(hash-set "hard-working" "honest")
                                        (hash-set "families" "workers" "teachers")]
                :do-something (hash-set "make a decent living" "restore our global competitiveness")
                :do-something-radical ["take back our" (hash-set "factories" "cities" "society")]

        :take-action [:acknowledge-now :offer-hope :stir-the-pot]
            :acknowledge-now [(hash-set "Now" "Currently") ", we all "
                            (hash-set "know" "are aware") "of the"
                            {
                                :mainstream [(hash-set "sacrifices" "hard choices")
                                "that have to be made to keep this country together"]
                                :radical (hash-set ["crimes of " :villains]
                                                ["struggles of " :heroes])
                            }
            "."]
                        :villains ["the" (hash-set "bourgeois" :bankers)
                                "and their all-consuming decadence"]
                        :heroes ["the "(hash-set "wage" "debt") "`-slave " (hash-set "proletariat" "students")
                                "and their hopeless victimhood"]
            :offer-hope {
                :mainstream ["However, we have faced "
                            (hash-set "hard" "difficult") (hash-set "times" "challenges")
                            " before, and have always" :perservered "."]
                :radical ["But there is unrest among the people, "
                "hope in the" (hash-set "universities" "factories" "unions") "!"
                "The " (hash-set "intellectuals" "senators" "workers") "are " :getting-info "!"]
            }
                            :perservered [
                            (hash-set "pulled through" "survived")
                            "due to " (hash-set ["massive expansions of your favorite"
                                            "social programs"]
                                            "our unshakeable national unity"
                                            "the guiding hand of a strong leader")
                            ]
                            :getting-info [
                                (hash-set "waking up to" "studying")
                                "the " (hash-set "philosophy" "lifestyle") "of"
                                (hash-set "anarcho" ["post" (hash-set "`modern" "`-structural")])
                                (hash-set "chomsky" "dada" "social") "`ism"
                            ]
            :stir-the-pot [:lead-in-closing :more-upsetting-facts ]
                :lead-in-closing [{:mainstream "Citizens" :radical "Comrades"}
                        ", the time " (hash-set "is now" "has never been better"
                            "is quickly approaching") "for" :useless-action "!"]
                        :useless-action {
                            :mainstream (hash-set "vigorous letter-writing campaigns"
                                            "getting out the vote"
                                            "doing your part")
                            :radical (hash-set "organizing a general strike"
                                            "spreading the ideals of the revolution"
                                            "staging a protest")
                        }
                :more-upsetting-facts ["While " :enemy {:mainstream "gets votes and supporters"
                            :radical :bad-radical-actions} ", the situation gets worse for "
                            {:mainstream :mainstream-heroes :radical :heroes}
                            "every day."]
                        :bad-radical-actions (hash-set "exploits your labor"
                                                "saddles you with debt"
                                                "earns more money than you")
        :closing speech-closing
)
