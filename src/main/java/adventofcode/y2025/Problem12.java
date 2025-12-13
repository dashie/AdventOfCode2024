package adventofcode.y2025;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Day 12: Christmas Tree Farm
 * https://adventofcode.com/2025/day/12
 */
public class Problem12 extends AoCProblem<Long, Problem12> {

    public static void main(String[] args) throws Exception {
        new Problem12().loadResourceAndSolve(false);
    }

    record Region(long w, long h, List<Integer> presents) {}

    private List<Integer> shapeAreas = new ArrayList<>();
    private List<Region> regions = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        Iterator<LineEx> it = input.iterateLineExs().iterator();
        var index = 0;
        while (index < 6) {
            LineEx line = it.next();
            var area = 0;
            for (int i = 0; i < 3; ++i) {
                line = it.next();
                area += line.toString().replaceAll("[^#]", "").length();
            }
            shapeAreas.add(area);
            it.next();
            index++;
        }

        while (it.hasNext()) {
            LineEx line = it.next();
            var m = line.match("([0-9]+)x([0-9]+): (.*)");
            var w = m.getInt(1);
            var h = m.getInt(2);
            var presents = Arrays.stream(m.get(3).split(" ")).map(Integer::parseInt).toList();
            regions.add(new Region(w, h, presents));
        }
    }

    /**
     * ...Consider the regions beneath each tree and the presents
     * the Elves would like to fit into each of them.
     * How many of the regions can fit all of the presents listed?
     *
     * NOTE:
     * Solved with an approximation algorithm, after learning that
     * this problem hid Eric’s prank for Christmas 2025.
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = 0;
        for (var r : regions) {
            var area = r.w * r.h;
            for (int i = 0; i < r.presents.size(); ++i) {
                var p = r.presents.get(i);
                area -= shapeAreas.get(i) * p;
            }
            if (area >= 0)
                result++;
        }
        return result;
    }
}
