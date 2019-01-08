(ns aoc2018-14.next-loc)

(comment
  (def locations [0 1])
  (def recipes [3 7 1 0]))

(defn one-elf-next-loc [length curr-index jump]
  (mod (+ curr-index jump) length))

(defn next-location [recipes locations]
  (let [a-loc (locations 0)
        b-loc (locations 1)
        a-jump (+ 1 (recipes a-loc))
        b-jump (+ 1 (recipes b-loc))
        a-next-loc (one-elf-next-loc (count recipes) a-loc a-jump)
        b-next-loc (one-elf-next-loc (count recipes) b-loc b-jump)]
    [a-next-loc b-next-loc]))

(defn finished?
  ([input-recipes current-recipes]
   (finished? input-recipes current-recipes 10))
  ([input-recipes current-recipes size]
   (let [remaining (drop (count input-recipes) current-recipes)]
     (if (>= (count remaining) size)
       (into [] (take size remaining))
       nil))))

(comment
  (def length 4)
  (def curr-index 1)
  (def jump 8))

(comment
  (next-location recipes locations))


