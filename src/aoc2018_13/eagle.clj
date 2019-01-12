(ns aoc2018-13.eagle)

; The row indices are the first dimension/axis, and column indices are the second.
(defn parse-lines [text]
  (let [lines (clojure.string/split text #"\n")]
    (mapv (comp vec seq) lines)
  )) ;=> 2D vec: [row => vec: column => char]

(def track? #{\| \- \\ \/ \+})
(def dir? #{\^ \v \> \< })
(defn tracks? [matrix]
  (and (vector? matrix) (every? vector? matrix)
       (every? (fn [row] (every? (some-fn track? (partial = \space)) row)) matrix)))

(defn matrix-of-chars-to-tracks "Take output of function parse-lines. Replace any car directions <>v^ with respective track direction." [mx]
  {:pre [(vector? mx) (every? vector? mx) (every? (partial every? char?) mx)]
   :post [(tracks? %)]}
  (mapv (partial mapv {\< \-, \> \-, \v \| \^ \|}) mx))

(defn coordinates? [[x y :as pos]]
            (and (= (count pos) 2) (int? x) (int? y) (< -1 x) (< -1 y) ))

(defn direction-coordinates? [[dx dy :as deltas]]
      (and (= (count deltas) 2) (<= -1 dx 1) (<= -1 dy 1) (not (and (zero? dx) (zero? dy)))))

(defn x2y2cart? [x2y2cart]
  (and (map? x2y2cart) (sorted? x2y2cart) (every? int? (keys x2y2cart))
       (every? (fn [y2cart] (and (map? y2cart) (sorted? y2cart)
                                 (every? int? (keys y2cart))
                                 (every? cart? (vals y2cart))))
               (vals x2y2cart))))



(defn -main [& args]
  (let [matrix-of-chars (-> args first slurp parse-lines)
        tracks (matrix-of-chars-to-tracks matrix-of-chars)
        _ (assert (places? places))
        initial-carts (for [row lines
                            ch row
                            :let [cart-dir (char2cart-dir ch)]
                            :when cart-dir])
        x2y2cart (comp sorted-map seq)
        ])
  )
