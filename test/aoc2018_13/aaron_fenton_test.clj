(ns aoc2018-13.aaron-fenton-test
  (:require  [clojure.test :as t]))

;; ** cart-next-state - Fenton/Aaron
;; :in: a cart, {:col 3 :row 2 :dir \> :next-turn :left}
;; :in: tracks, (see above)
;; :processing: based on the carts current location, direction, next
;; turn, and the track(s) it is on, calculate its next
;; coord/dir/next-turn. 
;; :out: {:col 4 :row 1 :dir :right :next-turn :right} 

;; /--
;; | 
(deftest cart-next-state-tests
  (testing "what is the next state for the cart"
    (is (=
         (let [track [[\/ \- \-]]

               curr-cart {:col 1 :row 0 :dir \> }])


         ))))
