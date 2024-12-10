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
public class Problem10 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem10().solve(false);
    }

    private AoCBoard<Integer> board;

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toIntBoard();
    }

    /**
     * ...The reindeer gleefully carries over a protractor and adds it to the pile.
     * What is the sum of the scores of all trailheads on your topographic map?
     */
    @Override
    public Long partOne() throws Exception {
        return (long) board.forEach((p, v) ->
            (v == 0) ? countTrailheads(p, false) : 0
        );
    }

    private int countTrailheads(AoCPoint p0, boolean distinctPath) {
        int trailheads = 0;
        Set<AoCPoint> visited = new HashSet<>();
        Deque<AoCPoint> stack = new LinkedList<>();
        stack.push(p0);
        while (!stack.isEmpty()) {
            AoCPoint p = stack.pop();
            visited.add(p);
            int level = board.get(p, -1);
            if (level == 9) {
                trailheads++;
            } else {
                for (AoCVector dir : AoCVector.DIRECTIONS) {
                    AoCPoint next = p.translate(dir);
                    if (distinctPath || !visited.contains(next)) {
                        if (board.get(next, -1) == level + 1)
                            stack.push(next);
                    }
                }
            }
        }
        return trailheads;
    }

    /**
     * ...You're not sure how, but the reindeer seems to have crafted some tiny flags
     * out of toothpicks and bits of paper and is using them to mark trailheads on your
     * topographic map.
     * What is the sum of the ratings of all trailheads?
     */
    @Override
    public Long partTwo() throws Exception {
        return (long) board.forEach((p, v) ->
            (v == 0) ? countTrailheads(p, true) : 0
        );
    }
}
