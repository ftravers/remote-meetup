(ns aoc2018-13.aaron-fenton-test
  (:require  [clojure.test :refer :all]
             [aoc2018-13.aaron-fenton :as sut]))

;; ** cart-next-state - Fenton/Aaron
;; :in: a cart, {:col 3 :row 2 :dir \> :next-turn :left}
;; :in: tracks, (see above)
;; :processing: based on the carts current location, direction, next
;; turn, and the track(s) it is on, calculate its next
;; coord/dir/next-turn. 
;; :out: {:col 4 :row 1 :dir :right :next-turn :right} 

;; /--
;; | 
;; {:row 0 :col 1 :dir \>} 

;; /->
;; | 
(deftest cart-next-state-tests
  (testing "what is the next state for the cart"
;; /->
;; | 
    (is  (= {:col 2 :row 0 :dir \> :next-turn :left}
            (let [track [[\/ \- \-]
                         [\| \space \space]]
                  curr-cart {:col 1 :row 0 :dir \> :next-turn :left}]
              (sut/cart-next-state track curr-cart))))
;; /--   ;; >--
;; ^     ;; |
    (is  (= {:col 0 :row 0 :dir \> :next-turn :left}
            (let [track [[\/ \- \-]
                         [\| \space \space]]
                  curr-cart {:col 0 :row 1 :dir \^ :next-turn :left}]
              (sut/cart-next-state track curr-cart))))
;; -+-  ;; -<-
;;  ^   ;;  |
    (is  (= {:col 1 :row 0 :dir \< :next-turn :straight}
            (let [track [[\- \+ \-]
                         [\space \| \space]]
                  curr-cart {:col 1 :row 1 :dir \^ :next-turn :left}]
              (sut/cart-next-state track curr-cart))))

    
    ))
