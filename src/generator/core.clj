(ns generator.core
    (:use [generator.parser :only [eval-grammar]]
          [generator.templates :only [political-speech]]))


(defn -main [& args]
    (let [[timed?] args
          main-expr `(println (eval-grammar political-speech) )]
        (if timed?
            (time (eval main-expr))
        (eval main-expr))))
