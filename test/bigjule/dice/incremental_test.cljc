(ns bigjule.dice.incremental-test
  (:require #?(:cljs [cljs.test :as test :refer-macros [deftest testing is]]
               :clj  [clojure.test :refer :all])
            [bigjule.core :as bjd]
            [bigjule.dice.incremental :as bjdi]))

(deftest test-incremental-expected-result
  (testing "Confirm incremental dice return expected so other tests work"
    (bjdi/reset-incremental!)
    (is (= '(1 2 3 4 5 6 6) (bjd/roll [7 :dinc 6 identity])))
    (bjdi/reset-incremental!)))
