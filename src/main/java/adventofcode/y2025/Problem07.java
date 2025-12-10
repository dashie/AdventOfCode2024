package adventofcode.y2025;

import adventofcode.commons.*;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Day 7: Laboratories
 * https://adventofcode.com/2025/day/7
 */
public class Problem07 extends AoCProblem<Long, Problem07> {

    public static void main(String[] args) throws Exception {
        new Problem07().loadResourceAndSolve(false);
    }

    private Board<Character> board;

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toCharBoard();
    }

    /**
     * ...OTo repair the teleporter, you first need to
     * understand the beam-splitting properties of the tachyon manifold.
     * In this example, a tachyon beam is split a total of 21 times.
     *
     * Analyze your manifold diagram. How many times will the beam be split?
     */
    @Override
    public Long solvePartOne() throws Exception {
        var tmpBoard = board.clone();
        Point S = tmpBoard.searchFor('S');
        Vector d = Vector.NORTH;

        long splits = 0;
        Queue<Point> points = new LinkedList<>();
        points.add(S);

        while (!points.isEmpty()) {

            var p = points.poll();
            if (!tmpBoard.isValidCell(p)) continue;
            if (tmpBoard.get(p) == '|') continue;
            tmpBoard.set(p, '|');

            p = p.translate(d);
            var next = tmpBoard.get(p, '#');
            if (next == '^') {
                splits++;
                points.add(p.translate(Vector.EAST));
                points.add(p.translate(Vector.WEST));
            } else {
                points.add(p);
            }
        }

        // tmpBoard.dumpBoard("Beam Simulation", "%c");
        return splits;
    }

    /**
     * ...Apply the many-worlds interpretation of quantum
     * tachyon splitting to your manifold diagram. In total,
     * how many different timelines would a single tachyon
     * particle end up on?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        Point S = board.searchFor('S');
        long timelines = countTimelines(S);
        return timelines;
    }

    private MemoizationCache<Long> countTimelinesCache = new MemoizationCache<>();

    private long countTimelines(Point p) {
        return countTimelinesCache.key(p).andCompute(() -> {

            if (!board.isValidCell(p)) return 0L;
            if (p.y == board.N - 1) return 1L;

            Point nextp = p.translate(Vector.NORTH);
            var next = board.get(nextp, '#');
            long timelines = 0;
            if (next == '^') {
                timelines += countTimelines(nextp.translate(Vector.EAST));
                timelines += countTimelines(nextp.translate(Vector.WEST));
            } else {
                timelines += countTimelines(nextp);
            }
            return timelines;
        });
    }
}
