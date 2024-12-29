package adventofcode.y2023;

import adventofcode.commons.*;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import static adventofcode.commons.Vector.EAST;
import static adventofcode.commons.Vector.NORTH;

/**
 * Day 17: Clumsy Crucible
 * https://adventofcode.com/2023/day/17
 */
public class Problem17 extends AoCProblem<Long, Problem17> {

    public static void main(String[] args) throws Exception {
        new Problem17().loadResourceAndSolve();
    }

    Board<Integer> board;

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toIntBoard();
    }

    /**
     * ...Directing the crucible from the lava pool to the machine
     * parts factory, but not moving more than three consecutive
     * blocks in the same direction, what is the least heat loss it
     * can incur?
     */
    @Override
    public Long solvePartOne() throws Exception {
        return findMinHeatLost(Point.of(0, 0), false);
    }

    record Step(DirectedPoint dp, int heatLoss, int stepCount, Step prev) implements Comparable<Step> {

        @Override
        public int compareTo(Step o) {
            return Integer.compare(heatLoss, o.heatLoss);
        }
    }

    public long findMinHeatLost(Point p0, boolean ultraCrucible) {
        Point END = Point.of(board.N - 1, board.M - 1);
        long minHeatLoss = Long.MAX_VALUE;
        Step lastStep = null;

        Set<ArrayKey> visited = new HashSet<>();
        PriorityQueue<Step> stack = new PriorityQueue<>();
        stack.add(new Step(DirectedPoint.of(p0, EAST).moveFront(), 0, 1, null));
        stack.add(new Step(DirectedPoint.of(p0, NORTH).moveFront(), 0, 1, null));
        while (!stack.isEmpty()) {
            Step s = stack.poll();
            int c = board.get(s.dp.p, -1);
            if (c == -1) continue;
            int heatLoss = s.heatLoss + c;
            if (heatLoss > minHeatLoss) continue;
            if (!visited.add(ArrayKey.toKey(s.dp, s.stepCount))) continue;
            if (s.dp.p.equals(END)) {
                if (heatLoss < minHeatLoss) {
                    if (ultraCrucible && s.stepCount < 4) continue;
                    // log("min heat loss: %d < %d%n", heatLoss, minHeatLoss);
                    minHeatLoss = heatLoss;
                    lastStep = s;
                }
                continue;
            }
            if (ultraCrucible) {
                if (s.stepCount < 10) {
                    stack.add(new Step(s.dp.moveFront(), heatLoss, s.stepCount + 1, s));
                }
                if (s.stepCount > 3) {
                    stack.add(new Step(s.dp.moveRight(), heatLoss, 1, s));
                    stack.add(new Step(s.dp.moveLeft(), heatLoss, 1, s));
                }
            } else {
                if (s.stepCount < 3) stack.add(new Step(s.dp.moveFront(), heatLoss, s.stepCount + 1, s));
                stack.add(new Step(s.dp.moveRight(), heatLoss, 1, s));
                stack.add(new Step(s.dp.moveLeft(), heatLoss, 1, s));
            }
        }

//        // dump solution
//        Set<Point> path = new HashSet<>();
//        Step s = lastStep;
//        while (s != null) {
//            path.add(s.dp.p);
//            s = s.prev;
//        }
//        board.dumpBoard("%c", c -> {
//            if (path.contains(c.p)) return '0' + c.v;
//            return '₀' + c.v;
//        });

        return minHeatLoss;
    }

    /**
     * ...Directing the ultra crucible from the lava pool to the
     * machine parts factory, what is the least heat loss it can incur?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        return findMinHeatLost(Point.of(0, 0), true);
    }
}
