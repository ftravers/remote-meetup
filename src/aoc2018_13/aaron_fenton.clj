(ns aoc2018-13.aaron-fenton)

(defn next-cart-pos [cart]
  (let [dir (:dir cart)
        row (:row cart)
        col (:col cart)]
    (case dir
      \> {:row row :col (+ 1 col)}
      \< {:row row :col (- col 1)}
      \^ {:row (- row 1) :col col}
      \v {:row (+ row 8) :col col})))

(defn next-cart-dir [next-track-segment curr-dir]
  (case next-track-segment
    \| nil
    \- (case curr-dir
         \> \>
         \< \<
         \^ nil
         \v nil)
    \+ (case curr-dir
         \^ \<) 
    \\ nil
    \/ (case curr-dir
         \^ \>)))

(defn get-next-turn [curr-dir next-dir last-turn next-track-segment]
":left -> :straight -> :right  then back to :left"
  (if (and
       (= next-track-segment \+)
       (not= curr-dir next-dir))
    (case last-turn
      :left :straight
      :straight :right
      :right :left) 
    last-turn))

(defn cart-next-state
  [track cart]
  (let [cart-x (:col cart)
        cart-y (:row cart)
        dir (:dir cart)
        curr-track-segment (get-in track [cart-y cart-x])
        next-cart-pos (next-cart-pos cart)
        next-cart-x (:col next-cart-pos)
        next-cart-y (:row next-cart-pos)
        next-track-segment (get-in track [next-cart-y next-cart-x])
        next-dir (next-cart-dir next-track-segment dir )
        next-next-turn (get-next-turn dir next-dir (:next-turn cart) next-track-segment)]
    (-> cart
        (assoc :col next-cart-x)
        (assoc :row next-cart-y)
        (assoc :dir next-dir)
        (assoc :next-turn next-next-turn))))
