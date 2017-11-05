(ns bigjule.modifiers.bias-test
  (:require #?(:cljs [cljs.test :as test :refer-macros [deftest testing is]]
               :clj  [clojure.test :as test])
            [bigjule.core :as bjd]
            [bigjule.modifiers.bias :as bjdmb]
            [bigjule.dice.incremental :as bjdi]))

(defmethod bjd/roll-die :dmax
  [_ {:keys [sides]}]
  sides)

(test/deftest test-discard-lowest-expected
  (test/testing "confirm that one of the dice is not rolled with bjdmb/discard-lowest"
    (test/is (= 18 (bjd/roll [4 :dmax 6 bjdmb/discard-lowest])))))

(test/deftest test-discard-highest-expected
  (test/testing "confirm that one of the dice is not rolled with bjdmb/discard-highest"
    (test/is (= 18 (bjd/roll [4 :dmax 6 bjdmb/discard-highest])))))

(test/deftest test-discard-lowest-discards-lowest
  (test/testing "just confirm that the lowest number is tossed"
    (bjdi/reset-incremental!)
    (test/is (not= 21 (bjd/roll [6 :dinc 6 bjdmb/discard-lowest])))
    (bjdi/reset-incremental!)
    (test/is (= 20 (bjd/roll [6 :dinc 6 bjdmb/discard-lowest])))))

(test/deftest test-discard-highest-discards-highest
  (test/testing "just confirm that the highest number is tossed"
    (bjdi/reset-incremental!)
    (test/is (not= 21 (bjd/roll [6 :dinc 6 bjdmb/discard-highest])))
    (bjdi/reset-incremental!)
    (test/is (= 15 (bjd/roll [6 :dinc 6 bjdmb/discard-highest])))))

(test/deftest test-available-keyword-modifiers
  (test/testing "test keyword lowest"
    (bjdi/reset-incremental!)
    (test/is (= 5 (bjd/roll [3 :dinc 6 :discard-lowest])))
    (bjdi/reset-incremental!))
(test/testing "test keyword highest"
    (bjdi/reset-incremental!)
    (test/is (= 3 (bjd/roll [3 :dinc 6 :discard-highest])))
    (bjdi/reset-incremental!)))
