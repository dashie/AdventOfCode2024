package adventofcode.y2024;

import adventofcode.commons.AoCBoard;
import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCPoint;
import adventofcode.commons.AoCProblem;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Day 20: Race Condition
 * https://adventofcode.com/2024/day/20
 */
public class Problem20 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem20().solve(true);
    }

    AoCBoard<Character> board;
    AoCPoint p0, pEnd;
    Map<AoCPoint, Long> distFromSMap;
    Map<AoCPoint, Long> distFromEMap;

    long savedTimePart1;
    long savedTimePart2;

    @Override
    public void processInput(AoCInput input) throws Exception {
        if (isUsingSample()) {
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

    record Visit(AoCPoint p, long distance) {}

    private Map<AoCPoint, Long> evalDistanceMap(AoCPoint p0, AoCPoint pEnd) {
        Map<AoCPoint, Long> distMap = new HashMap<>();
        Deque<Visit> stack = new LinkedList<>();
        stack.add(new Visit(p0, 0));
        while (!stack.isEmpty()) {
            Visit v = stack.pollFirst();
            char c = board.get(v.p, '#');
            if (c == '#') continue;
            if (!board.isValidCell(v.p)) continue;
            if (distMap.containsKey(v.p)) continue;
            distMap.put(v.p, v.distance);
            if (v.p.equals(pEnd)) continue; // end
            for (AoCPoint np : v.p.neighbors()) {
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
    public Long partOne() throws Exception {
        long targetTime = distFromEMap.get(p0);
        long n = countPathsWithCheat(2, targetTime - savedTimePart1);
        return n;
    }

    private long countPathsWithCheat(int cheatTime, long targetTime) {
        long count = 0;
        for (AoCPoint p : distFromSMap.keySet()) {
            for (AoCPoint np : board.neighbors(p, cheatTime, v -> v != '#')) {
                long dist = np.distance(p).manhattam();
                long distFromP0 = distFromSMap.get(p);
                long distFromEnd = distFromEMap.get(np);
                if (distFromP0 + distFromEnd + dist <= targetTime) count++;
            }
        }
        return count;
    }

    /**
     * Find the best cheats using the updated cheating rules.
     * How many cheats would save you at least 100 picoseconds?
     */
    @Override
    public Long partTwo() throws Exception {
        long targetTime = distFromEMap.get(p0);
        long n = countPathsWithCheat(20, targetTime - savedTimePart2); // 20 = 5
        return n;
    }
}
