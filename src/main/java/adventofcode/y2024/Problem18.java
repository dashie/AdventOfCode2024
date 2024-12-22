package adventofcode.y2024;

import adventofcode.commons.*;

import java.util.*;

import static adventofcode.commons.AoCPoint.parsePoint;

/**
 * Day 18: RAM Run
 * https://adventofcode.com/2024/day/18
 */
public class Problem18 extends AoCProblem<String> {

    public static void main(String[] args) throws Exception {
        new Problem18().loadAndSolve(true);
    }

    int PART1_TIME;
    AoCRect MEMORY_SIZE;
    List<AoCPoint> bytes = new ArrayList<>();
    List<Set<AoCPoint>> corruptedSetsAtTime = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        if (isUsingSampleData()) {
            MEMORY_SIZE = AoCRect.of(6, 6);
            PART1_TIME = 12;
        } else {
            MEMORY_SIZE = AoCRect.of(70, 70);
            PART1_TIME = 1024;
        }

        for (String str : input.iterateLines()) {
            bytes.add(parsePoint(str));
        }

        Set<AoCPoint> prevSet = new HashSet<>();
        corruptedSetsAtTime.add(prevSet); // empty set at time 0
        for (AoCPoint p : bytes) {
            Set<AoCPoint> set = new HashSet<>(prevSet);
            set.add(p);
            corruptedSetsAtTime.add(set);
            prevSet = set;
        }
    }

    /**
     * ...Simulate the first kilobyte (1024 bytes) falling onto your memory space.
     * Afterward, what is the minimum number of steps needed to reach the exit?
     */
    @Override
    public String solvePartOne() throws Exception {
        Step lastStep = findShortestPath(PART1_TIME);
        return Long.toString(lastStep.len);
    }

    record Step(AoCPoint p, long len, int time, Step prev) {}

    private Step findShortestPath(int time) {
        AoCPoint p0 = new AoCPoint(0, 0);
        AoCPoint pEnd = new AoCPoint(MEMORY_SIZE.p2.x, MEMORY_SIZE.p2.y);

        Step result = null;

        Set<AoCPoint> visited = new HashSet<>();
        Deque<Step> stack = new LinkedList<>();
        stack.add(new Step(p0, 0, 0, null));

        while (!stack.isEmpty()) {
            Step s = stack.poll();
            if (s.p.isOut(MEMORY_SIZE)) continue;
            if (result != null && s.len >= result.len) continue;
            if (s.p.equals(pEnd)) {
                if (result == null || s.len < result.len) result = s;
                continue;
            }
            if (visited.contains(s.p)) continue;
            if (corruptedSetsAtTime.get(time).contains(s.p)) continue;
            visited.add(s.p);
            for (AoCPoint np : s.p.neighbors()) {
                stack.add(new Step(np, s.len + 1, s.time + 1, s));
            }
        }

        // dumpState(best, time, width, height);
        return result;
    }

    private void dumpState(Step best, int time, int width, int height) {
        AoCBoard<Character> board = new AoCBoard<>(Character.class, width + 1, height + 1);
        board.fill('.');
        for (AoCPoint p : corruptedSetsAtTime.get(time)) {
            board.set(p, '#');
        }
        Step s = best;
        while (s != null) {
            board.set(s.p, s.prev != null ? 'O' : '@');
            s = s.prev;
        }
        board.dumpBoard("%c");
    }

    /**
     * ...Simulate more of the bytes that are about to corrupt your memory space.
     * What are the coordinates of the first byte that will prevent the exit from
     * being reachable from your starting position?
     * (Provide the answer as two integers separated by a comma with no other characters.)
     */
    @Override
    public String solvePartTwo() throws Exception {
        Set<AoCPoint> pathPoints = null;
        for (int time = PART1_TIME; time < bytes.size(); ++time) {
            if (pathPoints != null) {
                // before to run a new simulation check if the new point falls on path cells
                var p = bytes.get(time - 1); // 1st point falls at time 0
                if (!pathPoints.contains(p)) continue;
            }
            Step lastStep = findShortestPath(time);
            if (lastStep == null) {
                var p = bytes.get(time - 1); // 1st point falls at time 0
                return p.x + "," + p.y;
            }
            pathPoints = lastStepToPathPoints(lastStep); // update path points set
        }
        return "?";
    }

    private Set<AoCPoint> lastStepToPathPoints(Step lastStep) {
        Set<AoCPoint> points = new HashSet<>();
        while (lastStep != null) {
            points.add(lastStep.p);
            lastStep = lastStep.prev;
        }
        return points;
    }
}
