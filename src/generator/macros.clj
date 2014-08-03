(ns generator.macros
  (:use [clojure.algo.generic.functor :only (fmap)]))

(defn fmap? [item]
  (or
    (set? item)
    (vector? item)
    (map? item)))

(defn eval-template [template item]
  (cond
    (fmap? item)
      (fmap
        (partial eval-template template)
        item)
    (keyword? item)
      (eval-template template (item template))
    (list? item)
      (eval-template template (eval item))
    :else
      item))

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
    (let [{main :main :as template} (into { } pairs)
          evaled-main (eval-template template main)
          template-body (assoc template :main evaled-main)]
      `(def ~name ~template-body))))