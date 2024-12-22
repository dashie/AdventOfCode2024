package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.*;

import static java.lang.Long.parseLong;

/**
 * Day 11: Plutonian Pebbles
 * https://adventofcode.com/2024/day/11
 */
public class Problem11 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem11().loadResourceAndSolve(false);
    }

    private List<Long> data0;

    @Override
    public void processInput(AoCInput input) throws Exception {
        data0 = input.toListOfLongList(" ").getFirst();
    }

    /**
     * ...Consider the arrangement of stones in front of you.
     * How many stones will you have after blinking 25 times?
     */
    @Override
    public Long solvePartOne() throws Exception {
        List<Long> data = new LinkedList<>(data0);
        long round = 25;
        while (round > 0) {
            ListIterator<Long> i = data.listIterator(data.size());
            while (i.hasPrevious()) {
                long n = i.previous();
                long[] newStones = blink(n);
                i.set(newStones[0]);
                if (newStones.length > 1) {
                    i.next();
                    i.add(newStones[1]);
                    i.previous();
                    i.previous();
                }
            }
            round--;
        }
        return (long) data.size();
    }

    /**
     * - If the stone is engraved with the number 0,
     *   it is replaced by a stone engraved with the number 1.
     * - If the stone is engraved with a number that has an even number of digits,
     *   it is replaced by two stones. The left half of the digits are engraved on the
     *   new left stone, and the right half of the digits are engraved on the new right stone.
     *   (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)
     * - If none of the other rules apply, the stone is replaced by a new stone;
     *   the old stone's number multiplied by 2024 is engraved on the new stone.
     */
    private long[] blink(long n) {
        if (n == 0) {
            return new long[]{1L};
        } else if (Long.toString(n).length() % 2 == 0) {
            String ns = Long.toString(n);
            long n1 = parseLong(ns.substring(0, ns.length() / 2));
            long n2 = parseLong(ns.substring(ns.length() / 2));
            return new long[]{n1, n2};
        } else {
            return new long[]{n * 2024L};
        }
    }

    /**
     * ...How many stones would you have after blinking a total of 75 times?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        int round = 75;
        long result = 0;
        for (long n : data0) {
            result += count(n, round);
        }
        return result;
    }

    private Map<String, Long> countCache = new HashMap<>();

    private long count(long n, int round) {
        if (round <= 0) return 1; // end of rounds

        // looking at the output I can see repeated numbers, so I try to cache the results
        long cachedCount = countCache.getOrDefault(n + "-" + round, 0L);
        if (cachedCount > 0) {
            return cachedCount;
        }

        long result = 0;
        try {
            long[] newStones = blink(n);
            result = count(newStones[0], round - 1);
            if (newStones.length > 1) {
                result += count(newStones[1], round - 1);
            }
            return result;
        } finally {
            countCache.put(n + "-" + round, result);
        }
    }
}
