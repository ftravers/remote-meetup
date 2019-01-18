(ns aoc2018-12.next-generation)

(defn pad-false [num-pads in-vec]
  nil)

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

(defn next-generation [curr-plants rules]
  (map-indexed
   (fn [idx itm]
     (->> curr-plants
          (get-neighbours idx)
          (get-next-plant-state rules)))
   (into [] (concat [false false] curr-plants [false false]))))

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
