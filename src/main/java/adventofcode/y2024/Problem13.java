package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;
import adventofcode.commons.MatcherEx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Day 13: Claw Contraption
 * https://adventofcode.com/2024/day/13
 */
public class Problem13 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem13().loadResourceAndSolve(false);
    }

    record Machine(int prizeX, int prizeY, int[][] buttons) {}

    public List<Machine> machines = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {

        Iterator<LineEx> it = input.iterateLineExs().iterator();
        while (it.hasNext()) {
            MatcherEx m;
            m = it.next().match("X([+-][0-9]+), Y([+-][0-9]+)");
            int ax = m.getInt(1);
            int ay = m.getInt(2);
            m = it.next().match("X([+-][0-9]+), Y([+-][0-9]+)");
            int bx = m.getInt(1);
            int by = m.getInt(2);
            m = it.next().match("X=([0-9]+), Y=([0-9]+)");
            int x = m.getInt(1);
            int y = m.getInt(2);
            machines.add(new Machine(x, y, new int[][]{{ax, ay}, {bx, by}}));
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

    public long solve(Machine machine, long fix) {
        long prizeX = machine.prizeX + fix;
        long prizeY = machine.prizeY + fix;
        long ax = machine.buttons[0][0];
        long ay = machine.buttons[0][1];
        long bx = machine.buttons[1][0];
        long by = machine.buttons[1][1];
        // Cramer rule, linear system of 2 equations (https://en.wikipedia.org/wiki/Cramer%27s_rule#Explicit_formulas_for_small_systems)
        // x • ax + y • bx = prizeX
        // x • ay + y • by = prizeY
        long d = ax * by - bx * ay; // det = ax • by - bx • by
        if (d == 0) return 0; // no solution
        long dx = prizeX * by - prizeY * bx; // detX
        if (dx % d != 0) return 0; // no integer solution
        long dy = ax * prizeY - ay * prizeX; // detY
        if (dy % d != 0) return 0; // no integer solution
        long x = dx / d; // push A count
        long y = dy / d; // push B count
        return x * 3 + y; // cost = pushA * 3 + pushB
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


