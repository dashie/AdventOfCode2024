package adventofcode.y2023;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;

import java.util.*;

import static java.util.Comparator.comparing;

/**
 * Day 4: Scratchcards
 * https://adventofcode.com/2023/day/4
 */
public class Problem04 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem04().loadResourceAndSolve(false);
    }

    Map<Integer, Card> cards = new HashMap<>();

    class Card {

        int id;
        Set<Integer> wins;
        Set<Integer> numbers;
        int copies = 1;

        Card(int id, Collection<Integer> wins, Collection<Integer> numbers) {
            this.id = id;
            this.wins = new HashSet<>(wins);
            this.numbers = new HashSet<>(numbers);
        }
    }

    @Override
    public void processInput(AoCInput input) throws Exception {
        // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        for (LineEx line : input.iterateLineExs()) {
            int id = line.match("Card\\s+([0-9]+):").getInt(1);
            List<Integer> wins = line.getListOfIntegers(": ([0-9 ]+) \\|", "\\s+");
            List<Integer> numbers = line.getListOfIntegers(" \\| ([0-9 ]+)$", "\\s+");
            cards.put(id, new Card(id, wins, numbers));
        }
    }

    /**
     * ...Take a seat in the large pile of colorful cards.
     * How many points are they worth in total?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = 0;
        for (Card card : cards.values()) {
            long n = countMatches(card);
            // points 1, 2, 4, 8
            if (n > 0) result += 1 << (n - 1);
        }
        return result;
    }

    private long countMatches(Card card) {
        Set<Integer> matches = new HashSet<>(card.numbers);
        matches.retainAll(card.wins);
        if (matches.isEmpty()) return 0;
        return matches.size();
    }

    /**
     * ...Process all of the original and copied scratchcards
     * until no more scratchcards are won. Including the original
     * set of scratchcards, how many total scratchcards do you end
     * up with?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        for (Card card : cards.values().stream().sorted(comparing(a -> a.id)).toList()) {
            long n = countMatches(card);
            if (n > 0) {
                for (int i = 0; i < n; ++i) {
                    Card next = cards.get(card.id + i + 1);
                    if (next != null) next.copies += card.copies;
                }
            }
        }
        return cards.values().stream().mapToLong(c -> c.copies).sum();
    }
}
