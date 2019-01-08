(ns aoc2018-14core-test
  (:require [clojure.test :refer :all]
            [aoc2018-14core :as c]))

(deftest next-game-state-tests
  (testing "progress game state correctly"
    (is (let [curr-game-state {:recipes [3 7]
                               :locations [0 1]}
              next-game-state {:recipes [3 7 1 0]
                               :locations [0 1]}]))
    ))
