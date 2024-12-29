package adventofcode.y2024;

import adventofcode.commons.*;

import java.util.HashMap;
import java.util.Map;

import static adventofcode.commons.DirectedPoint.Direction.*;
import static java.util.Arrays.asList;

/**
 * Day 16: Reindeer Maze
 * https://adventofcode.com/2024/day/16
 *
 * Slow solution, not optimized and based on recursion.
 * Set VM option -Xss to -Xss32m to avoid a Stack Overflow error.
 */
public class Problem16v2 extends AoCProblem<Long, Problem16v2> {

    public static void main(String[] args) throws Exception {
        new Problem16v2().loadResourceAndSolve(false);
    }

    Board<Character> board;

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toCharBoard();
    }

    /**
     * ...Analyze your map carefully.
     * What is the lowest score a Reindeer could possibly get?
     */
    @Override
    public Long solvePartOne() throws Exception {
        var p = board.searchFor('S');
        return searchBestScore(DirectedPoint.of(p, Vector.EAST));
    }

    private long searchBestScore(DirectedPoint dp) {
        return _searchBestScore(dp, 0, new HashMap<>());
    }

    private long _searchBestScore(DirectedPoint dp, long score, Map<DirectedPoint, Long> visitsWithScore) {
        char c = board.get(dp.p);
        if (c == '#') return -1;
        if (c == 'E')
            return score;

        long prevScore = visitsWithScore.getOrDefault(dp, Long.MAX_VALUE);
        if (prevScore < score) return -1;
        visitsWithScore.put(dp, score);

        long nextScore = -1;
        for (DirectedPoint.Direction d : asList(FRONT, LEFT, RIGHT)) {
            DirectedPoint dp1 = dp.move(d);
            long cost = d == FRONT ? 1 : 1001;
            long tmpScore = _searchBestScore(dp1, score + cost, visitsWithScore);
            if (tmpScore < 0) continue;
            if (nextScore == -1 || tmpScore < nextScore) nextScore = tmpScore;
        }
        return nextScore;
    }

    /**
     * ...Analyze your map further.
     * How many tiles are part of at least one of the best paths through the maze?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        return 0L;
    }

}
