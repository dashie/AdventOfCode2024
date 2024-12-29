package adventofcode.y2024;

import adventofcode.commons.*;
import adventofcode.commons.Vector;

import java.util.*;

/**
 * Day 16: Reindeer Maze
 * https://adventofcode.com/2024/day/16
 */
public class Problem16 extends AoCProblem<Long, Problem16> {

    public static void main(String[] args) throws Exception {
        new Problem16().loadResourceAndSolve(false);
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
        List<SearchState> bestPaths = evalBestPaths(p, Vector.EAST);
        return bestPaths.getFirst().score();
    }

    record SearchState(DirectedPoint dp, long score, SearchState prevState) {}

    private List<SearchState> evalBestPaths(Point p0, Vector d0) {
        Map<DirectedPoint, Long> visitsWithScore = new HashMap<>();
        // if we use a priority queue based on position to be visited sorted by score
        // is more probable to visits a cell the first time at the lowest score possible
        PriorityQueue<SearchState> pqueue = new PriorityQueue<>(Comparator.comparingLong(a -> a.score));
        pqueue.add(new SearchState(new DirectedPoint(p0, d0), 0, null));
        long bestScore = Integer.MAX_VALUE;
        List<SearchState> bestPaths = new ArrayList<>();

        while (!pqueue.isEmpty()) {
            SearchState state = pqueue.poll();

            if (state.score > bestScore) continue; // discard this option
            char c = board.get(state.dp.p);
            if (c == '#') continue; // if hits a wall continue
            if (c == 'E') {
                if (state.score <= bestScore) { // best option
                    if (state.score < bestScore) bestPaths = new ArrayList<>();
                    bestPaths.add(state);
                    bestScore = state.score;
                }
                continue;
            }

            long lastScore = visitsWithScore.getOrDefault(state.dp, Long.MAX_VALUE);
            if (state.score > lastScore) continue; // cell already visited with a best score
            visitsWithScore.put(state.dp, state.score);

            pqueue.add(new SearchState(state.dp.moveRight(), state.score + 1001, state));
            pqueue.add(new SearchState(state.dp.moveLeft(), state.score + 1001, state));
            pqueue.add(new SearchState(state.dp.moveFront(), state.score + 1, state));
        }

        return bestPaths;
    }

    /**
     * ...Analyze your map further.
     * How many tiles are part of at least one of the best paths through the maze?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        var p = board.searchFor('S');
        long score = countBestTiles(p, Vector.EAST);
        return score;
    }

    private long countBestTiles(Point p0, Vector d0) {
        List<SearchState> bestPaths = evalBestPaths(p0, d0);
        Set<Point> bestTitles = new HashSet<>();
        for (SearchState s : bestPaths) {
            while (s != null) {
                bestTitles.add(s.dp.p);
                s = s.prevState;
            }
        }
        return bestTitles.size();
    }
}
