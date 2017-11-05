(ns bigjule.modifiers.check
  (:require [bigjule.core :as bjd]))

(defn count-over
  "This returns the number of dice rolled equal to or above the first
   supplied argument in the dice specification."
  [rolls arguments]
  (count (filter #(>= %(first arguments)) rolls)))

(defn count-under
  "This returns the number of dice rolled equal to or under the first
   supplied argument in the dice specification."
  [rolls arguments]
  (count (filter #(<= %(first arguments)) rolls)))


(defmethod bjd/modifier :count-over
  [_ rolls arguments]
  (count-over rolls arguments))

(defmethod bjd/modifier :count-under
  [_ rolls arguments]
  (count-under rolls arguments))
