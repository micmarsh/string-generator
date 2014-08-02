(defproject generator "0.1.2"
  :description "Generate strings based on templates. Might someday be helpful in conjunction with nonsense generators"
  :url "https://github.com/micmarsh/string-generator/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [marshmacros "0.1.1"]]
  :main generator.core

  :profiles {
    :dev {
        :dependencies [[org.clojure/clojurescript "0.0-2268"]]

        :plugins [[com.keminglabs/cljx "0.4.0"]
                  [lein-cljsbuild "1.0.3"]]

        :hooks [cljx.hooks]

        :cljsbuild {
          :builds [{
              :source-paths ["target/cljs"]
              :compiler {
                :output-to "target/main.js"
                :optimizations :whitespace
                :pretty-print true}}
        ]}

        :cljx {
          :builds [
                {:source-paths ["src/generator/"]
                 :output-path "target/classes/generator"
                 :rules :clj}
                {:source-paths ["src/generator/"]
                 :output-path "target/cljs/generator"
                 :rules :cljs}
        ]}

    }
  })

