(defproject generator "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [marshmacros "0.1.1"]
                 [org.clojure/core.typed "0.2.13"]]
  :javac-target "1.7"
  :java-target "1.7"
  :main generator.core)
