(ns generator.example
  (:use [generator.macros :only (deftemplate)]))

(deftemplate speech-intro [:address :follow-up :loaded-question]
    :address ["My " {:mainstream "trusted" :else "fellow"}
                {:mainstream "citizens"
                :radical "brothers and sisters in solidarity"}
                 ", " ]
    :follow-up [ (hash-set "2" "6") (hash-set "years" "weeks") "ago today," :enemy :did-bad-thing "."]
        :enemy {
            :mainstream (hash-set "my opponent" :other-party)
            :radical (hash-set "the 1%" "our capitalist oppressors")
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

(deftemplate speech-closing
    [:haters :gonna-hate :historical-validity :inspiration]
     :haters
         {:mainstream [ :lead-in-haters
                #{"some folks" "they" } ]
         :radical #{"The happily employed" "Greedy bankers" "Corporate CEOs"}}
         :lead-in-haters #{"My friends, " "Citizens, "}
     :gonna-hate [:speak-verb "we don't" :do-enough "." :finish-thought "."]
         :speak-verb ["may" #{"think" "say" "proclaim"} "that"]
         :do-enough  ["have" {
             :mainstream ["enough "  :mainstream-commodities "to" :good-program] ;TODO empty sets should be an exception! along with nil
             :radical [ :radical-commodities "to" :radical-program]
         }]
            :mainstream-commodities #{"bipartisan support" "votes" "of a budget"}
            :good-program #{"continue defending our freedom"
                            "leave no child behind"
                            "put folks back to work"}
            :radical-commodities  #{["the" #{"intellectual" "widespread"} "support necessary "]
                                  "enough money"}
            :radical-program #{["cast off the oppression of" :oppressive-institution]
                                ["inspire revolution in the "
                                #{"universities" "factories" "cities"}]
                            }
                :oppressive-institution  #{"the myths of capitalist economics   "
                                        "post-agrarian society"
                                        "planned obsolesence"}
         :finish-thought [#{"However" "Unfortunately"}
                            ", they have been"
                            #{"mislead" "blinded"}
                            "by their"
                                {:mainstream #{"special-interest lobbyists"
                                            "close-mindedness" "biases"}
                                 :radical #{"greed" "ignorance" "bourgeois upbringings"}}]
     :historical-validity {:mainstream
                            ["Did" :president :concede  "when" :opposed-him "? No!" ]
                         :radical
                            ["Just as" :victims :triumphed ", so shall we" :also-triumph "!"]}
        :president #{"George Washington" "Lincoln" "Teddy Roosevelt" "FDR"}
        :concede #{"give up" "step down" "throw in the towel"}
        :opposed-him [:who? :did-what]
            :who? #{["his" #{"political opponents" "dissenters"}]
                    "disloyal citizens"}
            :did-what #{"attempted to tarnish his legacy"
                        "opposed all of his essential reforms"
                        "refused to cooperate with his grand vision"}
        :victims ["the"
                    #{"students" "workers" "underclass" "people"}
                  "of revolutions past"]

        :triumphed [#{"heroically" "valiantly"}
                    #{"organized their strikes"
                      "were fired from their jobs"
                      "formed unions"
                      "did a lot of drugs"}]

        :also-triumph #{"inspire a brand new revolution"
                        ["move society beyond the oppression of"
                        #{"money" "patriarchy" "big business"}]
                        "hand out some socialist fliers"}
     :inspiration {
        :mainstream ["Let's " :useless-inspirational "!" ]
        :radical [""]
     }
        :useless-inspirational [:useless-inspire-action "and" :get-result]
            :useless-inspire-action #{"get to the ballot box"
                                "get to work"
                                "pick up our phones"}
            :get-result #{"create some real change in Washington"
                          "move our nation in the right direction"
                          "secure America's future"}
)

(deftemplate political-speech
    (mapv (partial vector \tab)
        [:introduction "\n" :problem  "\n" :take-action "\n" :closing])
        :introduction speech-intro
        :problem [:lead-in "." :blame-game "." :indignant-statement
                {:mainstream "." :radical "!"}]
            :lead-in [ {:mainstream #{"These days, " :as-know}
                        :radical :as-know} :new-fact]
                :new-fact {
                    ;if this could be localized, that would be great
                    :mainstream ["times are tough for"
                                (hash-set "all Americans" "middle-class folks")]
                    :radical ["the corporations have"
                                (hash-set "defiled" "repressed")
                                (hash-set "mother nature" "the workers") "for too long"]
                }
                :as-know ["As " (hash-set "you" "we all") "know, "]
            :blame-game {
                :mainstream ["In fact, the economic problems caused by"
                    (hash-set :foreigners :bankers :politicians)
                    "still continue to make life difficult today"]

                :radical ["While you have struggled "
                    (hash-set "to make use of your sociology degrees"
                        "under the heel of your employers"
                        "for dignity in a capitalist society") ","
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