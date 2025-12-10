package adventofcode.y2023;

import adventofcode.commons.Vector;
import adventofcode.commons.*;

import java.util.*;

import static adventofcode.commons.DirectedPoint.Direction.*;
import static adventofcode.commons.Vector.*;
import static java.util.Collections.EMPTY_MAP;

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
        // perimeter score optimization works only on part 2
        long longestPath = findLongestPath(START, g, false);
        return longestPath;
    }

    record FindLongestPathStep(Point p, long steps, FindLongestPathStep prev, boolean lastSibling) {}

    public long findLongestPath(Point p0, Map<Point, Vertex> g, boolean usePerimeterScore) {
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
                for (var e : v.edges.values()) {
                    if (usePerimeterScore && v.perimeterScore != -1 && v.edges.size() < 4) { // perimeter vertex
                        if (g.get(e.p).perimeterScore > v.perimeterScore) continue;
                    }
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

    private void dumpGraph(Map<Point, Vertex> g) {
        // visualize graph with command:
        // dot Problem23.dot -Tpng -oProblem23.png -Kfdp -Gdpi=300
        log("%ndigraph G {%n");
        for (var ve : g.entrySet()) {
            log("\"%s\"[label=\"%s\\n%s\"]%n", ve.getKey(), ve.getKey(), ve.getValue().perimeterScore);
        }
        for (var ve : g.entrySet()) {
            for (var e : ve.getValue().edges.values())
                log("\"%s\" -> \"%s\"%n", ve.getKey(), e.p);
        }
        log("}%n%n");
    }

    record Edge(Point p, int cost) {}

    record Vertex(Point p, char c, Integer perimeterScore, Map<Point, Edge> edges) {}

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

        // eval perimeter score
        g.put(END, new Vertex(END, '.', 0, EMPTY_MAP));
        evalPerimeterScore(g);

        //
        return g;
    }

    private void evalPerimeterScore(Map<Point, Vertex> g) {
        // assign a score on perimeter nodes (see the file Problem23.png)
        // when a visits go on perimeter node it has to go only to the END,
        // or if it goes far from END the visits finishes always in a loop.

        // FIME improve the way we eval the perimeter score
        boolean buildPerimeterScore = true;
        while (buildPerimeterScore) {
            buildPerimeterScore = false;
            for (var entry : g.entrySet()) {
                var vertex = entry.getValue();
                if (vertex.perimeterScore != null) continue;
                if (vertex.c != '.' || vertex.edges.size() > 3) {
                    g.put(vertex.p, new Vertex(vertex.p, vertex.c, -1, vertex.edges));
                } else {
                    for (var edge : vertex.edges.values()) {
                        var vertex1 = g.get(edge.p);
                        if (vertex1.perimeterScore != null && vertex1.perimeterScore != -1) {
                            buildPerimeterScore = true;
                            g.put(vertex.p, new Vertex(vertex.p, vertex.c, vertex1.perimeterScore + 1, vertex.edges));
                            break;
                        }
                    }
                }
            }
        }
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

        return new Vertex(p0, vc, null, edges);
    }


    /**
     * ...Find the longest hike you can take through the surprisingly
     * dry hiking trails listed on your map. How many steps long is the
     * longest hike?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        Map<Point, Vertex> g = reduceGraph(true);
        // dumpGraph(g);
        long longestPath = findLongestPath(START, g, true);
        return longestPath;
    }
}
