(ns generator.macros)

(defn- template? [object]
  (cond
    (symbol? object)
      (let [evaled (eval object)]
          (and
            (map? evaled)
            (:main evaled)))
    (map? object)
      (:main object)
    :else
      false))

(defn- assoc-template [final-map [key value]]
  (if (template? value)
    (let [to-map (eval value)
          main (:main to-map)
          value-without-main (dissoc to-map :main)
          map-with-main (assoc final-map key main)]
      (merge map-with-main value-without-main))
    (assoc final-map key value)))

(defn- make-template-map [pairs]
  (loop [final-map { }
         pairs (partition 2 pairs)]
  (if (= 0 (count pairs))
    final-map
    (let [pair (first pairs)]
      (recur (assoc-template final-map pair) (rest pairs))))))

(defmacro deftemplate [name & args]
  (let [needs-main (odd? (count args))
        new-args (if needs-main (cons :main args) args)
        template-map (make-template-map new-args)]
    `(def ~name ~template-map)))