package adventofcode.y2023;

import adventofcode.commons.AoCBoard;
import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.AoCVector;

import java.util.HashMap;
import java.util.Map;

/**
 * Day 14: Parabolic Reflector Dish
 * https://adventofcode.com/2023/day/14
 */
public class Problem14 extends AoCProblem<Long, Problem14> {

    public static void main(String[] args) throws Exception {
        new Problem14().loadResourceAndSolve(false);
    }

    AoCBoard<Character> board;

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toCharBoard();
    }


    /**
     * ...Tilt the platform so that the rounded rocks all roll north.
     * Afterward, what is the total load on the north support beams?
     */
    @Override
    public Long solvePartOne() throws Exception {
        AoCBoard<Character> b = tiltBoard(board.clone(), AoCVector.SOUTH);
        return evalTotalLoad(b);
    }

    private long evalTotalLoad(AoCBoard<Character> b) {
        long totalLoad = b.forEach((p, v) -> {
            if (v == 'O') return b.M - p.y;
            return 0;
        });
        return totalLoad;
    }

    public AoCBoard<Character> tiltBoard(AoCBoard<Character> b, AoCVector dir) {
        b.forEach(dir, (p0, v) -> {
            if (v != 'O') return 0;
            var p = p0;
            while (b.get(p, dir, '#') == '.') {
                b.swap(p, p.translate(dir));
                p = p.translate(dir);
            }
            return 1;
        });
        return b;
    }

    /**
     * ...Run the spin cycle for 1000000000 cycles.
     * Afterward, what is the total load on the north support beams?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        int TOTAL_CYCLES = 1000000000;

        Map<AoCBoard<Character>, Integer> frames = new HashMap<>();
        AoCBoard<Character> b = board.clone();

        boolean fastForwardDone = false;
        long totalLoad = -1;
        for (int i = 1; i <= TOTAL_CYCLES; ++i) {
            totalLoad = simulateTiltCycle(b);
            int lastFrame = frames.getOrDefault(b, -1);
            if (lastFrame == -1) {
                frames.put(b.clone(), i); // put a snapshot, because "b" is modified cycle by cycle
            } else if (!fastForwardDone) {
                // log("frame match i=%d, totalLoad=%d%n", i, totalLoad);
                int missingCycles = TOTAL_CYCLES - i; //
                int period = i - lastFrame;
                int remainder = missingCycles % period;
                i = TOTAL_CYCLES - remainder; // fast-forward
                // log("fast-forward to i=%d%n", i);
                fastForwardDone = true;
            }
        }
        return totalLoad;
    }

    public long simulateTiltCycle(AoCBoard<Character> b) {
        tiltBoard(b, AoCVector.SOUTH);
        tiltBoard(b, AoCVector.WEST);
        tiltBoard(b, AoCVector.NORTH);
        tiltBoard(b, AoCVector.EAST);
        return evalTotalLoad(b);
    }

}
