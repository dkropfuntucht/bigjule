(ns bigjule.dice.open-ended-test
  (:require #?(:cljs [cljs.test :as test :refer-macros [deftest testing is]]
               :clj  [clojure.test :as test])
            [bigjule.core :as bjd]
            [bigjule.dice.open-ended :as bjdoe]))

(test/deftest test-low-open-ended
  (test/testing "Low open Ended rolls do not exceed sides, produce ok results"
    (let [vals (map (fn [_] (bjd/roll [:d-open-low 6])) (range 0 1000))]
      (test/is (every? #(< % 7) vals))
      (test/is (every? number? vals)))))

(test/deftest test-high-open-ended
  (test/testing "High open Ended rolls are uniformly positive numbers"
    (let [vals (map (fn [_] (bjd/roll [:d-open-high 6])) (range 0 1000))]
      (test/is (every? pos? vals))
      (test/is (every? number? vals)))))

(test/deftest test-open-ended
  (test/testing "Open-Ended roles should be positive and negative and zero"
    (let [vals (map (fn [_] (bjd/roll [:d-open 6])) (range 0 1000))]
      (test/is (some pos? vals))
      (test/is (some #(> % 6) vals))
      (test/is (some neg? vals))
      (test/is (some zero? vals))
      (test/is (every? number? vals)))))
