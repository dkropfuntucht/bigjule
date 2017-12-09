(defproject bigjule "0.1.1-SNAPSHOT"
  :description "Clojure Dice Rolling Library"
  :url "http://github.com/dkropfuntucht/bigjule"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  
  :dependencies [[org.clojure/clojure             "1.9.0"]
                 [org.clojure/clojurescript     "1.9.946"]
                 [org.clojure/test.check          "0.9.0"]]
  
  :plugins [[lein-cljsbuild "1.1.7"]]
  
  :main bigjule.core

  :cljsbuild {:builds [{:id "test-build"
                        :source-paths ["src/clj" "target/classes" "test"]
                        :compiler {:output-to "out/test.js"
                                   :main bigjule.test-runner
                                   :target :nodejs
                                   :optimizations :none}}]
              :test-commands {"test" ["nodejs"
                                    ; Files will be crated later:
                                    "out/test.js"]}})
