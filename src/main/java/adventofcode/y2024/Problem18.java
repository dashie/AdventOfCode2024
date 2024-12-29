package adventofcode.y2024;

import adventofcode.commons.*;

import java.util.*;

import static adventofcode.commons.Point.parsePoint;

/**
 * Day 18: RAM Run
 * https://adventofcode.com/2024/day/18
 */
public class Problem18 extends AoCProblem<String, Problem18> {

    public static void main(String[] args) throws Exception {
        new Problem18().loadResourceAndSolve(true);
    }

    int PART1_TIME;
    Rect MEMORY_SIZE;
    List<Point> bytes = new ArrayList<>();
    List<Set<Point>> corruptedSetsAtTime = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        if (isUsingSampleResource()) {
            MEMORY_SIZE = Rect.of(6, 6);
            PART1_TIME = 12;
        } else {
            MEMORY_SIZE = Rect.of(70, 70);
            PART1_TIME = 1024;
        }

        for (String str : input.iterateLines()) {
            bytes.add(parsePoint(str));
        }

        Set<Point> prevSet = new HashSet<>();
        corruptedSetsAtTime.add(prevSet); // empty set at time 0
        for (Point p : bytes) {
            Set<Point> set = new HashSet<>(prevSet);
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

    record Step(Point p, long len, int time, Step prev) {}

    private Step findShortestPath(int time) {
        Point p0 = new Point(0, 0);
        Point pEnd = new Point(MEMORY_SIZE.p2.x, MEMORY_SIZE.p2.y);

        Step result = null;

        Set<Point> visited = new HashSet<>();
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
            for (Point np : s.p.neighbors()) {
                stack.add(new Step(np, s.len + 1, s.time + 1, s));
            }
        }

        // dumpState(best, time, width, height);
        return result;
    }

    private void dumpState(Step best, int time, int width, int height) {
        Board<Character> board = new Board<>(Character.class, width + 1, height + 1);
        board.fill('.');
        for (Point p : corruptedSetsAtTime.get(time)) {
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
        Set<Point> pathPoints = null;
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

    private Set<Point> lastStepToPathPoints(Step lastStep) {
        Set<Point> points = new HashSet<>();
        while (lastStep != null) {
            points.add(lastStep.p);
            lastStep = lastStep.prev;
        }
        return points;
    }
}
