package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;
import adventofcode.commons.MatcherEx;
import org.apache.commons.math3.linear.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.rint;

/**
 * Day 13: Claw Contraption
 * https://adventofcode.com/2024/day/13
 *
 * Solve using Apache Commons Math
 */
public class Problem13v2 extends AoCProblem<Long, Problem13v2> {

    public static void main(String[] args) throws Exception {
        new Problem13v2().loadResourceAndSolve(false);
    }

    record Machine(double prizeX, double prizeY, double[][] buttons) {}

    public List<Machine> machines = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {

        Iterator<LineEx> it = input.iterateLineExs().iterator();
        while (it.hasNext()) {
            MatcherEx m;
            m = it.next().match("X([+-][0-9]+), Y([+-][0-9]+)");
            double ax = m.getDouble(1);
            double ay = m.getDouble(2);
            m = it.next().match("X([+-][0-9]+), Y([+-][0-9]+)");
            double bx = m.getDouble(1);
            double by = m.getDouble(2);
            m = it.next().match("X=([0-9]+), Y=([0-9]+)");
            double x = m.getDouble(1);
            double y = m.getDouble(2);
            machines.add(new Machine(x, y, new double[][]{{ax, ay}, {bx, by}}));
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

    // FIXME it is still not working with big numbers
    public long solve(Machine machine, long fix) {
        RealMatrix coefficients = MatrixUtils.createRealMatrix(new double[][]{
            {machine.buttons[0][0], machine.buttons[1][0]},
            {machine.buttons[0][1], machine.buttons[1][1]}});
        DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
        RealVector constants = MatrixUtils.createRealVector(new double[]{
            machine.prizeX + fix,
            machine.prizeY + fix});
        RealVector solutions = solver.solve(constants);
        long a = toLong(solutions.getEntry(0));
        long b = toLong(solutions.getEntry(1));
        if (a < 0 || b < 0) return 0;
        return a * 3 + b;
    }

    private long toLong(double v) {
        double vRounded = rint(v);
        if (abs(v - vRounded) < 0.0000000001) {
            // because I can't find a solver for integer solutions
            // I need to approximate to integer solutions values with
            // very near to integer values
            return (long) vRounded;
        } else {
            return -1; // means error
        }
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


