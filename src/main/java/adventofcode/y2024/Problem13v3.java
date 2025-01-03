package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;
import adventofcode.commons.MatcherEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.*;

/**
 * Day 13: Claw Contraption
 * https://adventofcode.com/2024/day/13
 *
 * Solve using linear search.
 */
public class Problem13v3 extends AoCProblem<Long, Problem13v3> {

    public static void main(String[] args) throws Exception {
        new Problem13v3().loadResourceAndSolve(false);
    }

    record Machine(long prizeX, long prizeY, long[][] buttons) {

        @Override
        public String toString() {
            String bstr = "[%s,%s]".formatted(Arrays.toString(buttons[0]), Arrays.toString(buttons[1]));
            return "%d,%d %s".formatted(prizeX, prizeY, bstr);
        }
    }

    public List<Machine> machines = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {

        Iterator<LineEx> it = input.iterateLineExs().iterator();
        while (it.hasNext()) {
            MatcherEx m;
            m = it.next().match("X([+-][0-9]+), Y([+-][0-9]+)");
            long ax = m.getLong(1);
            long ay = m.getLong(2);
            m = it.next().match("X([+-][0-9]+), Y([+-][0-9]+)");
            long bx = m.getLong(1);
            long by = m.getLong(2);
            m = it.next().match("X=([0-9]+), Y=([0-9]+)");
            long x = m.getLong(1);
            long y = m.getLong(2);
            machines.add(new Machine(x, y, new long[][]{{ax, ay}, {bx, by}}));
            if (it.hasNext()) it.next(); // skip empty line
        }
    }

    /**
     * ...Figure out how to win as many prizes as possible.
     * What is the fewest tokens you would have to spend to win all possible prizes?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = 0;
        for (Machine m : machines) {
            result += solve(m, 0L);
        }
        return result;
    }

    public static long solve(Machine machine, long fix) {
        long prizeX = machine.prizeX + fix;
        long prizeY = machine.prizeY + fix;
        long ax = machine.buttons[0][0];
        long ay = machine.buttons[0][1];
        long bx = machine.buttons[1][0];
        long by = machine.buttons[1][1];

        // start with increment of 1 or the 10% of prize
        long ratio = (long) pow(10, floor(log10(prizeX / ax)));
        long inc = max(1, ratio / 10);

        // linear search
        double lastDelta = Long.MAX_VALUE; // distance from target
        long staleGuard = 0; // count the numbers of iteration where the distance from target does not change
        boolean converging = false; // we are close to the target and we try to oscillate around it
        for (long a = 0; staleGuard < 100; a += inc, staleGuard++) {
            double bX = (prizeX - a * ax) / (double) bx; // solve x : a•ax + b•bx = pX -> b = (pX - a•ax) / bx
            double bY = (prizeY - a * ay) / (double) by; // solve y : a•ay + b•by = pY
            boolean checkX = ax * a + bx * (long) bY == prizeX; // check with bY as integer
            boolean checkY = ay * a + by * (long) bX == prizeY; // check with bX as integer
            double delta = abs(bX - bY);
            if (bX == bY && bX >= 0 && bY >= 0 && checkX && checkY) {
                return a * 3 + (long) bX; // min cost to reach the target
            } else if (delta > lastDelta) {
                if (converging) break;
                if (inc == 1 || inc == -1) {
                    inc = -inc;
                    converging = true;
                } else {
                    inc = -inc / 10;
                }
                staleGuard = 0;
            }
            lastDelta = delta;
        }
        return 0L;
    }

    /**
     * ...Using the corrected prize coordinates, figure out how to win as many prizes as possible.
     * What is the fewest tokens you would have to spend to win all possible prizes?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long result = 0L;
        for (Machine m : machines) {
            result += solve(m, 10000000000000L);
        }
        return result;
    }
}


