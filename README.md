# Big Jule

```
"I had the spots taken off for luck. But I remember where the spots formerly were."
```
- Big Jule - _Guys and Dolls_

Big Jule is an extensible and comprehensive Dice Roller for Clojure/Script.
It allows for expressing dice rolls in a traditional, natural format, and can build reusable dice-rolling functions. 

Dependencies are minimal, but meant to be Clojure 1.9 ready.  The presence of spec is assumed.

## Usage

Big Jule is available in Clojars. Add this `:dependency` to your Leiningen
`project.clj`:

```clojure
[bigjule "0.1.0"]
```

The library should has a minimal API and should be fairly easy to use.  These examples
should work in a repl.

Pull it in for use as such:

```clojure

  (require '[bigjule.core :as bjd])
  
```

A bare number supplied as a dice/roll specification will be treated as the number of sides.  This should look like this:

```clojure
 (bjd/roll [6])
 
 #_=> 4
 
```

Be a little more explicit with your roll request:

```clojure
 (bjd/roll [:d 6])
 
 #_=> 5
 
```

Roll more dice and total the result:

```clojure
 (bjd/roll [3 :d 6])
 
 #_=> 12
 
```

By default in these scenarios, we return the total of the roll.  `identity` as a dice modifier will return a col of individual dice rolls:

```clojure
  (bjd/roll [3 :d 6 identity])

  #_=> (4 2 1)
  
```

To allow modifiers to be passed as pure data, modifier functions can be supplied as keywords too:

```clojure
  (bjd/roll [3 :d 6 :identity])
  
  #_=> (4 5 2)

```

Other common dice modifiers are built into the library:

```clojure
  (bjd/roll [3 :d 6 + 5]

  #_=> 18
  
```

Try a different, improbable, number of sides:
```clojure
  (bjd/roll [2 :d 7])
   
  #_=> 5
  
```

...and with another common modifier:
```clojure
  (bjd/roll [2 :d 7 - 1])
  
  #_=> 3
  
```

Dice specifications can consume other dice specifications, allowing us to roll random sided dice and dice counts:

```clojure
  (bjd/roll [[1 :d 4] :d [2 :d 4]])
  
  #_=> 6
  
```

Arguments to modifiers can be dice specifications, too:

```clojure
  (bjd/roll [1 :d 6 - [1 :d 6]])
  
  #_ => -1
  
```

A dice-roller can be produced as a function with:

```clojure

  (bjd/prepare-roll [4 :d 6])
  
  #_ => 15
  
```

A convenience macro is provided, too:
```clojure
  (bjd/defroll r2d6 [2 :d 6])
  
  (r2d6)
  
  #_=> 5
  
```


The modifier position should be a function.  Except for a few obvious special cases,
it should be an fn of two arguments - a list of roll results and a list of arguments.

The roll-die multimethod can be extended to provide different dice-rolling conventions.

That should about cover it.  Enjoy.

## Roadmap
This mostly does what I need right now, but I suspect that might be a few interesting changes in the future.

I'm likely to overhaul the modifiers system to allow for multiple modifiers. 
Not 100% sure how I want to treat presidency rules between modifier functions, so I may leave well enough alone for a while.

It might be interesting to provide configurable random seeds and saved state for save games.

Suggestions and PRs welcome.

## License

    Copyright © 2017 Damon Kropf-Untucht
    Distributed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html) version 1.0

