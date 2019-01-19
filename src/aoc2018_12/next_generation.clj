(ns aoc2018-12.next-generation)

(defn plant-left-shift [[pot2 pot1]]
  (cond
    pot2 2
    pot1 1
    :else 0))

(defn trim-left [[pot2 pot1 & plants]]
  (cond
    pot2 (into [] (concat [pot2] [pot1] plants))
    pot1 (into [] (cons pot1 plants))
    :else plants))

(defn get-neighbours [ndx plants]
  (let [padded-2-plants
        (into [] (concat
                  [false false] plants [false false]))
        new-ndx (+ 2 ndx)]
    (subvec padded-2-plants
            (- new-ndx 2)
            (+ new-ndx 3))))

(defn get-next-plant-state [rules neighbours]
  (if (get rules neighbours) true false))

(defn trim-up-to-2-false-each-side [plants])

(defn next-generation [curr-plants rules]
  (let [padded-2-each-side
        (into []
              (concat [false false] curr-plants [false false]))]
    (map-indexed
     (fn [idx itm]
       (->> padded-2-each-side
            (get-neighbours idx)
            (get-next-plant-state rules)))
     padded-2-each-side)))

(defn convert-hash-2-true [hash-dots-str]
  (mapv (fn [x]
         (case x
           \. false
           \# true))
       hash-dots-str))

(defn parse-rule [string-rule]
  (let [only-selector (subs string-rule 0 5)]
    (convert-hash-2-true only-selector)))

(defn parse-all-rules [rules-str]
  (zipmap (map parse-rule rules-str)
          (map (constantly true) (range 20))))
