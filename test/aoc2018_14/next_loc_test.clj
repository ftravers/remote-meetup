(ns aoc2018-14.next-loc-test
  (:require [aoc2018-14.next-loc :as sut]
            [clojure.test :refer :all ]))

(deftest finished?-tests
  (testing ""
    (is (= [1] 
           (sut/finished? [3 7] [3 7 1 0] 1)))
    (is (= nil
           (sut/finished? [3 7] [3 7 1 0] 3)))))
 
(deftest next-location-tests
  (testing ""
    (is (let [recipes [3 7 1 0]
              locations [0 1]
              next-locations [0 1]]
          (= next-locations (sut/next-location recipes locations))))
    (is (let [recipes [3 7 1 0 1 0 1]
              locations [4 3]
              next-locations [6 4]]
          (= next-locations (sut/next-location recipes locations))))
    (is (let [recipes [3 7 1 0 1 0 1 2]
              locations [6 4]
              next-locations [0 6]]
          (= next-locations (sut/next-location recipes locations))))
    ))

;; paren () is the first location
;; bracket [] is the second location
 ;; 3  7  1  0 [1] 0 (1)
(deftest one-elf-next-loc-tests
  (testing "that the next location of the elves is correct"
    (is (let [ ;; recipes [3 7 1 0 1 0 1]
              ;; locations [4 3]
              ;; 3  7  1 [0](1) 0 1 <-- old locations 
              ;; 3  7  1  0 [1] 0 (1) <--- new locations
              jump 2
              recipe-length 7
              curr-loc 4
              a-new-loc 6]
          (= a-new-loc (sut/one-elf-next-loc recipe-length curr-loc jump))))
    (is (let [ ;; recipes [2 1 3]
              ;; locations [0 1]
              jump 3
              recipe-length 3
              curr-loc 0
              a-new-loc 0]
          (= a-new-loc (sut/one-elf-next-loc recipe-length curr-loc jump))))
    (is (let [ ;; recipes [3 7 1. 0,]
              ;; locations [0 1]
              jump 4
              recipe-length 4
              curr-loc 0
              a-new-loc 0]
          (= a-new-loc (sut/one-elf-next-loc recipe-length curr-loc jump))))
    (is (let [ ;; recipes [3. 7, 1 0]
              ;; locations [0 1]
              jump 8
              recipe-length 4
              curr-loc 1
              a-new-loc 1]
          (= a-new-loc (sut/one-elf-next-loc recipe-length curr-loc jump))))))
