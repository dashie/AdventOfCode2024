package adventofcode.y2023;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Day 6: Wait For It
 * https://adventofcode.com/2023/day/6
 */
public class Problem06 extends AoCProblem<Long, Problem06> {

    public static void main(String[] args) throws Exception {
        new Problem06().loadResourceAndSolve(false);
    }

    List<Integer> times;
    List<Integer> distances;

    @Override
    public void processInput(AoCInput input) throws Exception {
        // Time:      7  15   30
        // Distance:  9  40  200
        var it = input.iterateLineExs().iterator();
        times = it.next().getListOfInteger("Time:\\s+([0-9 ]+)", "\\s+");
        distances = it.next().getListOfInteger("Distance:\\s+([0-9 ]+)", "\\s+");
    }

    /**
     * ...Determine the number of ways you could beat the
     * record in each race.
     * What do you get if you multiply these numbers together?
     */
    @Override
    public Long solvePartOne() throws Exception {
        // s = v•t
        // v = h               h:  time button hold
        // s = h•(t-h)
        // h•(t-h) > sr        sr: distance of the record
        // -h² + h•t - sr > 0

        long result = 0;
        for (int i = 0; i < times.size(); ++i) {
            int t = times.get(i);
            int sr = distances.get(i);
            long solutionsCount = countSolutions(t, sr);
            result = result == 0 ? solutionsCount : result * solutionsCount;
        }
        return result;
    }

    private static long countSolutions(long t, long sr) {
        double determinant = Math.sqrt(t * t - 4 * sr);
        // "floor" and not "ceil" because we need the integer before first solution
        // and then move the the next one, because disequation is > and not >= and
        // we remove 1 at the end
        long xmin = (int) Math.floor((-t + determinant) / -2.0d) + 1L;
        // "ceil" and not "floor" for the same consideration od above
        long xmax = (int) Math.ceil((-t - determinant) / -2.0d) - 1L;
        long solutionsCount = xmax - xmin + 1L;
        return solutionsCount;
    }

    /**
     * ...How many ways can you beat the record in this
     * one much longer race?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long t = Long.parseLong(times.stream()
            .map(n -> Integer.toString(n))
            .collect(Collectors.joining()));

        long sr = Long.parseLong(distances.stream()
            .map(n -> Integer.toString(n))
            .collect(Collectors.joining()));

        return countSolutions(t, sr);
    }
}
