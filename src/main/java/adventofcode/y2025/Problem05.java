package adventofcode.y2025;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

/**
 * Day 5: Cafeteria
 * https://adventofcode.com/2025/day/5
 */
public class Problem05 extends AoCProblem<Long, Problem05> {

    public static void main(String[] args) throws Exception {
        new Problem05().loadResourceAndSolve(false);
    }

    private TreeMap<Long, Long> freshRanges = new TreeMap();
    private List<Long> ingredients = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        List<long[]> sortedRanges = input.before("\n\n")
            .toListOfLongArray("\\-");
        sortedRanges.sort(Comparator.comparingLong(a -> a[0]));

        for (var range : sortedRanges) {
            var overlap = freshRanges.floorEntry(range[0]);
            if (overlap == null || overlap.getValue() < range[0]) {
                freshRanges.put(range[0], range[1]);
            } else {
                // log("overlap: %s=%s %s%n", range[0], range[1], overlap);
                freshRanges.put(overlap.getKey(), Math.max(overlap.getValue(), range[1]));
            }
        }

        for (var line : input.after("\n\n").iterateLineExs())
            ingredients.add(line.getLong());
    }

    /**
     * ...Process the database file from the new inventory
     * management system. How many of the available ingredient
     * IDs are fresh?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = 0;
        for (var id : ingredients) {
            var range = freshRanges.floorEntry(id);
            boolean fresh = range != null
                && id >= range.getKey()
                && id <= range.getValue();

            if (fresh)
                result++;
        }
        return result;
    }

    /**
     * ...
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long result = 0;
        for (var r : freshRanges.entrySet()) {
            result += r.getValue() - r.getKey() + 1;
        }
        return result;
    }
}
