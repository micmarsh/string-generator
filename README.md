# generator

Define a "grammar" (disclaimer: I know absolutely nothing about linguistics or NLP) using Clojure maps, generate some words from it.

## Usage

```bash
lein run [loop | recur | nostack]
#'recur' and 'nostack' are likely to be phased out soon
```

```clojure
(def example {:main [:greeting  " " :second-phrase]
                ;":main" defines the entry point of the parser
                ;keywords currently map to vectors or lists
                :greeting [:greeting-word :middle :world]
                    ;vectors define a sequence of successive strings
                    :greeting-word (list "Hello" "Goodbye")
                    ;a single word is randomly chosen from a list
                    :middle (list " " ", ")
                    :world [ :word :punc ]
                        :word (list "World" "Mother" "Brother" "earthlings")
                        :punc (list "!" "." "?")
                :second-phrase [:emotion " to meet you, " :end-phrase :punc]
                    ;strings are ignored until every keyword is evaulated to a string
                    :emotion (list "Pleased" "Inconvenienced" "Aroused")
                    :end-phrase (list "hope you guess my name"
                                      "I need to go sleep now")
                        })
```
Some Examples of the above output:
"Goodbye, Brother? Inconvenienced to meet you, I need to go sleep now."
"Goodbye, World! Pleased to meet you, hope you guess my name!"
"Hello, World. Aroused to meet you, I need to go sleep now!"

## Todo

### Short-term plans:
    -Allow list (and map, see below) literals in vectors
    -Themes: define them somehow (most likely with :themes), then allow maps alongside vectors and list, where the word chosen is looked up according to defined themes

### Long-term plans:
    -Embed vectors and maps into lists (this will work automatically for maps, may need to do extra stuff for vectors)
    -Dynamically (randomly) choosing themes as lists are evaluated


## License

Copyright © 2013 Michael Marsh

Distributed under the Eclipse Public License, the same as Clojure.
