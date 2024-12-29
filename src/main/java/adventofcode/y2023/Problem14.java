package adventofcode.y2023;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.Board;
import adventofcode.commons.Vector;

import java.util.HashMap;
import java.util.Map;

import static adventofcode.commons.Vector.*;

/**
 * Day 14: Parabolic Reflector Dish
 * https://adventofcode.com/2023/day/14
 */
public class Problem14 extends AoCProblem<Long, Problem14> {

    public static void main(String[] args) throws Exception {
        new Problem14().loadResourceAndSolve(false);
    }

    Board<Character> board;

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
        Board<Character> b = tiltBoard(board.clone(), SOUTH);
        return evalTotalLoad(b);
    }

    private long evalTotalLoad(Board<Character> b) {
        long totalLoad = b.forEach((p, v) -> {
            if (v == 'O') return b.M - p.y;
            return 0;
        });
        return totalLoad;
    }

    public Board<Character> tiltBoard(Board<Character> b, Vector dir) {
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

        Map<Board<Character>, Integer> frames = new HashMap<>();
        Board<Character> b = board.clone();

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

    public long simulateTiltCycle(Board<Character> b) {
        tiltBoard(b, SOUTH);
        tiltBoard(b, WEST);
        tiltBoard(b, NORTH);
        tiltBoard(b, EAST);
        return evalTotalLoad(b);
    }

}
