(ns bigjule.core
  (:require [clojure.spec.alpha :as s])
  #?(:cljs
     (:require-macros [bigjule.core])))

;;============================================================================
;; A spec defining the simple dice language
;;============================================================================
;; - pieces of the dice language
(s/def ::number-or-dice
  (s/or ::roll ::dice-spec ::count #(and (integer? %) (pos? %))))
(s/def ::dice-count ::number-or-dice)
(s/def ::dice-sides ::number-or-dice)
(s/def ::die-type keyword?)
(s/def ::die-modifier #(or (fn? %) (keyword? %)))
(s/def ::die-arg ::number-or-dice)

;; - various top-level formats for the dice language
(s/def ::n-d-s        (s/cat ::dice-count ::dice-count
                             ::die-type ::die-type
                             ::dice-sides ::dice-sides))

(s/def ::one-d-s      (s/cat ::die-type ::die-type ::dice-sides ::dice-sides))

(s/def ::just-d-s     (s/cat ::dice-sides ::dice-sides))

(s/def ::with-mod     (s/cat ::dice-count   ::dice-count
                             ::die-type     ::die-type
                             ::dice-sides   ::dice-sides
                             ::die-modifier ::die-modifier))

(s/def ::with-args   (s/cat ::dice-count   ::dice-count
                            ::die-type     ::die-type
                            ::dice-sides   ::dice-sides
                            ::die-modifier ::die-modifier
                            ::die-args     (s/+ ::die-arg)))

;; - roll it all up into one "type"
(s/def ::dice-spec (s/alt ::n-d-s     ::n-d-s
                          ::one-d-s   ::one-d-s
                          ::just-d-s  ::just-d-s
                          ::with-mod  ::with-mod
                          ::with-args ::with-args))

;;============================================================================
;; Die-Rolling multi-method
;;============================================================================
(defmulti roll-die
  "Responsible for actually generating the dice number.  The data argument
   should be a map containing a sides parameter.  Perhaps future customizations
   can carry more on that map.  Use defmethod to create new types of dice."
  (fn [type data] type))

(defmethod roll-die :default
  [type {:keys [sides]}]
  (inc (int (* (rand sides)))))

;;============================================================================
;; Modifier fn multi-method and common summary fns
;;============================================================================
(defmulti modifier
  "This multi-method is used to define roll modifier functions.  It should
   accept two arguments the first is a sequence of generated rolls, the second
   is a sequence of arguments provided to the modifier.  Use defmethod to provide
   new modifier functions."
  (fn [type rolls args] type))

(defmethod modifier :+
  [_ rolls args]
  (apply + (into rolls args)))

(defmethod modifier :identity
  [_ rolls args]
  rolls)

(defmethod modifier :-
  [_ rolls args]
  (apply - (into [(reduce + rolls)] args)))

;;============================================================================
;; Internal Implementation
;;============================================================================
(declare prepare-roll-parsed)

(defn- roll-or-number
  [struct]
  (cond
    (number? struct)
    (constantly struct)
    (= (first struct) ::roll)
    (prepare-roll-parsed (second struct))
    :or
    (constantly (second struct))))

(defn- prepare-roll-parsed [spec-parsed-dice-spec]
  (let [struct     spec-parsed-dice-spec
        dice-sides (roll-or-number (::dice-sides (second struct)))
        die-type   (::die-type (second struct) :d)
        dice-count (roll-or-number (::dice-count (second struct) 1))
        summary    (::die-modifier (second struct) +)
        arguments  (::die-args (second struct) [])]
    (fn []
      (let [rolls (map
                   (fn [_] (roll-die die-type {:sides (dice-sides)}))
                   (range 0 (dice-count)))
            args  (map #((roll-or-number %)) arguments)]
        (cond
          (contains? #{+} summary)
          (apply summary (into rolls args))
          (contains? #{identity} summary)
          (summary rolls)
          (= - summary)
          (apply - (into [(reduce + rolls)] args))
          (keyword? summary)
          (modifier summary rolls args)
          :or
          (summary rolls args))))))

;;============================================================================
;; API
;;============================================================================
(defn prepare-roll [dice-spec]
  "Parses a dice spec. and returns a fn that rolls the specified dice."
  (prepare-roll-parsed (s/conform ::dice-spec dice-spec)))

#?(:clj
   (defmacro defroll [name dice-spec]
     "Prepare a roll defined by dice-spec and bind it to name."
     `(def ~name (prepare-roll ~dice-spec))))

(defn roll [dice-spec]
  "Roll the dice-spec provided, immediately returning the result."
  ((prepare-roll dice-spec)))
