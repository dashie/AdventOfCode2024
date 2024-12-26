package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.List;

import static java.lang.Long.parseLong;

/**
 * Day 7: Bridge Repair
 * https://adventofcode.com/2024/day/7
 */
public class Problem07 extends AoCProblem<Long, Problem07> {

    public static void main(String[] args) throws Exception {
        new Problem07().loadResourceAndSolve(false);
    }

    private List<long[]> paramsList;

    @Override
    public void processInput(AoCInput input) throws Exception {
        paramsList = input.toListOfLongArray(":\s|\s");
    }

    /**
     *
     */
    @FunctionalInterface
    interface Operator {

        long apply(long tmp, long n);
    }

    static Operator ADD = (tmp, n) -> tmp + n;
    static Operator MUL = (tmp, n) -> tmp * n;
    static Operator CAT = (tmp, n) -> parseLong("%s%s".formatted(tmp, n));

    /**
     * ...The engineers just need the total calibration result, which is the
     * sum of the test values from just the equations that could possibly be true.
     * In the above example, the sum of the test values for the three equations
     * listed above is 3749.
     *
     * Determine which equations could possibly be true.
     * What is their total calibration result?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = 0;
        for (long[] params : paramsList) {
            if (isSolvable(params, List.of(ADD, MUL))) {
                result += params[0];
            }
        }
        return result;
    }

    private boolean isSolvable(long[] params, List<Operator> operators) {
        return solve(params, 1, 0, params[0], operators);
    }

    private boolean solve(long[] params, int index, long tmp, long expected, List<Operator> operators) {
        if (tmp > expected) return false;
        if (index == params.length) {
            return tmp == expected;
        }

        long n = params[index];
        if (index == 1) {
            return solve(params, index + 1, n, expected, operators);
        } else {
            for (Operator operator : operators) {
                long result = operator.apply(tmp, n);
                if (solve(params, index + 1, result, expected, operators))
                    return true;
            }
            return false;
        }
    }

    /**
     * ...Using your new knowledge of elephant hiding spots,
     * determine which equations could possibly be true.
     * What is their total calibration result?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long result = 0;
        for (long[] params : paramsList) {
            if (isSolvable(params, List.of(ADD, MUL, CAT))) {
                result += params[0];
            }
        }
        return result;
    }
}
