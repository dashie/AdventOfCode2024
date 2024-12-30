package adventofcode.y2023;

import adventofcode.commons.*;

import java.util.HashSet;
import java.util.Set;

import static adventofcode.commons.DirectedPoint.Direction.*;
import static adventofcode.commons.Vector.*;
import static java.lang.Math.max;

/**
 * Day 23: A Long Walk
 * https://adventofcode.com/2023/day/23
 *
 * N.B. Use -Xss1M to avoid StackOverflow
 */
public class Problem23v2 extends AoCProblem<Long, Problem23v2> {

    public static void main(String[] args) throws Exception {
        new Problem23v2().loadResourceAndSolve(false);
    }

    Board<Character> board;
    Point START;
    Point END;

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toCharBoard();
        START = Point.of(1, 0);
        END = Point.of(board.N - 2, board.M - 1);
    }

    /**
     * ...Find the longest hike you can take through the hiking
     * trails listed on your map. How many steps long is the longest
     * hike?
     */
    @Override
    public Long solvePartOne() throws Exception {
        return findLongestPath(false);
    }

    private long findLongestPath(boolean climb) {
        findLongestPath = -1; // reset global variable
        return findLongestPath(DirectedPoint.of(START, NORTH), climb, 0, new HashSet<>());
    }

    long findLongestPath = -1;

    private long findLongestPath(DirectedPoint dp, boolean climb, int steps, Set<Point> visited) {
        char c = board.get(dp.p, '#');
        if (c == '#') return -1;

        if (END.equals(dp.p)) {
            if (steps > findLongestPath) {
                findLongestPath = steps;
                board.dumpBoard("best: " + steps + " ---", "%c", cell -> {
                    if (cell.v == '.') return visited.contains(cell.p) ? '•' : ' ';
                    return cell.v;
                });
            }
            return 0;
        }

        if (!climb) {
            if (c == '>' && dp.d.is(WEST)) return -1;
            if (c == '<' && dp.d.is(EAST)) return -1;
            if (c == 'v' && dp.d.is(SOUTH)) return -1;
            if (c == '^' && dp.d.is(NORTH)) return -1;
        }

        if (visited.contains(dp.p)) return -1;
        visited.add(dp.p);
        long best = -1;
        for (var ndp : dp.move(FRONT, LEFT, RIGHT)) {
            long nbest = findLongestPath(ndp, climb, steps + 1, visited);
            if (nbest != -1) best = max(best, nbest + 1);
        }
        visited.remove(dp.p);

        return best;
    }

    /**
     * ...Find the longest hike you can take through the surprisingly
     * dry hiking trails listed on your map. How many steps long is the
     * longest hike?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        return findLongestPath(true);
    }
}
