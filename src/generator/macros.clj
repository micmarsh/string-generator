(ns generator.macros)

(defmacro deftemplate [name & args]
    (let [needs-main (odd? (count args))
          new-args (if needs-main (cons :main args) args)
          template-map (make-template-map new-args)]
        `(def ~name ~template-map)))