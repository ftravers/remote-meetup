This is for https://adventofcode.com/2018/day/13.

## Glossary

 - "tracks" - used to denote the tracks of the carts, with any intersections/turns, regardless of how we represent it in Clojure. In human terminology, it's a "map", but that would be ambiguous. Let' keep a "map" for Clojure's maps only.

## Main data structures
The input file carries two sets of information:

1. The tracks. That is permanent.
2. The initial position and direction of carts. That's an initial value of our state.

### Naming the cordinates
What's the easiest way to parse the input text file into a matrix? By lines, so the matrix is an array of rows, each row an array of characters (or derivatives). The natural convention for order of dimensions is "x" and "y", hence "x" means "row", "y" means "column".

However, what is the expected output? As "X,Y", but they use "X" for columns and "Y" for rows (see the bottom of the assignment). Doesn't that feel unnatural? Fortunately, it's not used anywhere else other than the expected output. Hence, let's stick to our (natural) convention. When we generate the actual output, we'll swap them.

Here's one more reason to keep the natural coordinates. The carts make their one-place moves in turns. However, that order of the carts is not fixed. Instead, it depends on the actual position of a cart ("carts on the top row move first (acting from left to right), then carts on the second row move (again from left to right)..."). One easy way to represent all the carts is in a 2-dimensional sorted map, indexed by their row and column. That will update their order automatically.

### State
All the state seems to be the carts: their positions, current direction and (relative) direction of their next turn. Can you think of anything else?

* carts: A 2-dimensional `sorted-map` of cart. However, don't use with `assoc-in`. That would create any new inner maps unsorted. The overall map would stay sorted: `(assoc-in (sorted-map) [1 2] :cart-entity-here)` returns a sorted map. But any new inner maps would be unsorted: `( (assoc-in (sorted-map) [1 2] :cart-entity-here) 1)` returns an unsorted map.
* cart: At least a current (absolute) direction, and a (relative) direction of their next turn. Coordinates are not strictly necessary, since you get them as you loop over 2-D map of carts. But having coordinates in cart makes it easy to pass all cart-related data between functions. Hence a map of:
  * position: a `vector` of two non-negative `int`, the first denotes a row `x`, the second indicates a column `y`. That helps when using `get-in` http://clojuredocs.org/clojure.core/get-in to access places: `(get-in 2D-map-of-tracks [3 4])`, or `(get-in 2D-map-of-tracks coordinates)`.
  * direction: A current direction. A `vector` of change of the two respective coordinates. The possible changes are -1, 0 or +1. All possible directions: up `[-1 0]`, right `[0 1]`, down `[1 0]`, left `[0 -1]`.
  * turn: (Relative) direction of the next turn: Left, straight, right (then repeated). It can be an `int` 0..2. At every turn, increase it with `inc`, then modulo 3 with `mod (inc next-turn) 3)`. (0..2 can also serve as an index into an array, which would store any higher structures stored for computation of the absolute direction at the next turn.)

### Representation of directions
There are two types: carts have current directions one way (two horizontal, two vertical). Tracks have directions that are bothdirectional (one horizontal, vertical, two turns). Tracks also have junction.

To represent them visually, CLJ keywords are difficult: `:\` fails, even more fail in CLJS. The simplest way is character literals: `\| \- \/ \\ \+`. That makes processing the input easy, since `(seq string-of-line)` returns a seq. of characters.

## Main tranformations

* `input => vector [x => vector [y => track places]]` - permanent, stored in `tracks`
* `input => sorted-map {x => sorted-map {y => cart}}` = initial state

```
```
