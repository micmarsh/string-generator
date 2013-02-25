(ns generator.utils)



(defn- no-space? [i, item, strings]
    (or
        (= i 0)
        (= (last (strings (- i 1))) \newline)
        (contains? #{"," "." "?" "!"} item)
        (= (first item) \`)
        ))

(defn- remove-backtick [item]
    (if (= (first item) \`)
        (apply str (rest item))
    ;else
        item))

(defn- prepend-spaces [strings]
        (map-indexed
            (fn[i item]
                (if (no-space? i, item, strings)
                    (remove-backtick item)
                ;else
                    (str " " item)))
        strings))

(defn- append-and-split [strings]
    (clojure.string/split
        (apply str (map #(str % " ") strings)) #" +"))

(defn sanitize-spaces [strings]
    (apply str
        (prepend-spaces
            (append-and-split strings))))