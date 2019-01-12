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

(defn move-cart [{:keys [pos dir turn] :as cart} tracks]
  {:pre [(cart? cart) (tracks? tracks)] :post [(cart? %)]}
  (let [
        deltas (dir2delta dir)
        track (-> tracks (:row pos) (:col pos))
        turning (= track \+)
        ]
    {:pos {:row (+ (:row pos) (:row deltas)) :col (+ (:col pos) (:col deltas))}
     :dir (if turning (-> dir&track2delta dir track) (-> dir&turn2delta dir turn))
     :turn (if turning (mod (inc turn) 3) turn)})
  )

(defn sort-carts "Sort the carts as they take turns to move (depending on their current positions)." [carts]
 {:pre [(carts? carts)] :post [(carts? %)]}
  (let [
    row2col2cart (reduce
      (fn [row2col2cart {pos :pos :as cart}]
          {:pre [(cart? cart)]}
          (let [row-map (or (row2col2cart (:row pos)) (sorted-map))
                _ (assert (nil? (row-map (:col pos))))
                row-map-new (assoc row-map (:col pos) cart)])
        )
      (sorted-map)
      carts)]
      (flatten (map (partial map val) (vals row2col2cart)))) ;flatten doesn't work on maps: (flatten {:i 1}) ;=> nil
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
        _ (assert (every? cart? initial-carts))
        ])
  )
