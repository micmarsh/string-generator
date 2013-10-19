(ns generator.utils
    (:use clojure.core.typed))


(ann no-space? [AnyInteger String (NonEmptyVec String) -> Boolean])
(defn- no-space? [i, item, strings]
    (or
        (= i 0)
        (= (last (get strings (- i 1))) \newline)
        (contains? #{"," "." "?" "!" "-"} item)
        (= (first item) \`)
        ))

(ann remove-backtick [String -> String])
(defn- remove-backtick [item]
    (if (= (first item) \`)
        (apply str (rest item))
    ;else
        item))

(ann prepend-spaces [ (NonEmptyVec String) -> (Seqable String)])
(defn- prepend-spaces [strings]
        (map-indexed
            (fn> [i :- AnyInteger
                 item :- String]
                (if (no-space? i, item, strings)
                    (remove-backtick item)
                ;else
                    (str " " item)))
        strings))

(ann clojure.string/split [String java.util.regex.Pattern -> (NonEmptyVec String)])
(ann append-and-split [(Seqable String) -> (NonEmptyVec String)])
(defn- append-and-split [strings]
    (clojure.string/split
        (apply str (map #(str % " ") strings))
        #" +"))

(ann sanitize-spaces [(Seqable String) -> String])
(defn sanitize-spaces [strings]
    (apply str
        (prepend-spaces
            (append-and-split strings))))
