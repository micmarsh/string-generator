(ns generator.core
    (:use [generator.parser :only [eval-grammar]]
          [generator.templates :only [political-speech]]))


(def theme [(rand-nth '(:mainstream :radical))])

(defn -main [& args]
  (let [[timed?] args
        main-expr `(println (eval-grammar political-speech theme) )]
    (if timed?
      (time (eval main-expr))
      (eval main-expr ))))
