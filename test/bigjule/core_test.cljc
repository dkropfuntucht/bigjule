(ns bigjule.core-test
  (:require #?(:cljs [cljs.test :as test :refer-macros [deftest testing is]])
            #?(:clj [clojure.test :as test])
            [bigjule.core :as bjd :refer-macros [defroll]]
            [bigjule.dice.incremental :as bjdi]))

;; - many of the random tests good give false pos/neg if something happened
(test/deftest test-just-sides
  (test/testing "roll a single die using just the number of sides."
    (let [val (bjd/roll [6])]
      (test/is (pos? val))
      (test/is (< val 7)))))

(test/deftest test-single-die
  (test/testing "roll a single die."
    (let [val (bjd/roll [:d 6])]
      (test/is (pos? val))
      (test/is (< val 7)))))

(test/deftest test-classic-3d6
  (test/testing "roll 3d6."
    (let [val (bjd/roll [3 :d 6])]
      (test/is (pos? val))
      (test/is (> val 2))
      (test/is (< val 19))))
  (test/testing "roll 3d6. incremental"
    (bjdi/reset-incremental!)
    (test/is (= 6  (bjd/roll [3 :dinc 6])))
    (test/is (= 15 (bjd/roll [3 :dinc 6])))
    (test/is (= 18 (bjd/roll [3 :dinc 6])))
    (bjdi/reset-incremental!)))

(test/deftest test-standard-3d6+1
  (test/testing "roll 3d6+1"
    (let [val (bjd/roll [3 :d 6 + 1])]
      (test/is (pos? val))
      (test/is (> val 3))
      (test/is (< val 20))))
  (test/testing "roll 3d6+1. incremental"
    (bjdi/reset-incremental!)
    (test/is (= 7  (bjd/roll [3 :dinc 6 + 1])))
    (test/is (= 16 (bjd/roll [3 :dinc 6 + 1])))
    (test/is (= 19 (bjd/roll [3 :dinc 6 + 1])))
    (bjdi/reset-incremental!)))

(test/deftest test-standard-3d6-1
  (test/testing "roll 3d6-11"
    (let [val (bjd/roll [3 :d 6 - 1])]
      (test/is (pos? val))
      (test/is (> val 1))
      (test/is (< val 18))))
  (test/testing "roll 3d6-1. incremental"
    (bjdi/reset-incremental!)
    (test/is (= 5  (bjd/roll [3 :dinc 6 - 1])))
    (test/is (= 14 (bjd/roll [3 :dinc 6 - 1])))
    (test/is (= 17 (bjd/roll [3 :dinc 6 - 1])))
    (bjdi/reset-incremental!)))

(test/deftest test-rolled-number-of-dice
  (test/testing "roll (1d6)d6"
    (test/is
     (let [val (bjd/roll [[:d 6] :d 6])]
       (and (pos? val)
            (> val 1)
            (< val 37)))))
  (test/testing "roll (1d6)d6 incremental"
    (test/is
     (bjdi/reset-incremental!)
     (let [val (bjd/roll [[:dinc 6] :dinc 6])]
       (bjdi/reset-incremental!)
       (= val 2)))))

(test/deftest test-rolled-number-of-sides
  (test/testing "roll 2d(2d4)"
    (let [val (bjd/roll [2 :d [2 :d 4]])]
      (test/is (pos? val))
      (test/is (> val 1))
      (test/is (< val 17))))
  (test/testing "roll (2d4)d(2d4) incremental"
    (bjdi/reset-incremental!)
    (let [val (bjd/roll [[2 :dinc 4] :dinc [2 :dinc 4]])]
      (bjdi/reset-incremental!)
      (test/is (= val 24)))))

(test/deftest test-roll-3d6-less-2d6
  (test/testing "Roll 3d6 - 2d6"
    (let [val (bjd/roll [3 :d 6 - [2 :d 6]])]
      (test/is (> val -10))
      (test/is (< val 17))))
  (test/testing "Roll 3d6 - 2d6 incremental"
    (bjdi/reset-incremental!)
    (let [val (bjd/roll [3 :dinc 6 - [2 :dinc 6]])]
      (bjdi/reset-incremental!)      
      (test/is (= val -3)))))

(bjd/defroll t3d6 [3 :d 6])
(test/deftest test-defroll
  (test/testing "test the defroll binding form"
    (do
      (test/is (fn? t3d6))
      (let [v (t3d6)]
        (test/is (> v 2))
        (test/is (< v 19))))))

(test/deftest test-keyword-summary-fn
  (test/testing "test keyword minus"
    (bjdi/reset-incremental!)
    (test/is (= 4 (bjd/roll [3 :dinc 6 :- 2])))
    (bjdi/reset-incremental!)))
