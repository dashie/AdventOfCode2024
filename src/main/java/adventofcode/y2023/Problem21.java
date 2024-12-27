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
public class Problem21 extends AoCProblem<Long, Problem21> {

    public static void main(String[] args) throws Exception {
        Problem21 problem = AoCProblem.buildWithInputResource(Problem21.class);
        System.out.println(problem.countPlotsWithProjection(26501365, false));
        System.out.println();
        System.out.println(problem.countPlotsWithPolynomial(26501365, false));
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
     * ...Starting from the garden plot marked S on your map,
     * how many garden plots could the Elf reach in exactly 64 steps?
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
        int plotParity = steps % 2 == 0 ? 0 : 1;

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
            if (s.distance % 2 == plotParity) count++;
            s.p.neighbors().forEach(p -> stack.add(new Step(p, s.distance + 1)));
        }
        return count;
    }

    /**
     * ...However, the step count the Elf needs is much larger! Starting from
     * the garden plot marked S on your infinite map, how many garden plots
     * could the Elf reach in exactly 26501365 steps?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long result = countPlotsWithProjection(26501365);
        return result;
    }

    class BoardPage implements Comparable<BoardPage> {

        final AoCPoint origin;
        final AoCPoint p0;
        final long createdAt;
        Set<AoCPoint> visited = new HashSet<>();
        long destroyAt = -1;
        int size = -1;
        long plotsCount = 0;

        BoardPage(AoCPoint origin, AoCPoint p0, long createdAt) {
            this.origin = origin;
            this.p0 = p0;
            this.createdAt = createdAt;
        }

        long incPlotsCount() {
            return ++plotsCount;
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
        public int compareTo(BoardPage o) {
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
            return "%-15s p0=%-15s c:%-6d l:%-6d sz:%-5d plots:%d".formatted(origin, p0, createdAt, l, sz, plotsCount);
        }
    }

    public long countPlotsWithVirtualBoard(int steps) {
        return countPlotsWithVirtualBoard(steps, false);
    }

    public long countPlotsWithVirtualBoard(int steps, boolean dumpPages) {
        AoCPoint p0 = board.searchFor('S');
        Map<AoCPoint, BoardPage> pageMap = new HashMap<>();
        long count = countPlotsWithVirtualBoardTraversal(steps, p0, pageMap);

        if (dumpPages) {
            dumpPagesPeriod(steps);
            dumpPagesDiamond(pageMap);
            // dumpPagesStats(pageMap);
            System.out.printf("pages count: %d%n", pageMap.size());
            long cellsCountStats = pageMap.values().stream().mapToLong(p -> p.plotsCount).sum();
            System.out.printf("cells' count (scaled): %d%n", cellsCountStats);
        }

        return count;
    }

    private long countPlotsWithVirtualBoardTraversal(int steps, AoCPoint p0, Map<AoCPoint, BoardPage> pageMap) {
        int plotParity = steps % 2 == 0 ? 0 : 1;
        Deque<Step> stack = new LinkedList<>();
        stack.add(new Step(p0, 0));
        long count = 0;
        while (!stack.isEmpty()) {
            Step s = stack.pollFirst();
            if (s.distance > steps) continue;
            Character c = getVirtualCell(s.p, s.distance, plotParity, pageMap);
            if (c == null) continue;
            if (s.distance % 2 == plotParity)
                count++;
            s.p.neighbors().forEach(p -> stack.add(new Step(p, s.distance + 1)));
        }
        return count;
    }

    public Character getVirtualCell(AoCPoint p, long time, int plotParity, Map<AoCPoint, BoardPage> pageMap) {
        int rx = p.x % board.N;
        if (rx < 0) rx += board.N;
        int ry = p.y % board.M;
        if (ry < 0) ry += board.M;
        var rp = AoCPoint.of(rx, ry);

        AoCPoint origin = AoCPoint.of(p.x - rx, p.y - ry);
        BoardPage page = pageMap.computeIfAbsent(origin, k -> new BoardPage(k, rp, time));
        if (page.visited == null) return null;

        char c = board.get(rx, ry, '#');
        if (c == '#') return null;
        if (!page.visited.add(rp)) {
            return null;
        } else if (page.visited.size() == pageSize) {
            page.destroy(time);
            // System.out.printf("%s%n", page);
        }
        if (time % 2 == plotParity) page.incPlotsCount();
        return c;
    }

    public long countPlotsWithProjection(int steps) {
        return countPlotsWithProjection(steps, false);
    }

    // TODO still need to be fixed to cover all possible board shape (e.g. not square shapes)
    public long countPlotsWithProjection(int steps, boolean dumpPages) {
        if (steps < board.N * 6) return countPlotsWithVirtualBoard(steps, dumpPages);

        long scaledSteps = steps;
        scaledSteps = scaledSteps - board.N / 2;
        long ratio = scaledSteps / board.N;
        long parity = ratio % 2;
        long remainder = scaledSteps % board.N;
        long simulationMultiplier = parity == 0 ? 4 : 3;
        int simulationSteps = board.N / 2 + board.N * (int) simulationMultiplier + (int) remainder;

        AoCPoint p0 = board.searchFor('S');
        Map<AoCPoint, BoardPage> pageMap = new HashMap<>();
        long scaledCount = countPlotsWithVirtualBoardTraversal(simulationSteps, p0, pageMap);

        // eval visited area size (a diamond if the board is a square)
        int ymin = Integer.MAX_VALUE;
        int xmin = Integer.MAX_VALUE;
        int ymax = Integer.MIN_VALUE;
        int xmax = Integer.MIN_VALUE;

        for (BoardPage p : pageMap.values()) {
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
            long cellsCountStats = pageMap.values().stream().mapToLong(p -> p.plotsCount).sum();
            System.out.printf("cells' count (scaled): %d%n", cellsCountStats);
        }

        BoardPage page0 = pageMap.get(AoCPoint.of(0, 0));
        BoardPage page1 = pageMap.get(AoCPoint.of(board.N, 0));

        BoardPage pageVLh0 = pageMap.get(AoCPoint.of(xmin, -board.M));
        BoardPage pageVLh1 = pageMap.get(AoCPoint.of(xmin + board.N, -board.M));
        BoardPage pageVRh0 = pageMap.get(AoCPoint.of(xmax, -board.M));
        BoardPage pageVRh1 = pageMap.get(AoCPoint.of(xmax - board.N, -board.M));
        BoardPage pageVLb0 = pageMap.get(AoCPoint.of(xmin, board.M));
        BoardPage pageVLb1 = pageMap.get(AoCPoint.of(xmin + board.N, board.M));
        BoardPage pageVRb0 = pageMap.get(AoCPoint.of(xmax, board.M));
        BoardPage pageVRb1 = pageMap.get(AoCPoint.of(xmax - board.N, board.M));

        BoardPage pageVH = pageMap.get(AoCPoint.of(0, ymin));
        BoardPage pageVB = pageMap.get(AoCPoint.of(0, ymax));
        BoardPage pageVL = pageMap.get(AoCPoint.of(xmin, 0));
        BoardPage pageVR = pageMap.get(AoCPoint.of(xmax, 0));

        long ratio1 = ratio - 1;
        long oddPageSteps = ratio % 2 == 0 ? page1.plotsCount : page0.plotsCount;
        long projectionCellsCount =
            (page0.plotsCount + page1.plotsCount) * ratio1 * ratio1 + oddPageSteps * (ratio1 * 2L + 1L)
                + (pageVLh0.plotsCount + pageVLh1.plotsCount) * ratio1 + pageVLh0.plotsCount
                + (pageVRh0.plotsCount + pageVRh1.plotsCount) * ratio1 + pageVRh0.plotsCount
                + (pageVLb0.plotsCount + pageVLb1.plotsCount) * ratio1 + pageVLb0.plotsCount
                + (pageVRb0.plotsCount + pageVRb1.plotsCount) * ratio1 + pageVRb0.plotsCount
                + pageVH.plotsCount + pageVB.plotsCount + pageVL.plotsCount + pageVR.plotsCount;

        return projectionCellsCount;
    }

    public long countPlotsWithPolynomial(int steps) {
        return countPlotsWithPolynomial(steps, false);
    }

    public long countPlotsWithPolynomial(int steps, boolean dumpStats) {
        // I try to solve a polynomial system:
        //
        // y1 = a•x1² + b•x1 + c
        // y2 = a•x2² + b•x2 + c
        // y3 = a•x3² + b•x3 + c
        //
        // In the input file the "S" position is central in a square tile large 131 cells.
        // If x-> is the number o horizontal tiles thet I can reach, and "y" the number of even cells
        //
        //     y = a•x² + b•x + c
        //
        // "c" is the value with x at the end of the central tile, so, for 131 width tiles, x = 0 -> c = 65.
        // Then I have to solve the polynomial:
        //
        //    x = 0 -> c = y0
        //
        // With "c" solved
        //
        //    a•x1² + b•x1 = u1 = y1 - c
        //    a•x2² + b•x2 = u2 = y2 - c
        //
        //    b = (u1 - a•x1²) / x1
        //    b = (u2 - a•x2²) / x2 = u2/x2 - a•x2
        //
        //    (u1 - a•x1²) / x1 = (u2 - a•x2²) / x2
        //    a = (u2/x2 - u1/x1) / (x2 - x1)

        // use always "even" x
        long x0 = 0;
        long x0steps = board.N / 2 + board.N * x0; // x = 0 in the equation
        long x1 = 2;
        long x1steps = board.N / 2 + board.N * x1; // x = 2 in the equation
        long x2 = 4;
        long x2steps = board.N / 2 + board.N * x2; // x = 10 in the equation

        long y0 = countPlotsWithVirtualBoard((int) x0steps);
        long y1 = countPlotsWithVirtualBoard((int) x1steps);
        long y2 = countPlotsWithVirtualBoard((int) x2steps);

        long c = y0;
        double u1 = y1 - c;
        double u2 = y2 - c;
        double a = (u2 / x2 - u1 / x1) / (x2 - x1);
        double b = u2 / x2 - a * x2;

        if (dumpStats) {
            System.out.printf("f(x) = a•x² + b•x + c%n");
            System.out.printf("f(%d:%d) = %d%n", x0, x0steps, y0);
            System.out.printf("f(%d:%d) = %d%n", x1, x1steps, y1);
            System.out.printf("f(%d:%d) = %d%n", x2, x2steps, y2);

            System.out.println("a = " + a);
            System.out.println("b = " + b);
            System.out.println("c = " + c);

            System.out.println("check coefficients");
            System.out.printf("f(%d) ≈ %d (%d)%n", x0, (long) (a * x0 * x0 + b * x0 + c), y0);
            System.out.printf("f(%d) ≈ %d (%d)%n", x1, (long) (a * x1 * x1 + b * x1 + c), y1);
            System.out.printf("f(%d) ≈ %d (%d)%n", x2, (long) (a * x2 * x2 + b * x2 + c), y2);
        }

        long xsteps = (steps - board.N / 2) / board.N;
        long estimatedCount = (long) (a * xsteps * xsteps + b * xsteps + c);
        if (dumpStats) System.out.println("projection count = " + estimatedCount);
        return estimatedCount;
    }

    private void dumpPagesPeriod(int steps) {
        int stepsInto1stPage = board.N / 2;
        int stepsAfter1stPage = steps - stepsInto1stPage;
        int ratio = stepsAfter1stPage / board.N;
        int remainder = stepsAfter1stPage % board.N;
        System.out.printf("steps: %-8d %d + %d x %d + %d%n", steps, stepsInto1stPage, board.N, ratio, remainder);
    }

    private void dumpPagesDiamond(Map<AoCPoint, BoardPage> pageMap) {
        int ymin = Integer.MAX_VALUE;
        int xmin = Integer.MAX_VALUE;
        int ymax = Integer.MIN_VALUE;
        int xmax = Integer.MIN_VALUE;

        for (BoardPage p : pageMap.values()) {
            if (p.completed()) continue;
            if (p.origin.y < ymin) ymin = p.origin.y;
            if (p.origin.x < xmin) xmin = p.origin.x;
            if (p.origin.y > ymax) ymax = p.origin.y;
            if (p.origin.x > xmax) xmax = p.origin.x;
        }

        System.out.println("diamond data:");
        for (int n = xmin; n <= xmax; n += board.N) {
            for (int m = ymin; m <= ymax; m += board.M) {
                BoardPage page = pageMap.get(AoCPoint.of(n, m));
                String cell = page == null ? "      " : "%5d ".formatted(page.plotsCount);
                System.out.print(cell);
            }
            System.out.println();
        }
    }

    private void dumpPagesStats(Map<AoCPoint, BoardPage> pageMap) {
        System.out.println("perimeter's pages:");
        pageMap.values().stream()
            .filter(p -> p.destroyAt < 0)
            .sorted(BoardPage::compareTo)
            .forEach(System.out::println);

        System.out.println("central pages with parity:");
        System.out.println(pageMap.get(AoCPoint.of(0, 0)));
        System.out.println(pageMap.get(AoCPoint.of(board.N, 0)));
        System.out.println(pageMap.get(AoCPoint.of(0, board.M)));
    }
}
