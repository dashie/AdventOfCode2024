package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;

/**
 * Day 11: Plutonian Pebbles
 * https://adventofcode.com/2024/day/11
 */
public class Problem11v2 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem11v2().solve(false);
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
    public Long partOne() throws Exception {
        return count(25);
    }

    private long count(int round) {
        Map<Long, Long> bag = toBag(data0);
        while (round > 0) {
            Map<Long, Long> bagAfterBlink = new HashMap<>(bag.size() * 2);
            bag.forEach((n, count) -> {
                for (var newStone : blink(n)) {
                    bagAfterBlink.compute(newStone, (k, v) -> v == null ? count : v + count);
                }
            });
            bag = bagAfterBlink;
            round--;
        }
        return bag.values().stream().mapToLong(n -> n).sum();
    }

    public Map<Long, Long> toBag(List<Long> data) {
        return data.stream().collect(Collectors.groupingBy(n -> n, Collectors.counting()));
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
    public Long partTwo() throws Exception {
        return count(75);
    }
}
