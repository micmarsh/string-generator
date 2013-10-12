(ns generator.core
    (:use [generator.parser :only [eval-grammar]]
          [generator.templates :only [letter]]))


(defn -main [& args]
    (let [[timed?] args
          main-expr  `(println (eval-grammar letter) )]
        (if timed?
            (time (eval main-expr))
            (eval main-expr))))
