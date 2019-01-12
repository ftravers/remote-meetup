(ns aoc2018-13.eagle)

; The row indices are the first dimension/axis, and column indices are the second.
(defn parse-lines [text]
  (let [lines (clojure.string/split text #"\n")]
    (mapv (comp vec seq) lines)
  )) ;=> 2D vec: [row => vec: column => char]

(def track-chars #{\| \- \\ \/ \+})
(def direction-chars #{\^ \v \> \< })
(defn tracks? [matrix]
  (and (vector? matrix) (every? vector? matrix)
       (every? (fn [row] (every? (some-fn track-chars (partial = \space)) row)) matrix)))

(defn matrix-of-chars-to-tracks "Take output of function parse-lines. Replace any car directions <>v^ with respective track direction." [mx]
  {:pre [(vector? mx) (every? vector? mx) (every? (partial every? char?) mx)]
   :post [(tracks? %)]}
  (mapv (partial mapv {\< \-, \> \-, \v \| \^ \|}) mx))

(defn coordinates? [{:keys [row col]}]
            (every? (every-pred int? (partial < -1)) [row col]))

(defn direction-coordinates? [{:keys [row col]}]
      (and (every? (every-pred int? (partial <= -1) (partial >= 1)) [row col])
           (not (and (zero? row) (zero? col)))))

(defn cart? [{:keys [pos dir turn]}] ;{:pos {:row 2 :col 5} :dir direction-character :turn 0-2 }
  (and (coordinates? pos) (direction-chars dir) (<= 0 turn 2)))

(defn -main [& args]
  (let [matrix-of-chars (-> args first slurp parse-lines)
        tracks (matrix-of-chars-to-tracks matrix-of-chars)
        height (count matrix-of-chars)
        width  (count (matrix-of-chars 0))
        initial-carts (for [row-idx (range 0 height)
                            col-idx (range 0 width)
                            :let [cart-dir (direction-chars (get-in matrix-of-chars [row-idx col-idx]))]
                            :when cart-dir]
                            {:pos {:row row-idx, :col col-idx} :dir cart-dir :turn 0})
        _ (assert (every? cart? initial-carts))
        ])
  )
