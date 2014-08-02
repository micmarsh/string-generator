(ns generator.core-test
  (:use clojure.test
        [generator.example :only (political-speech)]
        [generator.parser :only (eval-grammar)]))

(deftest timing
  (let [theme (rand-nth [:mainstream :radical])
        result (time (eval-grammar political-speech [theme]))]
    (println "Using theme" (name theme))
    #_(println "Result" result)))
