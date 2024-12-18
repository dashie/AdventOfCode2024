package adventofcode.y2024;

import adventofcode.commons.*;

import java.util.*;

/**
 * Day 18: RAM Run
 * https://adventofcode.com/2024/day/18
 */
public class Problem18 extends AoCProblem<String> {

    public static void main(String[] args) throws Exception {
        new Problem18().solve(true);
    }

    int PART1_TIME;
    int WIDTH;
    int HEIGHT;
    List<AoCPoint> bytes = new ArrayList<>();
    List<Set<AoCPoint>> corruptedSetsAtTime = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        if (isUsingSample()) {
            WIDTH = 6;
            HEIGHT = 6;
            PART1_TIME = 12;
        } else {
            WIDTH = 70;
            HEIGHT = 70;
            PART1_TIME = 1024;
        }

        for (MatcherEx m : input.pattern("([0-9]+),([0-9]+)").toList()) {
            AoCPoint p = AoCPoint.valueOf(m.get(1), m.get(2));
            bytes.add(p);
        }

        Set<AoCPoint> prevSet = new HashSet<>();
        corruptedSetsAtTime.add(prevSet); // empy set at time 0
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
    public String partOne() throws Exception {
        Step step = findShortestPath(PART1_TIME);
        return Long.toString(step.len);
    }

    record Step(AoCPoint p, long len, int time, Step prev) {}

    private Step findShortestPath(int time) {
        AoCPoint p0 = new AoCPoint(0, 0);
        AoCPoint pEnd = new AoCPoint(WIDTH, HEIGHT);

        Step best = null;

        Set<AoCPoint> visited = new HashSet<>();
        Deque<Step> stack = new LinkedList<>();
        stack.add(new Step(p0, 0, 0, null));

        while (!stack.isEmpty()) {
            Step s = stack.poll();
            if (s.p.x < 0 || s.p.x > WIDTH) continue;
            if (s.p.y < 0 || s.p.y > HEIGHT) continue;
            if (best != null && s.len >= best.len) continue;
            if (s.p.equals(pEnd)) {
                if (best == null || s.len < best.len) best = s;
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
        return best;
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
    public String partTwo() throws Exception {
        Set<AoCPoint> pathPoints = null;
        for (int time = PART1_TIME; time < bytes.size(); ++time) {
            if (pathPoints != null) {
                // before to run a new simulation check if the new point falls on path cells
                var p = bytes.get(time - 1); // 1st point falls at time 0
                if (!pathPoints.contains(p)) continue;
            }
            Step step = findShortestPath(time);
            if (step == null) {
                var p = bytes.get(time - 1); // 1st point falls at time 0
                return p.x + "," + p.y;
            }
            pathPoints = stepToPathPoints(step); // update path points set
        }
        return "?";
    }

    private Set<AoCPoint> stepToPathPoints(Step lastStep) {
        Set<AoCPoint> points = new HashSet<>();
        while (lastStep != null) {
            points.add(lastStep.p);
            lastStep = lastStep.prev;
        }
        return points;
    }
}
