(ns bigjule.modifiers.check-test
  (:require #?(:cljs  [cljs.test :as test :refer-macros [deftest testing is]]
               :clj   [clojure.test :as test])
            [bigjule.core :as bjd]
            [bigjule.dice.incremental :as bjdi]
            [bigjule.modifiers.check :as bjmc]))

(defmethod bjd/roll-die :dmax
  [_ {:keys [sides]}]
  sides)

(defmethod bjd/roll-die :dmin
  [_ _]
  1)

(test/deftest test-count-over
  (test/testing "all dice over"
    (test/is (= 4 (bjd/roll [4 :dmax 6 bjmc/count-over 3]))))
  (test/testing "all dice under"
    (test/is (zero? (bjd/roll [4 :dmin 6 bjmc/count-over 3]))))
  (test/testing "half dice over"
    (bjdi/reset-incremental!)
    (test/is (= 3 (bjd/roll [6 :dinc 6 bjmc/count-over 4])))))

(test/deftest test-count-under
  (test/testing "all dice over"
    (test/is (zero? (bjd/roll [4 :dmax 6 bjmc/count-under 3]))))
  (test/testing "all dice under"
    (test/is (= 4 (bjd/roll [4 :dmin 6 bjmc/count-under 3]))))
  (test/testing "half dice under"
    (bjdi/reset-incremental!)
    (test/is (= 3 (bjd/roll [6 :dinc 6 bjmc/count-under 3])))))

(test/deftest test-keyword-modifiers
  (test/testing "test keyword :count-over"
    (bjdi/reset-incremental!)
    (test/is (= 4 (bjd/roll [6 :dinc 6 :count-over 3])))
    (bjdi/reset-incremental!))
  (test/testing "test keyword :count-under"
    (bjdi/reset-incremental!)
    (test/is (= 3 (bjd/roll [6 :dinc 6 :count-under 3])))
    (bjdi/reset-incremental!)))
