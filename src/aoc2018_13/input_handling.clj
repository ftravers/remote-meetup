(ns aoc2018-13.input-handling)

(defn print-and-pass [any] (println (type any) ":\n" any) any)

(defn file-to-chars [file-path] {:post [(vector? %) (every? vector? %) (every? (partial every? char?) %)]}
  (let [;text (slurp file-path)
        ;_ (println text)
        ;lines (clojure.string/split-lines text)
      ]
        (->> file-path slurp clojure.string/split-lines #_print-and-pass (mapv seq) #_print-and-pass (mapv vec))
    ))

(defn format-chars [chars]
  (->> chars
       ;(map (comp print-and-pass #(reduce str "" %))))
       (map #(apply str %))
       (clojure.string/join "\n")
  ))

(def car-direction-to-track {\^ \|, \v \|, \< \-, \> \-})
; Use: (-> (aoc2018-13.input-handling/file-to-chars "/home/pkehl/GIT/remote-meetup/test/aoc2018_13/small-data-1-1.txt") format-chars println)
(defn chars-to-tracks "Input matrix into a matrix of tracks (replacing any carts' initial directions)." [chars]
  (print-and-pass chars)
  (mapv (fn [line]
          (mapv #(get car-direction-to-track % %) line)) chars))
;LEARNING: Have a map-like function for 2D

(def sp "One space character. This makes examples in code more readable." \space)
(def tracks
  [[\/ \- \\]
   [\| sp \|]
   [\| sp \|]])

   (def carts
     [{:col 3 :row 2 :dir \> :next-turn :left}
      {:col 2 :row 1 :dir \v :next-turn :straight}])

      (def tracks-n-carts
      [[\/ \- \\]
       [\| sp \v]
       [\| sp \|]])
