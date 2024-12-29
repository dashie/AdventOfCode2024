package adventofcode.y2023;

import adventofcode.commons.*;

import java.util.*;

/**
 * Day 11: Cosmic Expansion
 * https://adventofcode.com/2023/day/11
 */
public class Problem11v2 extends AoCProblem<Long, Problem11v2> {

    public static void main(String[] args) throws Exception {
        new Problem11v2().loadResourceAndSolve(false);
    }

    Board<Character> board;
    Set<Integer> freeRows = new HashSet<>();
    Set<Integer> freeCols = new HashSet<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toCharBoard();

        // check rows
        nextrow:
        for (int m = 0; m < board.M; ++m) {
            for (int n = 0; n < board.N; ++n)
                if (board.buffer[m][n] != '.') continue nextrow;
            freeRows.add(m);
        }

        // check cols
        nextcol:
        for (int n = 0; n < board.N; ++n) {
            for (int m = 0; m < board.M; ++m)
                if (board.buffer[m][n] != '.') continue nextcol;
            freeCols.add(n);
        }
    }

    /**
     * ...Expand the universe, then find the length of the
     * shortest path between every pair of galaxies.
     * What is the sum of these lengths?
     */
    @Override
    public Long solvePartOne() throws Exception {
        return evalDistancesSum(2);
    }

    public long evalDistancesSum(int expansionFactor) {
        List<Point> galaxies = board.listAll('#').stream()
            .map(Board.Cell::p)
            .sorted((a, b) -> {
                // sort galaxies by distance from origin,
                // to reduce deep traversal after the first traversals
                int d1 = a.distance(0, 0).manhattam();
                int d2 = b.distance(0, 0).manhattam();
                int cmp = Integer.compare(d1, d2);
                if (cmp == 0) {
                    cmp = Integer.compare(a.y, b.y);
                    if (cmp == 0) {
                        cmp = Integer.compare(a.x, b.x);
                    }
                }
                return cmp;
            })
            .toList();

        Map<Rect, Integer> distances = new HashMap<>();
        for (Point galaxy : galaxies) {
            Set<Point> missingTos = new HashSet<>();
            for (Point to : galaxies) {
                if (to.equals(galaxy)) continue;
                Rect rect = Rect.of(galaxy, to).sortVertices();
                if (distances.containsKey(rect)) continue;
                missingTos.add(to);
            }
            Map<Point, Integer> map = buildDistanceMap(galaxy, missingTos, expansionFactor);
            for (var e : map.entrySet()) {
                Rect r = Rect.of(galaxy, e.getKey()).sortVertices();
                distances.put(r, e.getValue());
            }
        }
        return distances.values().stream()
            .mapToLong(Integer::intValue)
            .sum();
    }

    record Step(Point p, int d) implements Comparable<Step> {

        @Override
        public int compareTo(Step o) {
            return Integer.compare(d, o.d);
        }
    }

    public Map<Point, Integer> buildDistanceMap(Point p0, int expansionFactor) {
        return buildDistanceMap(p0, null, expansionFactor);
    }

    public Map<Point, Integer> buildDistanceMap(Point p0, Set<Point> missingTos, int expansionFactor) {
        Map<Point, Integer> map = new HashMap<>();

        Set<Point> visited = new HashSet<>();
        PriorityQueue<Step> stack = new PriorityQueue<>();
        stack.add(new Step(p0, 0));
        while (!stack.isEmpty()) {
            if (missingTos != null && missingTos.isEmpty()) break;
            Step s = stack.poll();
            char c = board.get(s.p, ' ');
            if (c == ' ') continue;
            if (!visited.add(s.p)) continue;
            if (c == '#') {
                map.put(s.p, s.d);
                if (missingTos != null) missingTos.remove(s.p);
            }
            s.p.neighbors().forEach(
                np -> stack.add(new Step(np, s.d + expandDistance(s.p, np, expansionFactor))));
        }

        return map;
    }

    private int expandDistance(Point from, Point to, int factor) {
        if (freeRows.contains(to.y) && from.y != to.y) return factor;
        if (freeCols.contains(to.x) && from.x != to.x) return factor;
        return 1;
    }

    /**
     * ...Now, instead of the expansion you did before, make each empty
     * row or column one million times larger. That is, each empty row should
     * be replaced with 1000000 empty rows, and each empty column should
     * be replaced with 1000000 empty columns.
     * ...Starting with the same initial image, expand the universe according
     * to these new rules, then find the length of the shortest path between
     * every pair of galaxies.
     * What is the sum of these lengths?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        return evalDistancesSum(1000000);
    }
}
