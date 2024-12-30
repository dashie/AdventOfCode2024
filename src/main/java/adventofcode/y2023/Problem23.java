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
public class Problem23 extends AoCProblem<Long, Problem23> {

    public static void main(String[] args) throws Exception {
        new Problem23().loadResourceAndSolve(false);
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
        long longestPath = findLongestPath(g, START, new HashSet<>());
        return longestPath;
    }

    public long findLongestPath(Map<Point, Vertex> g, Point p, Set<Point> visited) {
        if (END.equals(p)) return 0;
        if (!visited.add(p)) return -1;

        long longestPath = -1;
        Vertex v = g.get(p);

        if (v.edges.containsKey(END)) {
            // The END vertex is connected to the graph through a single edge.
            // Therefore, once we reach that edge, we must always choose the
            // path towards END; otherwise, we skip the vertex and waste time
            // on an unnecessary traversal.
            longestPath = v.edges.get(END).cost;
        } else {
            for (var e : v.edges.values()) {
                long cost = findLongestPath(g, e.p, visited);
                if (cost != -1) longestPath = Math.max(longestPath, cost + e.cost);
            }
        }

        visited.remove(p);
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
        long longestPath = findLongestPath(g, START, new HashSet<>());
        return longestPath;
    }
}
