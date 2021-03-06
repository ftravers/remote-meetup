(ns aoc2018-12.next-generation-test
  (:require  [clojure.test :refer :all]
             [aoc2018-12.next-generation :as sut]))

(deftest plant-left-shift-tests
  (testing "how far left 0, 1 or 2 spaces to grow the plant vector"
    (is (= 0 (sut/plant-left-shift [false false true])))
    (is (= 1 (sut/plant-left-shift [false true true])))
    (is (= 2 (sut/plant-left-shift [true false true])))))

(deftest trim-left-tests
  (testing "how many pots to drop from left of plant vector"
    (is (= [true false true] (sut/trim-left [true false true])))
    (is (= [true false true] (sut/trim-left [false true false true])))
    (is (= [true false true] (sut/trim-left [false false true false true])))
    (is (= [false true false true] (sut/trim-left [false false false true false true])))))

(deftest trim-up-to-2-false-each-side-tests
  (testing "trimming false from ends of vector"
    (let [plants [false false true false false]]
      (is (= {:plants [true]
              :grow-left 0}
             (sut/trim-up-to-2-false-each-side plants))))
    (let [plants [false true true false false]]
      (is (= {:plants [true true]
              :grow-left 1}
             (sut/trim-up-to-2-false-each-side plants))))
    (let [plants [true true true false false]]
      (is (= {:plants [true true true]
              :grow-left 2}
             (sut/trim-up-to-2-false-each-side plants))))
    (let [plants [false true true false true]]
      (is (= {:plants [true true false true]
              :grow-left 1}
             (sut/trim-up-to-2-false-each-side plants))))))

(deftest get-neighbours-tests
  (testing "getting neighbours for a given index"
    (is (= [false false true false false]
           (let [plants [true false false]
                 ndx 0]
             (sut/get-neighbours ndx plants))))
    (is (= [false true false false false]
           (let [plants [true false false]
                 ndx 1]
             (sut/get-neighbours ndx plants))))

    (is (= [true false true false false]
           (let [plants [true false true]
                 ndx 2]
             (sut/get-neighbours ndx plants))))))

(def gen-0-str "#..#.#..##......###...###")

(def later-gens-str
  ["...#..#.#..##......###...###..........."
   "...#...#....#.....#..#..#..#..........."
   "...##..##...##....#..#..#..##.........."
   "..#.#...#..#.#....#..#..#...#.........."
   "...#.#..#...#.#...#..#..##..##........."
   "....#...##...#.#..#..#...#...#........."
   "....##.#.#....#...#..##..##..##........"
   "...#..###.#...##..#...#...#...#........"
   "...#....##.#.#.#..##..##..##..##......."
   "...##..#..#####....#...#...#...#......."
   "..#.#..#...#.##....##..##..##..##......"
   "...#...##...#.#...#.#...#...#...#......"
   "...##.#.#....#.#...#.#..##..##..##....."
   "..#..###.#....#.#...#....#...#...#....."
   "..#....##.#....#.#..##...##..##..##...."
   "..##..#..#.#....#....#..#.#...#...#...."
   ".#.#..#...#.#...##...#...#.#..##..##..."
   "..#...##...#.#.#.#...##...#....#...#..."
   "..##.#.#....#####.#.#.#...##...##..##.."
   ".#..###.#..#.#.#######.#.#.#..#.#...#.."
   ".#....##....#####...#######....#.#..##."])

(def rules-str
  ["...## => #"
   "..#.. => #"
   ".#... => #"
   ".#.#. => #"
   ".#.## => #"
   ".##.. => #"
   ".#### => #"
   "#.#.# => #"
   "#.### => #"
   "##.#. => #"
   "##.## => #"
   "###.. => #"
   "###.# => #"
   "####. => #"])

(deftest parse-all-rules-tests
  (testing "convert a list of rules in # and . form to hashmap keyed
  by true/false vectors"
    (is (= true
           (get (sut/parse-all-rules rules-str)
                [false false false true true])))
    ;Following results in nil, but only with our test input above.
    ;Real input will have complete 32 rules, hence any rule input
    ;will map to either true or false.
    (is (= nil
           (get (sut/parse-all-rules rules-str)
                [false false false false false])))
    #_(is (= {[false false false false false] false} ;TODO
           (sut/parse-all-rules ["..... => ."])))))

(deftest next-generation-tests
  (testing "The creation of the next generation"
    (let [
          ;; input plants: "."
          ;; need to pad 4 empty pots on either side
          ;; to find neighbors of -2 to +2.
          ;; process from index -2 to +2
          ;;  43210 1234
          ;; |--v--|----
          ;; "..... ...." gen0,step0
          ;; "..#.. ...." gen1,step0

          ;;  4 32101 234
          ;;  -|--v--|---
          ;; ". ..... ..." gen0,step1
          ;; ". .##.. ..." gen1,step1

          ;;  43 21012 34
          ;;  --|--v--|--
          ;; ".. ..... .." gen0,step2
          ;; ".. ###.. .." gen1,step2

          ;;  432 10123 4
          ;;  ---|--v--|-
          ;; "... ..... ." gen0,step3
          ;; "..# ###.. ." gen1,step3

          ;;  4321 01234
          ;;  ----|--v--|
          ;; ".... ..... " gen0,step4
          ;; "..## ###.. " gen1,step4

          ;; initial vector length: 1
          ;; # of steps 1 + 4

          rules-str ["..... => #"]
          rules (sut/parse-all-rules rules-str)
          gen-0-str "."
          gen-0-pseudo-str "........."
          gen-0 (sut/convert-hash-2-true gen-0-str)
          should-be-gen-1 (sut/convert-hash-2-true "#####")
          is-gen-1 (sut/next-generation gen-0 rules)]
      (is (= should-be-gen-1 is-gen-1)))
    ;; (is (= (sut/convert-hash-2-true (later-gens-str 1))
    ;;      (let [rules (sut/parse-all-rules rules-str)
    ;;            gen-0 (sut/convert-hash-2-true gen-0-str)]
    ;;        (sut/next-generation gen-0 rules))))
    )
  (testing "The creation of the next generation"
    (let [rules (sut/parse-all-rules rules-str)
          gen-0-str "#..#.#."
          gen-0 (sut/convert-hash-2-true gen-0-str)
          should-be-gen-1 (sut/convert-hash-2-true "#...#..")
          is-gen-1 (sut/next-generation gen-0 rules)]
      (is (= should-be-gen-1 is-gen-1)))
    ;; (is (= (sut/convert-hash-2-true (later-gens-str 1))
    ;;      (let [rules (sut/parse-all-rules rules-str)
    ;;            gen-0 (sut/convert-hash-2-true gen-0-str)]
    ;;        (sut/next-generation gen-0 rules))))
    ))

(deftest parse-rule-tests
  (testing "given a string rule, convert to true/false vector"
    (is (= [false false false true true]
           (let [string-rule "...## => #"]
             (sut/parse-rule string-rule))))))

(deftest convert-hash-2-true-tests
  (testing "given a string of hashes (#) and dots (.), convert to true and false"
    (is (= [false false true false false true]
           (let [input-plants "..#..#"]
             (sut/convert-hash-2-true input-plants))))))
