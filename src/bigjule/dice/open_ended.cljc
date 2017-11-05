(ns bigjule.dice.open-ended
  (:require [bigjule.core :as bjd]))

(defmethod bjd/roll-die :d-open-low
  [type {:keys [sides total low] :as data}]
  (let [trigger-range (int (max (* 0.1 sides) 1.0))
        total         (if (some? total) total 0)
        low           (if (some? low) low false)
        roll          (bjd/roll-die :d data)]
    (cond
      ;; - straight roll with no recurse
      (and (> roll trigger-range) (zero? total))
      roll
      ;; - closing out with subtraction of total
      (and (< roll (- sides (dec trigger-range))) low)
      (- total roll)
      ;; - high roll to continue subtraction or first time
      ;; under threshold
      (or (and (not low) (< roll (inc trigger-range)))
          (and (> roll (- sides trigger-range)) low))
      (recur :d-open-low
             {:sides sides
              :total (if (not low) roll (- total roll))
              :low true}))))

(defmethod bjd/roll-die :d-open-high
  [type {:keys [sides total high] :as data}]
  (let [trigger-range (int (max (* 0.1 sides) 1.0))
        high-trigger  (- sides (dec trigger-range))
        total         (if (some? total) total 0)
        high          (if (some? high) high false)
        roll          (bjd/roll-die :d data)]
    (if (< roll high-trigger)
      (+ total roll)
      (recur :d-open-high {:sides sides :total (+ total roll) :high true}))))


(defmethod bjd/roll-die :d-open
  [type {:keys [sides] :as data}]
  (let [trigger-range (int (max (* 0.1 sides) 1.0))
        high-trigger  (- sides trigger-range)
        low-trigger   (inc trigger-range)
        roll          (bjd/roll-die :d data)]
    (cond
      (> roll high-trigger)
      (bjd/roll-die :d-open-high
                   {:sides sides
                    :total roll
                    :high true})
      (< roll low-trigger)
      (bjd/roll-die :d-open-low
                   {:sides sides
                    :total roll
                    :low true})
      :or
      roll)))


