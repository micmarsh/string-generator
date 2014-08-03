(ns generator.macros)

(defn- test-keywords [pairs]
  (let [seen (atom #{ })]
    (doseq [[key value] pairs]
      (if (contains? @seen key)
        (throw (Exception. (str "Duplicate key error: " key)))
        (swap! seen conj key)))))

(def ^:private tuples
  #(->> % (partition 2) (map vec)))

(defmacro deftemplate [name & args]
  (let [needs-main (odd? (count args))
        new-args (if needs-main (cons :main args) args)
        pairs (tuples new-args)]
    (test-keywords pairs)
    (let [template-body (into { } pairs)]
      `(def ~name ~template-body))))