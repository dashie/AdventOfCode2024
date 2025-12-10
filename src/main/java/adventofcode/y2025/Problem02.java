package adventofcode.y2025;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Day 2: Gift Shop
 * https://adventofcode.com/2025/day/2
 */
public class Problem02 extends AoCProblem<Long, Problem02> {

    public static void main(String[] args) throws Exception {
        new Problem02().loadResourceAndSolve(false);
    }

    private static final long DIAL_SIZE = 100;
    private List<String[]> ranges = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        input.splitInput(",").forEach(r -> {
            ranges.add(r.split("-").toArray(String[]::new));
        });
    }

    /**
     * ...Adding up all the invalid IDs in this example
     * produces N.
     *
     * What do you get if you add up all of the invalid IDs?
     */
    @Override
    public Long solvePartOne() throws Exception {
        HashSet<Long> ids = new HashSet<>();
        long result = 0;
        for (var r : ranges) {
            int patternLength = r[0].length() / 2 + (r[0].length() % 2);
            int count = 2;
            result += findInvalidIDs(r, patternLength, count, ids);
        }
        return result;
    }

    private long findInvalidIDs(String[] range, int patternLength, int count, HashSet<Long> ids) {
        if (count < 2)
            return 0;

        int prefixlen = patternLength * count - range[0].length();
        if (prefixlen < 0)
            return 0;

        var pattern = "?";
        if (prefixlen > 0) {
            pattern = "1" + "0".repeat(patternLength - 1);
        } else {
            pattern = range[0].substring(0, patternLength);
        }

        var low = Long.parseLong(range[0]);
        var high = Long.parseLong(range[1]);

        long result = 0;
        var n = Long.parseLong(pattern.repeat(count));
        while (n <= high) {
            if (n >= low) {
                if (ids.add(n)) {
                    result += n;
                }
            }
            pattern = Long.toString(Long.parseLong(pattern) + 1);
            n = Long.parseLong(pattern.repeat(count));
        }

        return result;
    }

    /**
     * ...Adding up all the invalid IDs in this
     * example produces N.
     *
     * What do you get if you add up all of the invalid IDs
     * using these new rules?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        HashSet<Long> ids = new HashSet<>();
        long result = 0;
        for (var r : ranges) {
            int patternLength = r[0].length() / 2 + (r[0].length() % 2);
            while (patternLength > 0) {
                int count = r[1].length() / patternLength;
                while (count > 1) {
                    result += findInvalidIDs(r, patternLength, count, ids);
                    count--;
                }
                patternLength--;
            }
        }
        return result;
    }
}
