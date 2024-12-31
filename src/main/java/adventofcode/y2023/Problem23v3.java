package adventofcode.y2023;

import adventofcode.commons.Vector;
import adventofcode.commons.*;

import java.util.*;

import static adventofcode.commons.DirectedPoint.Direction.*;
import static adventofcode.commons.Vector.*;

/**
 * Day 23: A Long Walk
 * https://adventofcode.com/2023/day/23
 */
public class Problem23v3 extends AoCProblem<Long, Problem23v3> {

    public static void main(String[] args) throws Exception {
        new Problem23v3().loadResourceAndSolve(false);
    }

    Board<Character> board;
    Point START;
    Point END;

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toCharBoard();
        START = Point.of(1, 0);
        END = Point.of(board.N - 2, board.M - 1);
    }

    /**
     * ...Find the longest hike you can take through the hiking
     * trails listed on your map. How many steps long is the longest
     * hike?
     */
    @Override
    public Long solvePartOne() throws Exception {
        Map<Point, Vertex> g = reduceGraph(false);
        long longestPath = findLongestPath(START, g);
        return longestPath;
    }

    record FindLongestPathStep(Point p, long steps, FindLongestPathStep prev, boolean lastSibling) {}

    public long findLongestPath(Point p0, Map<Point, Vertex> g) {
        long longestPath = -1;

        Set<Point> visited = new HashSet<>();
        Deque<FindLongestPathStep> stack = new LinkedList<>();
        stack.add(new FindLongestPathStep(p0, 0, null, true));

        while (!stack.isEmpty()) {
            var s = stack.pop();
            boolean firstVisit = true;
            boolean endOfDFS = true;
            try {

                if (!visited.add(s.p)) {
                    // we are in a loop and we want to exit without to touch visited
                    firstVisit = false;
                    continue;
                }

                if (END.equals(s.p)) {
                    if (s.steps > longestPath) {
                        longestPath = s.steps;
                    }
                    continue;
                }

                Vertex v = g.get(s.p);

                var vend = v.edges.get(END);
                if (vend != null) {
                    if (s.steps + vend.cost > longestPath) {
                        longestPath = s.steps + vend.cost;
                    }
                    continue;
                }

                for (var e : v.edges.values()) {
                    // mark the first node as the node that needs to clean the visited
                    stack.push(new FindLongestPathStep(e.p, s.steps + e.cost, s, endOfDFS));
                    endOfDFS = false;
                }

            } finally {

                if (endOfDFS == true) { // is terminal node for the DFS, no children, node visit is completed
                    if (firstVisit) visited.remove(s.p);

                    // backtracking
                    var prevs = s;
                    while (prevs != null && prevs.lastSibling) {
                        if (prevs.prev != null) visited.remove(prevs.prev.p);
                        prevs = prevs.prev;
                    }
                }
            }
        }
        return longestPath;
    }

    record Edge(Point p, int cost) {}

    record Vertex(Point p, char c, Map<Point, Edge> edges) {}

    public Map<Point, Vertex> reduceGraph(boolean climb) {
        // find critical nodes
        Set<Point> criticalNodes = new HashSet<>();
        for (var cell : board.cells()) {
            if (cell.v == '#') continue; // skip walls

            boolean isVertex =
                START.equals(cell.p) || END.equals(cell.p)
                    || (!climb && "^v<>".indexOf(cell.v) != -1) // ignore slopes if climb is enable
                    || cell.neighbors('.', '<', '>', '^', 'v').size() > 2;

            if (isVertex) criticalNodes.add(cell.p);
        }

        // edges to END count
        int edgesToEnd = 0;

        // build graph
        Map<Point, Vertex> g = new HashMap<>();
        for (var cn : criticalNodes) {
            if (END.equals(cn)) continue; // END accepts only in connections, so we don't create END vertex
            Vertex v = buildVertex(cn, climb, criticalNodes);
            if (v.edges.containsKey(END)) edgesToEnd++;
            g.put(cn, v);
        }

        // check that END has one single edge for a future traversal optimization
        if (edgesToEnd > 1) throw new IllegalStateException("Invalid number of edges on END");

        //
        return g;
    }

    record BuildVertexStep(DirectedPoint dp, int steps) {}

    public Vertex buildVertex(Point p0, boolean climb, Set<Point> criticalNodes) {
        char vc = board.get(p0);

        Map<Point, Edge> edges = new HashMap<>();

        Set<Point> visited = new HashSet<>();
        Deque<BuildVertexStep> stack = new LinkedList<>();
        Vector.DIRECTIONS.forEach(d ->
            stack.add(new BuildVertexStep(DirectedPoint.of(p0, d).moveFront(), 1)));

        while (!stack.isEmpty()) {
            var s = stack.poll();
            var c = board.get(s.dp.p, '#');
            if (c == '#') continue;
            if (START.equals(s.dp.p)) continue; // start accept only out edges
            if (criticalNodes.contains(s.dp.p)) {
                if (!climb) {
                    if (c == '>' && s.dp.d.is(WEST)) continue;
                    if (c == '<' && s.dp.d.is(EAST)) continue;
                    if (c == 'v' && s.dp.d.is(SOUTH)) continue;
                    if (c == '^' && s.dp.d.is(NORTH)) continue;
                }
                if (edges.put(s.dp.p, new Edge(s.dp.p, s.steps)) != null)
                    throw new IllegalStateException(); // or keep the longest one
                continue;
            }
            if (!visited.add(s.dp.p)) continue;
            s.dp.move(FRONT, RIGHT, LEFT).forEach(ndp ->
                stack.add(new BuildVertexStep(ndp, s.steps + 1)));
        }

        return new Vertex(p0, vc, edges);
    }


    /**
     * ...Find the longest hike you can take through the surprisingly
     * dry hiking trails listed on your map. How many steps long is the
     * longest hike?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        Map<Point, Vertex> g = reduceGraph(true);
        long longestPath = findLongestPath(START, g);
        return longestPath;
    }
}
