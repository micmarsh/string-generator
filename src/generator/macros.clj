(ns generator.macros
  (:use [generator.parser :only (eval-main)]))

(def finished?
  (let [times (atom 0)]
    (>= 1000 (swap! times inc))))

; (defn- eval-item! [new-sequence _ item]
;   (cond
;     (set? item)
;       (fmap )))

; (defn eval-template [template]
;   (eval-main
;     ))

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