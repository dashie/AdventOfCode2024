package adventofcode.y2024;

import adventofcode.commons.Board;
import adventofcode.commons.AoCInput;
import adventofcode.commons.Point;
import adventofcode.commons.AoCProblem;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Day 20: Race Condition
 * https://adventofcode.com/2024/day/20
 */
public class Problem20 extends AoCProblem<Long, Problem20> {

    public static void main(String[] args) throws Exception {
        new Problem20().loadResourceAndSolve(true);
    }

    Board<Character> board;
    Point p0, pEnd;
    Map<Point, Long> distFromSMap;
    Map<Point, Long> distFromEMap;

    long savedTimePart1;
    long savedTimePart2;

    @Override
    public void processInput(AoCInput input) throws Exception {
        if (isUsingSampleResource()) {
            savedTimePart1 = 12; // expected 8 paths
            savedTimePart2 = 70; // expected 41 paths
        } else {
            savedTimePart1 = 100; // expected 1358 paths with the AoC user inpu
            savedTimePart2 = 100; // expected 1005856 paths with the AoC user inpu
        }

        board = input.toCharBoard();
        p0 = board.searchFor('S');
        pEnd = board.searchFor('E');
        distFromSMap = evalDistanceMap(p0, pEnd);
        distFromEMap = evalDistanceMap(pEnd, p0);
    }

    record Visit(Point p, long distance) {}

    private Map<Point, Long> evalDistanceMap(Point p0, Point pEnd) {
        Map<Point, Long> distMap = new HashMap<>();
        Deque<Visit> stack = new LinkedList<>();
        stack.add(new Visit(p0, 0));
        while (!stack.isEmpty()) {
            Visit v = stack.pollFirst();
            if (!board.isValidCell(v.p)) continue;
            char c = board.get(v.p, '#');
            if (c == '#') continue;
            if (distMap.containsKey(v.p)) continue;
            distMap.put(v.p, v.distance);
            if (v.p.equals(pEnd)) continue; // end
            for (Point np : v.p.neighbors()) {
                stack.add(new Visit(np, v.distance + 1));
            }
        }
        return distMap;
    }

    /**
     * ...You aren't sure what the conditions of the racetrack will be like,
     * so to give yourself as many options as possible, you'll need a list
     * of the best cheats.
     * How many cheats would save you at least 100 picoseconds?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long targetTime = distFromEMap.get(p0);
        long n = countPathsWithCheat(2, targetTime - savedTimePart1);
        return n;
    }

    private long countPathsWithCheat(int cheatTime, long targetTime) {
        long count = 0;
        for (Point p : distFromSMap.keySet()) {
            for (Point np : board.neighbors(p, cheatTime, v -> v != '#')) {
                long distCheat = np.distance(p).manhattam();
                long distFromS = distFromSMap.get(p);
                long distFromE = distFromEMap.get(np);
                if (distFromS + distFromE + distCheat <= targetTime) count++;
            }
        }
        return count;
    }

    /**
     * ...Find the best cheats using the updated cheating rules.
     * How many cheats would save you at least 100 picoseconds?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long targetTime = distFromEMap.get(p0);
        long n = countPathsWithCheat(20, targetTime - savedTimePart2); // 20 = 5
        return n;
    }
}
