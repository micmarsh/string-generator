(ns generator.core-test
  (:use clojure.test
        generator.core
        generator.parser))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(deftest themes-1
    (testing "themes!"
        (is (=
            (eval-grammar
                {
                :themes [:foo]
                :main [{:foo "bar" :not-foo "baz"}]
                })
            "bar"))))

(deftest themes-2
    (testing "themes!"
        (is (=
            (eval-grammar
                {
                :themes [:foo :bar]
                :main [{:foo "bar" :not-foo "baz"} " crawl " {:bar "tonight" :not-bar "zex"}]
                })
            "bar crawl tonight"))))