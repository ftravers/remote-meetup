(ns aoc2018-13.eagle)

; => seq of rows; each row is a seq of char.
(defn parse-lines [text] {:pre [(every? string? text)] :post [(every? (partial every? char?) %)]}
  (let [lines (clojure.string/split text #"\n")]
    (map seq lines)
  ))

(def track-chars #{\| \- \\ \/ \+})
(def direction-chars #{\^ \v \> \< })
(defn tracks? [matrix]
  (and (vector? matrix) (every? vector? matrix)
       (every? (fn [row] (every? (some-fn track-chars (partial = \space)) row)) matrix)))

(defn chars-to-tracks "Take output of function parse-lines. Replace any car directions <>v^ with respective track direction." [chars]
  {:pre [(every? (partial every? char?) chars)]
   :post [(tracks? %)]}
  (mapv (partial mapv {\< \-, \> \-, \v \| \^ \|}) chars))

(defn coordinates? [{:keys [row col]}]
            (every? (every-pred int? (partial < -1)) [row col]))

(defn direction-coordinates? [{:keys [row col]}]
      (and (every? (every-pred int? (partial <= -1) (partial >= 1)) [row col])
           (not (and (zero? row) (zero? col)))))

(defn cart? [{:keys [pos dir turn]}] ;Example: {:pos {:row 2 :col 5} :dir direction-character :turn 0-2 }
  (and (coordinates? pos) (direction-chars dir) (<= 0 turn 2)))
(def carts? (partial every? cart?))

(def dir2delta {\< {:row 0 :col -1}, \> {:row 0 :col 1}, \^ {:row -1 :col 0}, \v {:row 1 :col 0}})
(def dir&track2delta {
  \< {}
  \> {}
  \^ {}
  \v {}
  }) ;not for junction +
(def dir&turn2delta {
  \< []
  \> []
  \^ []
  \v []
  })

(defn move-cart [tracks {:keys [pos dir turn] :as cart}] ;tracks is the first param, so that move-carts can (partial move-cart tracks).
  {:pre [(cart? cart) (tracks? tracks)] :post [(cart? %)]}
  (let [
        deltas (dir2delta dir)
        track (-> tracks (:row pos) (:col pos))
        turning (= track \+)
        ]
    {:pos {:row (+ (:row pos) (:row deltas)) :col (+ (:col pos) (:col deltas))}
     :dir (if turning (-> dir&track2delta dir track) (-> dir&turn2delta dir turn))
     :turn (if turning (mod (inc turn) 3) turn)}))

;(gen-class :name "aoc2018_13.eagle.CartCrash" :extends RuntimeException)

(defn sort-carts "Sort the carts as they take turns to move (depending on their current positions). Expect no crashes. Return a sorted-map {row => sorted-map { column => cart}}."
 [carts]
 {:pre [(carts? carts)] :post [(sorted? %) (every? sorted? %) (every? (comp (partial every? cart?) vals) %)]}
      (reduce
          (fn [row2col2cart {pos :pos :as cart}]
              {:pre [(cart? cart)]}
              (let [row-map (or (row2col2cart (:row pos)) (sorted-map))
                    _ (assert (not (row-map (:col pos))))
                    row-map-new (assoc row-map (:col pos) cart)]
                 (assoc row2col2cart (:row pos) row-map-new)))
          (sorted-map)
          carts))

(defn flatten-sorted-carts [row2col2cart] {:post [(every? cart? %)]}
  (flatten (map (partial map val) (vals row2col2cart)))) ;flatten doesn't work on maps: (flatten {:i 1}) ;=> nil

(defn move-all "Expects the carts to be sorted already. Move all carts by one step. On success, return seq of (updated) carts, but not re-sorted. On crash, return coordinates."
 [tracks carts]
  {:pre [(tracks? tracks) (carts? carts)] :post [(or (carts? %) (coordinates? %))]}
  ;Not using map, because on every step we check for a crash. Hence not: (map (partial move-cart tracks) carts)
  ;Following starts with a full list of carts, instead of empty (as is common with reduce). It updates each cart. That way it can detect the first crash, too.
  (reduce
    (fn [carts cart]
      )
    carts carts))

#_(defn detect-crash "Take the old seq. of carts, sorted. Take a new seq of carts, not re-sorted (hence in the old order, but with updated :pos). Return position map of the first crash, or nil."
  [old new]
  {:pre [(carts? old) (carts? new)] :post [(or (coordinates? %) (nil? %))]}
  :TODO
)

(defn -main [& args]
  (let [chars (-> args first slurp parse-lines)
        tracks (chars-to-tracks chars)
        height (count chars)
        width  (count (nth chars 0))
        initial-carts (for [row-idx (range 0 height)
                            col-idx (range 0 width)
                            :let [cart-dir (-> chars (nth row-idx) (nth col-idx))]
                            :when cart-dir]
                            {:pos {:row row-idx, :col col-idx} :dir cart-dir :turn 0})
        initial-cards-sorted (sort-carts initial-carts)
        ])
  )
