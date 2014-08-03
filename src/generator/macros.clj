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

(defn- test-keywords [pairs]
  (let [seen (atom #{ })]
    (doseq [[key value] pairs]
      (if (contains? @seen key)
        (throw (Exception. (str "Duplicate key error: " key)))
        (swap! seen conj key)))))

(def tuples #(->> % (partition 2) (map vec)))

(defmacro deftemplate [name & args]
  (let [needs-main (odd? (count args))
        new-args (if needs-main (cons :main args) args)
        pairs (tuples new-args)]
    (test-keywords pairs)
    (let [template-body (into { } pairs)]
      `(def ~name ~template-body))))