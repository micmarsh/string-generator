(ns generator.utils)

(defn- prepend-spaces [strings]
    (let [puncuation #{"," "." "?" "!"}]
        (map-indexed
            (fn[i item]
                (if (or
                        (= i 0)
                        (= (last (strings (- i 1))) \newline)
                        (contains? puncuation item))
                    item
                    (str " " item)))
            strings)))

(defn- append-and-split [strings]
    (clojure.string/split
        (apply str (map #(str % " ") strings)) #" +"))

(defn sanitize-spaces [strings]
    (apply str
        (prepend-spaces
            (append-and-split strings))))