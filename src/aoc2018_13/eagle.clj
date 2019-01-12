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
            (every? (every-pred int? (partial <= 0)) [row col]))

(defn direction-coordinates? [{:keys [row col]}]
      (and (every? (every-pred int? (partial <= -1) (partial >= 1)) [row col])
           (not (and (zero? row) (zero? col)))))

(defn cart? [{:keys [pos dir turn]}] ;Example: {:pos {:row 2 :col 5} :dir direction-character :turn 0-2 }
  (and (coordinates? pos) (direction-chars dir) (<= 0 turn 2)))
(def carts? (partial every? cart?))

(def dir2delta {\< {:row 0 :col -1}, \> {:row 0 :col 1}, \^ {:row -1 :col 0}, \v {:row 1 :col 0}})
(def dir&track2dir { ;previous direction & new place => new direction. For impossible turns use nil.
  \< {\| nil, \- \<,  \\ \^, \/ \v}
  \> {\| nil, \- \>,  \\ \v, \/ \^}
  \^ {\| \^,  \- nil, \\ \<, \/ \>}
  \v {\| \v,  \- nil, \\ \>, \/ \<}
  }) ;not for junctions
(def dir&turn2dir { ;at junctions only: ;previous direction & 0-based index of the cart's next junction turn => new direction. For impossible turns use nil.
  ; order of junction turns: left, straight, right
  \< [\v \< \^]
  \> [\^ \> \v]
  \^ [\< \^ \>]
  \v [\> \v \<]
  })

(defn move-cart [tracks {:keys [pos dir turn] :as cart}] ;tracks is the first param, if we need to (partial move-cart tracks).
  {:pre [(cart? cart) (tracks? tracks)] :post [(do (println "move-cart: old" cart "=> new:" %) true) (cart? %)]}
  (println "dir" dir)
  ; First move, applying the current directon. Then calculate the next direction, depending on the previous direction and the new track.
  (let [
        deltas (dir2delta dir)
        new-pos {:row (+ (:row pos) (:row deltas)), :col (+ (:col pos) (:col deltas))}
        track (-> tracks (get (:row new-pos)) (get (:col new-pos)))
        turning (= track \+)
        ] (println "turning" turning) (println "dir&track2dir dir:" (get dir&track2dir dir) "track:" track)
    {:pos new-pos
     :dir (if turning
            (-> dir&turn2dir (get dir) (get turn))
            (-> dir&track2dir (get dir) (get track)))
     :turn (if turning (mod (inc turn) 3) turn)} ))

(defn sorted-carts? "Validate result of sort-carts." [row2col2cart]
  (and (sorted? row2col2cart) (every? sorted? (vals row2col2cart)) (every? (comp (partial every? cart?) vals) (vals row2col2cart))))

(defn update-sorted-carts [row2col2cart pos cart validate-place-available]
  {:pre [(sorted-carts? row2col2cart) (coordinates? pos) (or (cart? cart) (nil? cart)) (boolean? validate-place-available)]
   :post [(sorted-carts? %)]}
  (let [row-map (get row2col2cart (:row pos) (sorted-map))
        _ (assert (or (not validate-place-available) (not (row-map (:col pos)))))
        row-map-new ((if cart assoc dissoc) ;nil cart indicates removal. We dissoc, instead of (assoc ... nil). If we put in nil, then sorted-carts? validation would have to filter it out.
                      row-map (:col pos) cart)
        result (assoc row2col2cart (:row pos) row-map-new)
        ;_ (println "sorted-carts" (map type result))
       ]
     result))

(defn flatten-sorted-carts [row2col2cart] {:pre [(sorted-carts? row2col2cart)] :post [(every? cart? %)]}
  (flatten (map (partial map val) (vals row2col2cart))))

(defn move-all "Expects the carts to be sorted already. Move all carts by one step. On success, return an updated sorted-map of sorted-map of (updated) carts. On crash, return coordinates."
 [tracks row2col2cart]
  {:pre [(sorted-carts? row2col2cart)] :post [(or (sorted-carts? %) (coordinates? %))]}
  ;Not using map, because on every step we check for a crash. Hence not: (map (partial move-cart tracks) carts)
  ;Following starts with a full list of carts, instead of empty (as is common with reduce). It updates each cart. That way it can detect the first crash, too.
  (reduce
        (fn [row2col2cart cart]
          (let [moved (move-cart tracks cart)
                pos (:pos moved)]
            (if (-> row2col2cart (get (:row pos)) (get (:col pos))) ;crash?
              (reduced pos) ;crash => stop reducing, return the position
              (let [removed-old  (update-sorted-carts row2col2cart #_old-position=> (:pos cart) nil false)
                    inserted-new (update-sorted-carts removed-old pos moved true)]
                  inserted-new))))
        row2col2cart (flatten-sorted-carts row2col2cart)))

        (defn sort-carts "Sort the carts as they take turns to move (depending on their current positions). Expect no crashes. Return a sorted-map {row => sorted-map { column => cart}}."
         [carts]
         {:pre [(carts? carts)] :post [(sorted-carts? %)]}
              (reduce
                  (fn [row2col2cart {pos :pos :as cart}]
                      {:pre [(sorted-carts? row2col2cart)] :post [(sorted-carts? %)]}
                      (update-sorted-carts row2col2cart pos cart true))
                  (sorted-map)
                  carts))

(defn -main [& args]
  (let [chars (-> args first slurp parse-lines)
        tracks (chars-to-tracks chars)
        height (count chars)
        width  (count (nth chars 0))
        initial-carts (for [row-idx (range 0 height)
                            col-idx (range 0 width)
                            :let [cart-dir (-> chars (nth row-idx) (nth col-idx))]
                            :when (direction-chars cart-dir)]
                            {:pos {:row row-idx, :col col-idx} :dir cart-dir :turn 0})
        _ (println "initial-carts" initial-carts)
        row2col2cart (sort-carts initial-carts)
        find-crash (fn [row2col2cart]
                    {:pre [(sorted-carts? row2col2cart)]} ;(loop) doesn't allow preconditions. That's why we use a separate function.
                    (let [new-row2col2cart-or-crash (move-all tracks row2col2cart)]
                      (println "find-crash: row2col2cart:" row2col2cart)
                      (println "=> new-row2col2cart-or-crash:" new-row2col2cart-or-crash)
                      (if (sorted-carts? new-row2col2cart-or-crash)
                        (recur new-row2col2cart-or-crash)
                        new-row2col2cart-or-crash)))
        crash (find-crash row2col2cart)
        _ (assert (coordinates? crash))
        _ (println "Crash:" crash)
        ])
  )
