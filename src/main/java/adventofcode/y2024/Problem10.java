package adventofcode.y2024;

import adventofcode.commons.*;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Day 10: Hoof It
 * https://adventofcode.com/2024/day/10
 */
public class Problem10 extends AoCProblem<Long, Problem10> {

    public static void main(String[] args) throws Exception {
        new Problem10().loadResourceAndSolve(false);
    }

    private Board<Integer> board;

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toIntBoard();
    }

    /**
     * ...The reindeer gleefully carries over a protractor and adds it to the pile.
     * What is the sum of the scores of all trailheads on your topographic map?
     */
    @Override
    public Long solvePartOne() throws Exception {
        return (long) board.forEach((p, v) ->
            (v == 0) ? countTrails(p, false) : 0
        );
    }

    private int countTrails(Point p0, boolean distinctPath) {
        int trails = 0;
        Set<Point> visited = new HashSet<>();
        Deque<Point> stack = new LinkedList<>();
        stack.push(p0);
        while (!stack.isEmpty()) {
            Point p = stack.pop();
            visited.add(p);
            int level = board.get(p, -1);
            if (level == 9) {
                trails++;
            } else {
                for (Vector dir : Vector.DIRECTIONS) {
                    Point next = p.translate(dir);
                    if (distinctPath || !visited.contains(next)) {
                        if (board.get(next, -1) == level + 1)
                            stack.push(next);
                    }
                }
            }
        }
        return trails;
    }

    /**
     * ...You're not sure how, but the reindeer seems to have crafted some tiny flags
     * out of toothpicks and bits of paper and is using them to mark trailheads on your
     * topographic map.
     * What is the sum of the ratings of all trailheads?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        return (long) board.forEach((p, v) ->
            (v == 0) ? countTrails(p, true) : 0
        );
    }
}
