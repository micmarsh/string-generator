(ns generator.core
    (:use [generator.parser :only [eval-grammar]]
          [generator.templates :only [political-speech]]
          clojure.core.typed))

; (ann clojure.core/eval [Any -> Any])

; (ann -main [Any * -> Any])
(defn -main [& args]
    (let [[timed?] args
          main-expr `(println (eval-grammar political-speech) )]
        ; (if timed?
        ;     (time (eval main-expr))
        (eval main-expr)));)
