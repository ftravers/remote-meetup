(ns aoc2018-13.eagle)

; => seq of rows; each row is a seq of char.
(defn parse-lines [text] {:pre [(string? text)] :post [(every? (partial every? char?) %)]}
  (let [lines (clojure.string/split text #"\n")]
    (map seq lines)
  ))

(def track-chars #{\| \- \\ \/ \+})
(def direction-chars #{\^ \v \> \< })
(defn tracks? [matrix]
  (and (vector? matrix) (every? vector? matrix)
       (every? (fn [row] (every? (some-fn track-chars (partial = \space)) row)) matrix)))

(defn chars-to-tracks "Take output of function parse-lines. Replace any car directions <>v^ with respective track direction."
  [chars]
  {:pre [(every? (partial every? char?) chars)]
   :post [(tracks? %)]}
  (mapv (partial mapv #(get {\< \-, \> \-, \v \| \^ \|} % %)) chars))

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
(def dir&turn2delta { ;order of turns: left, straight, right
  \< []
  \> []
  \^ []
  \v []
  })

(defn move-cart [tracks {:keys [pos dir turn] :as cart}] ;tracks is the first param, if we need to (partial move-cart tracks).
  {:pre [(cart? cart) (tracks? tracks)] :post [(cart? %)]}
  (let [
        deltas (dir2delta dir)
        track (-> tracks (:row pos) (:col pos))
        turning (= track \+)
        ]
    {:pos {:row (+ (:row pos) (:row deltas)) :col (+ (:col pos) (:col deltas))}
     :dir (if turning (-> dir&track2delta dir track) (-> dir&turn2delta dir turn))
     :turn (if turning (mod (inc turn) 3) turn)}))

(defn sorted-carts? "Validate result of sort-carts." [row2col2cart]
  (and (sorted? row2col2cart) (every? sorted? row2col2cart) (every? (comp (partial every? cart?) vals) row2col2cart)))

(defn update-sorted-carts [row2col2cart pos cart validate-place-available]
  {:pre [(sorted-carts? row2col2cart) (coordinates? pos) (or (cart? cart) (nil? cart)) (boolean? validate-place-available)]
   :post [(sorted-carts? %)]}
  (let [row-map (or (row2col2cart (:row pos)) (sorted-map))
        _ (assert (or (not validate-place-available) (not (row-map (:col pos)))))
        row-map-new (assoc row-map (:col pos) cart)]
     (assoc row2col2cart (:row pos) row-map-new)))

(defn sort-carts "Sort the carts as they take turns to move (depending on their current positions). Expect no crashes. Return a sorted-map {row => sorted-map { column => cart}}."
 [carts]
 {:pre [(carts? carts)] :post [(sorted-carts? %)]}
      (reduce
          (fn [row2col2cart {pos :pos :as cart}]
              (update-sorted-carts row2col2cart pos cart true))
          (sorted-map)
          carts))

(defn flatten-sorted-carts [row2col2cart] {:pre [(sorted-carts? row2col2cart)] :post [(every? cart? %)]}
  (flatten (map (partial map val) (vals row2col2cart))))

(defn move-all "Expects the carts to be sorted already. Move all carts by one step. On success, return an updated sorted-map of sorted-map of (updated) carts. On crash, return coordinates."
 [tracks row2col2cart]
  {:pre [(sorted-carts? row2col2cart)] :post [(or (sorted-carts? %) (coordinates? %))]}
  ;Not using map, because on every step we check for a crash. Hence not: (map (partial move-cart tracks) carts)
  ;Following starts with a full list of carts, instead of empty (as is common with reduce). It updates each cart. That way it can detect the first crash, too.
  (reduce
        (fn [row2col2cart cart]
          (let [moved (move-cart cart)
                pos (:pos moved)]
            (if (-> row2col2cart (:row pos) (:col pos)) ;crash?
              (reduced pos) ;crash => stop reducing, return the position
              (let [removed-old  (update-sorted-carts row2col2cart #_old-position=> (:pos cart) nil false)
                    inserted-new (update-sorted-carts removed-old pos moved true)]
                  inserted-new))))
        row2col2cart (flatten-sorted-carts row2col2cart)))

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
        _ (println initial-carts)
        row2col2cart (sort-carts initial-carts)
        find-crash (fn [row2col2cart]
                    {:pre [(sorted-carts? row2col2cart)]} ;(loop) doesn't allow preconditions. That's why we use a separate function.
                    (let [new-row2col2cart-or-crash (move-all tracks row2col2cart)]
                      (if (coordinates? new-row2col2cart-or-crash)
                        new-row2col2cart-or-crash
                        (recur new-row2col2cart-or-crash))))
        crash (find-crash row2col2cart)
        _ (assert (coordinates? crash))
        _ (println "Crash:" crash)
        ])
  )
