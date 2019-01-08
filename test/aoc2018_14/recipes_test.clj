(ns aoc2018-14.recipes-test
  (:require [aoc2018-14.recipes :refer :all]
            [clojure.test :refer :all]))

(deftest digits-tests
  (is (= (digits 10) '(1 0)))
  (is (= (digits 1234) '(1 2 3 4))))

(deftest next-recipes-tests
  (is (= (next-recipes [3 7] [0 1])
         [3 7 1 0]))
  (is (= (next-recipes [3 7 1 0] [0 1])
         [3 7 1 0 1 0]))
  (is (= (next-recipes [3 7 1 0 1 0 1 2 4 5 1 5] [1 6])
         [3 7 1 0 1 0 1 2 4 5 1 5 8])))
