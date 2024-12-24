package adventofcode.y2023;

import adventofcode.commons.AoCBoard;
import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCPoint;
import adventofcode.commons.AoCProblem;

import java.util.*;

/**
 * Day 21: Step Counter
 * https://adventofcode.com/2023/day/21
 */
public class Problem21 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        Problem21 sample = AoCProblem.buildWithSampleResource(Problem21.class);
        System.out.println(sample.countPlots(6) + " == 16");
        System.out.println(sample.countPlotsEx(100) + " == 6536");

        System.out.println();
        System.out.println(sample.countPlotsEx(6 + 11 * 20, true));
        System.out.println();
        System.out.println(sample.countPlotsInfinite(6 + 11 * 20, true));
        System.out.println();

        Problem21 problem = AoCProblem.buildWithInputResource(Problem21.class);
        System.out.println();
        System.out.println(problem.countPlotsEx(65 + 131 * 40, true));
        System.out.println();
        System.out.println(problem.countPlotsInfinite(65 + 131 * 40, true));
        System.out.println();
        System.out.println(problem.countPlotsInfinite(26501365, true));
        System.out.println();
    }

    AoCBoard<Character> board;
    long pageSize;

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toCharBoard();
        pageSize = countVisitableCells();
    }

    /**
     *
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = countPlots(64);
        return result;
    }

    record Step(AoCPoint p, int distance) {}

    public long countVisitableCells() {
        AoCPoint p0 = board.searchFor('S');

        Set<AoCPoint> visited = new HashSet<>();
        Deque<Step> stack = new LinkedList<>();
        stack.add(new Step(p0, 0));
        while (!stack.isEmpty()) {
            Step s = stack.pollFirst();
            char c = board.get(s.p, '#');
            if (c == '#') continue;
            if (!visited.add(s.p)) continue;
            s.p.neighbors().forEach(p -> stack.add(new Step(p, s.distance + 1)));
        }
        return visited.size();
    }

    public long countPlots(int steps) {
        AoCPoint p0 = board.searchFor('S');

        Set<AoCPoint> visited = new HashSet<>();
        Deque<Step> stack = new LinkedList<>();
        stack.add(new Step(p0, 0));
        long count = 0;
        while (!stack.isEmpty()) {
            Step s = stack.pollFirst();
            char c = board.get(s.p, '#');
            if (c == '#') continue;
            if (!visited.add(s.p)) continue;
            if (s.distance > steps) continue;
            if (s.distance % 2 == 0) count++;
            s.p.neighbors().forEach(p -> stack.add(new Step(p, s.distance + 1)));
        }
        return count;
    }

    /**
     *
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long result = countPlotsEx(50);
        return result;
    }

    class Page implements Comparable<Page> {

        final AoCPoint origin;
        final AoCPoint p0;
        final long createdAt;
        Set<AoCPoint> visited = new HashSet<>();
        long destroyAt = -1;
        int size = -1;
        long evenSteps = 0;

        Page(AoCPoint origin, AoCPoint p0, long createdAt) {
            this.origin = origin;
            this.p0 = p0;
            this.createdAt = createdAt;
        }

        long incEvenSteps() {
            return ++evenSteps;
        }

        void destroy(long time) {
            destroyAt = time;
            size = visited.size();
            visited = null;
        }

        public boolean completed() {
            return destroyAt > 0;
        }

        public int quadrant() {
            if (origin.x >= 0 && origin.y <= 0) return 0; // high-right
            else if (origin.x >= 0 && origin.y > 0) return 1; // low-right
            else if (origin.x < 0 && origin.y > 0) return 2; // low-left
            else return 3; // high-left
        }

        @Override
        public int compareTo(Page o) {
            int cmp = Integer.compare(quadrant(), o.quadrant());
            if (cmp == 0) {
                cmp = switch (quadrant()) {
                    case 0, 3 -> Integer.compare(origin.x, o.origin.x);
                    default -> Integer.compare(o.origin.x, origin.x);
                };
                if (cmp == 0) {
                    cmp = switch (quadrant()) {
                        case 0, 1 -> Integer.compare(origin.y, o.origin.y);
                        default -> Integer.compare(o.origin.y, origin.y);
                    };
                }
            }
            return cmp;
        }

        @Override
        public String toString() {
            long l = destroyAt > 0 ? destroyAt - createdAt : -1;
            long sz = visited != null ? visited.size() : size;
            return "%-15s p0=%-15s c:%-6d l:%-6d sz:%-5d steps:%d".formatted(origin, p0, createdAt, l, sz, evenSteps);
        }
    }

    public long countPlotsEx(int steps) {
        return countPlotsEx(steps, false);
    }

    public long countPlotsEx(int steps, boolean dumpPages) {
        AoCPoint p0 = board.searchFor('S');
        Map<AoCPoint, Page> pageMap = new HashMap<>();
        long count = countPlotsExTraversal(steps, p0, pageMap);

        if (dumpPages) {
            dumpPagesPeriod(steps);
            dumpPagesDiamond(pageMap);
            // dumpPagesStats(pageMap);
            System.out.printf("pages count: %d%n", pageMap.size());
            long cellsCountStats = pageMap.values().stream().mapToLong(p -> p.evenSteps).sum();
            System.out.printf("cells' count (scaled): %d%n", cellsCountStats);
        }

        return count;
    }

    private long countPlotsExTraversal(int steps, AoCPoint p0, Map<AoCPoint, Page> pageMap) {
        Deque<Step> stack = new LinkedList<>();
        stack.add(new Step(p0, 0));
        long count = 0;
        while (!stack.isEmpty()) {
            Step s = stack.pollFirst();
            if (s.distance > steps) continue;
            Character c = getVirtualCell(s.p, s.distance, pageMap);
            if (c == null) continue;
            if (s.distance % 2 == 0) count++;
            s.p.neighbors().forEach(p -> stack.add(new Step(p, s.distance + 1)));
        }
        return count;
    }

    public Character getVirtualCell(AoCPoint p, long time, Map<AoCPoint, Page> pageMap) {
        int rx = p.x % board.N;
        if (rx < 0) rx += board.N;
        int ry = p.y % board.M;
        if (ry < 0) ry += board.M;
        var rp = AoCPoint.of(rx, ry);

        AoCPoint origin = AoCPoint.of(p.x - rx, p.y - ry);
        Page page = pageMap.computeIfAbsent(origin, k -> new Page(k, rp, time));
        if (page.visited == null) return null;

        char c = board.get(rx, ry, '#');
        if (c == '#') return null;
        if (!page.visited.add(rp)) {
            return null;
        } else if (page.visited.size() == pageSize) {
            page.destroy(time);
            // System.out.printf("%s%n", page);
        }
        if (time % 2 == 0) page.incEvenSteps();
        return c;
    }

    public long countPlotsInfinite(int steps) {
        return countPlotsInfinite(steps, false);
    }

    // TODO still need to be fixed to cover all possible board shape
    public long countPlotsInfinite(int steps, boolean dumpPages) {
        if (steps < board.N * 6) return countPlotsEx(steps, dumpPages);

        long scaledSteps = steps;
        scaledSteps = scaledSteps - board.N / 2;
        long ratio = scaledSteps / board.N;
        long parity = ratio % 2;
        long remainder = scaledSteps % board.N;
        long simulationMultiplier = parity == 0 ? 4 : 3;
        int simulationSteps = board.N / 2 + board.N * (int) simulationMultiplier + (int) remainder;

        AoCPoint p0 = board.searchFor('S');
        Map<AoCPoint, Page> pageMap = new HashMap<>();
        long scaledCount = countPlotsExTraversal(simulationSteps, p0, pageMap);

        // eval shape size
        int ymin = Integer.MAX_VALUE;
        int xmin = Integer.MAX_VALUE;
        int ymax = Integer.MIN_VALUE;
        int xmax = Integer.MIN_VALUE;

        for (Page p : pageMap.values()) {
            if (p.completed()) continue;
            if (p.origin.y < ymin) ymin = p.origin.y;
            if (p.origin.x < xmin) xmin = p.origin.x;
            if (p.origin.y > ymax) ymax = p.origin.y;
            if (p.origin.x > xmax) xmax = p.origin.x;
        }

        //
        if (dumpPages) {
            dumpPagesPeriod(steps);
            dumpPagesDiamond(pageMap);
            // dumpPagesStats(pageMap);
            System.out.printf("pages module: %d (%d)%n", ratio, simulationMultiplier);

            System.out.printf("pages count: %d%n", pageMap.size());
            long cellsCountStats = pageMap.values().stream().mapToLong(p -> p.evenSteps).sum();
            System.out.printf("cells' count (scaled): %d%n", cellsCountStats);
        }

        Page page0 = pageMap.get(AoCPoint.of(0, 0));
        Page page1 = pageMap.get(AoCPoint.of(board.N, 0));

        Page pageVLh0 = pageMap.get(AoCPoint.of(xmin, -board.M));
        Page pageVLh1 = pageMap.get(AoCPoint.of(xmin + board.N, -board.M));
        Page pageVRh0 = pageMap.get(AoCPoint.of(xmax, -board.M));
        Page pageVRh1 = pageMap.get(AoCPoint.of(xmax - board.N, -board.M));
        Page pageVLb0 = pageMap.get(AoCPoint.of(xmin, board.M));
        Page pageVLb1 = pageMap.get(AoCPoint.of(xmin + board.N, board.M));
        Page pageVRb0 = pageMap.get(AoCPoint.of(xmax, board.M));
        Page pageVRb1 = pageMap.get(AoCPoint.of(xmax - board.N, board.M));

        Page pageVH = pageMap.get(AoCPoint.of(0, ymin));
        Page pageVB = pageMap.get(AoCPoint.of(0, ymax));
        Page pageVL = pageMap.get(AoCPoint.of(xmin, 0));
        Page pageVR = pageMap.get(AoCPoint.of(xmax, 0));

        long ratio1 = ratio - 1;
        long oddPageSteps = ratio % 2 == 0 ? page1.evenSteps : page0.evenSteps;
        long estimatedCellsCount =
            (page0.evenSteps + page1.evenSteps) * ratio1 * ratio1 + oddPageSteps * (ratio1 * 2L + 1L)
                + (pageVLh0.evenSteps + pageVLh1.evenSteps) * ratio1 + pageVLh0.evenSteps
                + (pageVRh0.evenSteps + pageVRh1.evenSteps) * ratio1 + pageVRh0.evenSteps
                + (pageVLb0.evenSteps + pageVLb1.evenSteps) * ratio1 + pageVLb0.evenSteps
                + (pageVRb0.evenSteps + pageVRb1.evenSteps) * ratio1 + pageVRb0.evenSteps
                + pageVH.evenSteps + pageVB.evenSteps + pageVL.evenSteps + pageVR.evenSteps;

        return estimatedCellsCount;
    }

    private void dumpPagesPeriod(int steps) {
        int stepsInto1stPage = board.N / 2;
        int stepsAfter1stPage = steps - stepsInto1stPage;
        int ratio = stepsAfter1stPage / board.N;
        int remainder = stepsAfter1stPage % board.N;
        System.out.printf("steps: %-8d %d + %d x %d + %d%n", steps, stepsInto1stPage, board.N, ratio, remainder);
    }

    private void dumpPagesDiamond(Map<AoCPoint, Page> pageMap) {
        int ymin = Integer.MAX_VALUE;
        int xmin = Integer.MAX_VALUE;
        int ymax = Integer.MIN_VALUE;
        int xmax = Integer.MIN_VALUE;

        for (Page p : pageMap.values()) {
            if (p.completed()) continue;
            if (p.origin.y < ymin) ymin = p.origin.y;
            if (p.origin.x < xmin) xmin = p.origin.x;
            if (p.origin.y > ymax) ymax = p.origin.y;
            if (p.origin.x > xmax) xmax = p.origin.x;
        }

        System.out.println("diamond data:");
        for (int n = xmin; n <= xmax; n += board.N) {
            for (int m = ymin; m <= ymax; m += board.M) {
                Page page = pageMap.get(AoCPoint.of(n, m));
                String cell = page == null ? "      " : "%5d ".formatted(page.evenSteps);
                System.out.print(cell);
            }
            System.out.println();
        }
    }

    private void dumpPagesStats(Map<AoCPoint, Page> pageMap) {
        System.out.println("perimeter's pages:");
        pageMap.values().stream()
               .filter(p -> p.destroyAt < 0)
               .sorted(Page::compareTo)
               .forEach(System.out::println);

        System.out.println("central pages with parity:");
        System.out.println(pageMap.get(AoCPoint.of(0, 0)));
        System.out.println(pageMap.get(AoCPoint.of(board.N, 0)));
        System.out.println(pageMap.get(AoCPoint.of(0, board.M)));
    }
}
