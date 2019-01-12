(ns aoc2018-13.input-handling)

(defn file-to-chars [file-path] {:post [(vector? %) (every? vector? %) (every? (partial every? char?) %)]}
  (let [;text (slurp file-path)
        ;_ (println text)
        ;lines (clojure.string/split-lines text)
        print-and-pass #(do (println %) %)
      ]
        (->> file-path slurp clojure.string/split-lines #_print-and-pass (mapv seq) #_print-and-pass (mapv vec))
    ))

(defn format-chars [chars]
  (->> chars
       (map (comp #(str % "\n")))
       (apply str))
  )

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
