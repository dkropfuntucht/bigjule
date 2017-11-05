(ns bigjule.dice.incremental
  "A roll-die that runs up to its maximum - useful for testing"
  (:require [bigjule.core :as bjd]))

(def incremental (atom 0))

(defn reset-incremental! [] (reset! incremental 0))

(defmethod bjd/roll-die :dinc
  [_ {:keys [sides]}]
  (if (= @incremental sides)
    sides
    (swap! incremental inc)))
