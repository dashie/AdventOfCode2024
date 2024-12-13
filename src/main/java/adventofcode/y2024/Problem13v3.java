package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;
import adventofcode.commons.MatcherEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Day 13: Claw Contraption
 * https://adventofcode.com/2024/day/13
 *
 * Solve using linear search.
 */
public class Problem13v3 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem13v3().solve(false);
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
    public Long partOne() throws Exception {
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

        long inc = Math.max(1, fix / 100);
        double lastDelta = Long.MAX_VALUE;
        long staleGuard = 0;
        boolean converging = false;
        for (long a = 0; staleGuard < 100; a += inc, staleGuard++) {
            double bX = (prizeX - a * ax) / (double) bx; // a•ax + b•bx = pX -> b = (pX - a•ax) / bx
            double bY = (prizeY - a * ay) / (double) by; // a•ay + b•by = pY
            boolean checkX = ax * a + bx * (long) bY == prizeX; // check with bY as integer
            boolean checkY = ay * a + by * (long) bX == prizeY; // check with bX as integer
            double delta = Math.abs(bX - bY);
            if (bX == bY && bX >= 0 && bY >= 0 && checkX && checkY) {
                return a * 3 + (long) bX;
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
    public Long partTwo() throws Exception {
        long result = 0L;
        for (Machine m : machines) {
            result += solve(m, 10000000000000L);
        }
        return result;
    }
}


