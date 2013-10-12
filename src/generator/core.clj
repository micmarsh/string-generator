(ns generator.core
    (:use [generator.parser :only [eval-grammar]]
          [generator.templates :only [example]]))


(defn -main [& args]
    (let [[timed?] args
          main-expr  `(println (eval-grammar example) )]
        (if timed?
            (time (eval main-expr))
            (eval main-expr))))
