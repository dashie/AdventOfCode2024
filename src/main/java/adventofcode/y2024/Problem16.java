package adventofcode.y2024;

import adventofcode.commons.*;

import java.util.*;

/**
 * Day 16: Reindeer Maze
 * https://adventofcode.com/2024/day/16
 */
public class Problem16 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem16().solve(false);
    }

    AoCBoard<Character> board;

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toCharBoard();
    }

    /**
     * ...Analyze your map carefully.
     * What is the lowest score a Reindeer could possibly get?
     */
    @Override
    public Long partOne() throws Exception {
        var p = board.searchFor('S');
        List<SearchState> bestPaths = searchBestPaths(p, AoCVector.EAST);
        return bestPaths.getFirst().score();
    }

    record SearchState(AoCPoint p, AoCVector d, long score, SearchState prevState) {}

    private List<SearchState> searchBestPaths(AoCPoint p0, AoCVector d0) {
        Map<String, Long> visitsWithScore = new HashMap<>();
        Deque<SearchState> stack = new LinkedList<>();
        stack.push(new SearchState(p0, d0, 0, null));
        long bestScore = Integer.MAX_VALUE;
        List<SearchState> bestPaths = new ArrayList<>();

        while (!stack.isEmpty()) {
            SearchState state = stack.pop();

            if (state.score > bestScore) continue; // discard this option
            char c = board.get(state.p);
            if (c == '#') continue; // if hits a wall continue
            if (c == 'E') {
                if (state.score <= bestScore) { // best option
                    if (state.score < bestScore) bestPaths = new ArrayList<>();
                    bestPaths.add(state);
                    bestScore = state.score;
                }
                continue;
            }

            String visitedKey = state.p + "-" + state.d;
            long lastScore = visitsWithScore.getOrDefault(visitedKey, Long.MAX_VALUE);
            if (state.score > lastScore) continue; // cell already visited with a best score
            visitsWithScore.put(visitedKey, state.score);

            stack.push(new SearchState(state.p.translate(state.d.rotate90R()), state.d.rotate90R(), state.score + 1001, state));
            stack.push(new SearchState(state.p.translate(state.d.rotate90L()), state.d.rotate90L(), state.score + 1001, state));
            stack.push(new SearchState(state.p.translate(state.d), state.d, state.score + 1, state));
        }

        return bestPaths;
    }

    /**
     * ...Analyze your map further.
     * How many tiles are part of at least one of the best paths through the maze?
     */
    @Override
    public Long partTwo() throws Exception {
        var p = board.searchFor('S');
        long score = searchBestTiles(p, AoCVector.EAST);
        return score;
    }

    private long searchBestTiles(AoCPoint p0, AoCVector d0) {
        List<SearchState> bestPaths = searchBestPaths(p0, d0);
        Set<AoCPoint> bestTitles = new HashSet<>();
        for (SearchState s : bestPaths) {
            while (s != null) {
                bestTitles.add(s.p);
                s = s.prevState;
            }
        }
        return bestTitles.size();
    }
}
