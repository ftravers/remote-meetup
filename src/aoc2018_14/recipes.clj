(ns aoc2018-14.recipes)

(defn digits [number]
  "Provides the sequence of each digit from the given number."
  (map (fn [c] (java.lang.Integer/parseInt (str c))) (str number)))

(defn next-recipes [recipes locations]
  "Given a vector of single-digit integers, each representing a taste-testing score for a hot chocolate recipe, returns the vector with updated scores for the next round of testing."
  (let [[elf-1 elf-2] locations
        sum (+ (nth recipes elf-1)
               (nth recipes elf-2))
        the-digits (digits sum)
        new-recipes (concat recipes the-digits)]
    (vec new-recipes)))
