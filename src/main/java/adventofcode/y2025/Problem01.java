package adventofcode.y2025;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.ArrayList;
import java.util.List;

/**
 * Day 1: Secret Entrance
 * https://adventofcode.com/2025/day/1
 */
public class Problem01 extends AoCProblem<Long, Problem01> {

    public static void main(String[] args) throws Exception {
        new Problem01().loadResourceAndSolve(false);
    }

    private static final long DIAL_SIZE = 100;
    private List<Long> moves = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        input.pattern("([LR])(\\d+)").forEach(m -> {
            long move = switch (m.getChar(1, ' ')) {
                case 'L' -> -m.getLong(2);
                default -> m.getLong(2);
            };
            moves.add(move);
        });
    }

    /**
     * ...Because the dial points at 0 a total of three times
     * during this process, the password in this example is 3.
     *
     * Analyze the rotations in your attached document.
     * What's the actual password to open the door?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = 0;
        long p = 50;
        for (var m : moves) {
            p = (p + m + DIAL_SIZE) % DIAL_SIZE;
            if (p == 0) result++;
        }
        return result;
    }

    /**
     * ...Be careful: if the dial were pointing at 50, a single
     * rotation like R1000 would cause the dial to point at 0
     * ten times before returning back to 50!
     *
     * Using password method 0x434C49434B, what is the password
     * to open the door?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long result = 0;
        long p = 50;
        for (var m : moves) {
            result += Math.abs(m / DIAL_SIZE);
            m = m % DIAL_SIZE;
            if (m > 0) {
                p += m;
                if (p >= DIAL_SIZE) result++;
                p = p % DIAL_SIZE;
            } else if (m < 0) {
                long prev = p;
                p += m;
                if ( prev != 0 && p <= 0) result++;
                p = (p + DIAL_SIZE) % DIAL_SIZE;
            } else {
                if (p == 0) result++; // stay on 0
            }
        }
        return result;
    }
}
