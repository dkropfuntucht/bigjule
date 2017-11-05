(ns bigjule.modifiers.bias
  (:require [bigjule.core :as bjd]))

(defn discard-highest
  "Modifier fn that discards the highest roll from the total.  This effectively
   reduces the dices count by 1, but biases the total towards lower results."
  [rolls arguments]
  (apply + (into (drop 1 (reverse (sort rolls))) arguments)))

(defn discard-lowest
  "Modifier fn that discards the lowest roll from the total.  This effectively
   reduces the dice count by 1, but biases the total towards a higher result. "
  [rolls arguments]
  (apply + (into (drop 1 (sort rolls)) arguments)))

(defmethod bjd/modifier :discard-highest
  [_ rolls argumentss]
  (discard-highest rolls argumentss))

(defmethod bjd/modifier :discard-lowest
  [_ rolls arguments]
  (discard-lowest rolls arguments))
