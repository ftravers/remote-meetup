For https://adventofcode.com/2018/day/12.

### Business Analysis
## Summary of user story
Elves grow plants in pots, which are in a row. Plants come to life and die over generations. That depends on the previous state of a pot (whether with a plant or not), and the state of its four neighbours: two to the left, two to the right. The rules apply equally to all pots.

## Details and interpretation
Input (logical, not the exact formatting of the input file):
- String of characters, either a hash # or a dot. They represent whether whether a pot does (#) or does not (.) currently contain a plant. That gives an initial state. For example, #..##.... indicates that pots 0, 3, and 4 currently contain plants.
The initial state begins at pot 0 and has a given width. Over generations, the state may expand to the left (negative indices) and to the right.

- Rules ("notes") on how these plants spread to nearby pots.
The example input lists "producing" roles only. However, your input includes all possible combinations (total of 2 to power of 5 = 32 rules).

When applying rules to the two leftmost, or two rightmost, pots, imagine adding extra empty pots to the side. In other words, the rules apply to the leftmost (and the second leftmost) pot, as if there were two (or one, respectively) empty pots to their left. In the same way, the rules apply to the rightmost (and the second rightmost) pots, as if there were two (or one, respectively) empty pots to their right.

### Data structures
## Whether a pot contains a plant: Keyword, character or boolean?
The code will frequently branch/depend on whether a pot contains a plan, or not. Hence the code will be more brief = focusing on the work, if we use boolean. We can easily parse the input, and transform back for debugging printout.
We can still have human-friendly, visual, representation in tests. We parse it in and transform it out.

## Pot
Pot entity doesn't need to carry its position, since pots don't move around. When applying the rules, we operate on subsequences of five neighbouring pots, independent of the absolute indices. Hence a pot can be a boolean (indicating presence of a plant).

## Generation
Not a simple vector of booleans on their own, since vectors can't have negative indices.
The easiest: A vector (or even just a seq) of booleans, and a number representing the "pot index" of the leftmost pot (zero or negative).
Human-friendly test input/output representation: a string of hashes and dots, and a numeric index of the leftmost pot. If we want to print several generations, we take the last generation's length and its index of the first pot. When printing the previous generations, fill in any missing leftmost/rightmost pots with a dot.

## Five neighbouring pots
We want this to be a structure that will easily identify which rule applies to its central (third) pot. The rules apply the same regardless of absolute positions. Therefore use a vector of five booleans.

## Rules
A map {[five booleans] boolean, ...}. Keys represent the five neighbour pots, values represent the next generation value of the central plot.
Using a map prevents duplicate rules.
Ensure we have 32 rules.

## Applying the rules
When handling the three leftmost, or the three rightmost, pots, inject the missing pots as false (no plant).
Leave any leftmost/rightmost pots in, even if they turn from true (#) to false (.). That's so we can format debugging output.

A BA sketch: (BA_sketch.jpg)
