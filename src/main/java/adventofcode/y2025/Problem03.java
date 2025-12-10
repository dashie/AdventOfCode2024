package adventofcode.y2025;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.Arrays;

/**
 * Day 3: Lobby
 * https://adventofcode.com/2025/day/3
 */
public class Problem03 extends AoCProblem<Long, Problem03> {

    public static void main(String[] args) throws Exception {
        new Problem03().loadResourceAndSolve(false);
    }

    private int[][] banks = null;

    @Override
    public void processInput(AoCInput input) throws Exception {
        banks = input.toIntMatrix();
    }

    /**
     * ...The total output joltage is the sum of the maximum joltage
     * from each bank, so in this example, the total output joltage
     * is 98 + 89 + 78 + 92 = 357.
     *
     * There are many batteries in front of you. Find the maximum
     * joltage possible from each bank; what is the total output
     * joltage?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = 0;
        for (int b = 0; b < banks.length; ++b) {
            long digits[] = {0, 0};
            for (int i = 0; i < banks[b].length; ++i) {
                long n = banks[b][i];
                if (digits[1] > digits[0]) {
                    digits[0] = digits[1];
                    digits[1] = n;
                } else if (n > digits[1]) {
                    digits[1] = n;
                }
            }
            long max = digits[0] * 10 + digits[1];
            result += max;
        }
        return result;
    }

    /**
     * ...The total output joltage is now much larger:
     * 987654321111 + 811111111119 + 434234234278 + ... = N.
     *
     * What is the new total output joltage?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long result = 0;
        for (int b = 0; b < banks.length; ++b) {
            long digits[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            for (int i = 0; i < banks[b].length; ++i) {
                long n = banks[b][i];
                if (shiftDigits(digits)) {
                    digits[digits.length - 1] = n;
                } else if (n > digits[digits.length - 1]) {
                    digits[digits.length - 1] = n;
                }
            }

            long max = 0;
            for (int i = 0; i < digits.length; ++i) {
                max = max * 10 + digits[i];
            }
            result += max;
        }
        return result;
    }

    private boolean shiftDigits(long[] digits) {
        for (int i = 0; i < digits.length - 1; ++i) {
            if (digits[i] < digits[i + 1]) {
                for (int s = i; s < digits.length - 1; ++s)
                    digits[s] = digits[s + 1];
                return true;
            }
        }
        return false;
    }
}
