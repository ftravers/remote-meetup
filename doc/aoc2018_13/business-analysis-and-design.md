This is for https://adventofcode.com/2018/day/13.

## Glossary

 - "tracks" - used to denote the tracks of the carts, with any intersections/turns, regardless of how we represent it in Clojure. In human terminology, it's a "map", but that would be ambiguous. Let' keep a "map" for Clojure's maps only.

## Main data structures
The input file carries two sets of information:

1. The tracks. That is permanent.
2. The initial position and direction of carts. That's an initial value of our state.

### Naming the cordinates
What's the easiest way to parse the input text file into a matrix? By lines, so the matrix is an array of rows, each row an array of characters (or derivatives):

```
(defn parse-lines [text]
  (let [lines (clojure.string/split text #"\n")]
    (mapv (comp vec seq) lines)
  )) ;=> 2D vec: [row dimension => vec: column dimension => char]
```

When representing carts, we chose a diagram/graph notation, where "x" denotes a column, "y" indicates a row. Hence, when accessing the tracks, don't pass the coordinates as `[x y]` (). Remember that the first coordinate is a row, the second is the column (hence pass them as `[y x]`).

### State
All the state seems to be the carts: their positions, current direction and (relative) direction of their next turn. Can you think of anything else?

* carts: a `seq` of cart

* cart: Coordinates, a current (absolute) direction, and a (relative) direction of their next turn.

  * `x` (column) and `y` (row). Or a vector `[column row]`.
  * direction: A current direction. A `vector` of change of the two respective coordinates. The possible changes are -1, 0 or +1. All possible directions: up `[-1 0]`, right `[0 1]`, down `[1 0]`, left `[0 -1]`.
  * turn: (Relative) direction of the next turn: Left, straight, right (then repeated). It can be an `int` 0..2. At every turn, increase it with `inc`, then modulo 3 with `mod (inc next-turn) 3)`. (0..2 can also serve as an index into an array, which would store any higher structures stored for computation of the absolute direction at the next turn.)

### Symbolic representation of directions
There are two types: carts have current directions one way (two horizontal, two vertical). Tracks have directions that are bothdirectional (one horizontal, vertical, two turns). Tracks also have junction.

To represent them visually, CLJ keywords are difficult: `:\` fails, even more fail in CLJS. The simplest way is character literals: `\| \- \/ \\ \+`. That makes processing the input easy, since `(seq string-of-line)` returns a seq. of characters.

## Main tranformations

* `input => vector [x => vector [y => track places]]` - permanent, stored in `tracks`
* `input => sorted-map {x => sorted-map {y => cart}}` = initial state

#### Order in which cars make their moves
The carts make their one-place moves in turns. However, that order of the carts is not fixed. Instead, it depends on the actual position of a cart ("carts on the top row move first (acting from left to right), then carts on the second row move (again from left to right)..."). One easy way to represent all the carts is in a 2-dimensional `sorted-map`, indexed first by their row, then by column. If you iterate over it (for example, in `for` with two iteration levels), you'll get them in the same order as they take turns.

However, don't use with `assoc-in`. That would create any new inner maps unsorted. The overall map would stay sorted: `(assoc-in (sorted-map) [1 2] :cart-entity-here)` returns a sorted map. But any new inner maps would be unsorted: `( (assoc-in (sorted-map) [1 2] :cart-entity-here) 1)` returns an unsorted map.
