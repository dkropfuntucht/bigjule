(ns bigjule.test-runner
  (:require [bigjule.core-test :as bjdct]
            [bigjule.dice.incremental-test :as bjdit]
            [bigjule.dice.open-ended-test :as bjdot]
            [bigjule.modifiers.bias-test :as bjdbt]
            [bigjule.modifiers.check-test :as bjdmct]
            [cljs.test :as test :refer-macros [run-all-tests]]))


(enable-console-print!)

(test/run-all-tests)
