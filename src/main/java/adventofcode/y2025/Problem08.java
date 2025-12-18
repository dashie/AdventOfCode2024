package adventofcode.y2025;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.Point;

import java.util.*;

/**
 * Day 8: Playground
 * https://adventofcode.com/2025/day/8
 */
public class Problem08 extends AoCProblem<Long, Problem08> {

    public static void main(String[] args) throws Exception {
        new Problem08().loadResourceAndSolve(false);
    }

    private int CONNECTION_COUNTS;
    private List<Point> boxes = null;

    @Override
    public void processInput(AoCInput input) throws Exception {
        if (isUsingSampleResource()) {
            CONNECTION_COUNTS = 10;
        } else {
            CONNECTION_COUNTS = 1000;
        }
        boxes = input.lines()
            .map(Point::parsePoint)
            .toList();
    }

    /**
     * What's the actual password to open the door?
     */
    @Override
    public Long solvePartOne() throws Exception {

        Map<Point[], Double> distances = new HashMap<>();
        for (int i = 0; i < boxes.size(); ++i) {
            for (int j = i + 1; j < boxes.size(); ++j) {
                Point[] k = {boxes.get(i), boxes.get(j)};
                double dist = k[0].distance(k[1]).module();
                distances.put(k, dist);
            }
        }

        List<Map.Entry<Point[], Double>> sortedPairs = distances.entrySet()
            .stream()
            .sorted(Comparator.comparingDouble(Map.Entry::getValue))
            .toList();

        Map<Point, Point> point2circuit = new HashMap<>();
        Map<Point, Set<Point>> circuit2points = new HashMap<>();

        int count = 0;
        for (var pair : sortedPairs) {
            if (count++ >= CONNECTION_COUNTS) break;

            var p1 = pair.getKey()[0];
            var p2 = pair.getKey()[1];
            var c1 = point2circuit.get(p1);
            var c2 = point2circuit.get(p2);
            if (c1 == null & c2 == null) {
                // create new circuit
                point2circuit.put(p1, p1);
                point2circuit.put(p2, p1);
                circuit2points.computeIfAbsent(p1, k -> new HashSet<>())
                    .addAll(Arrays.asList(p1, p2));
            } else if (c1 != null && c2 == null) {
                // add p2 to p1 circuit
                point2circuit.put(p2, c1);
                circuit2points.get(c1)
                    .add(p2);
            } else if (c1 == null && c2 != null) {
                // add p1 to p2 circuit
                point2circuit.put(p1, c2);
                circuit2points.get(c2)
                    .add(p1);
            } else if (!c1.equals(c2)) {
                // merge c2 into c1
                for (var p : circuit2points.get(c2)) {
                    point2circuit.put(p, c1);
                    circuit2points.get(c1).add(p);
                }
                circuit2points.remove(c2);
            } else {
                // do nothing, they are alredy in the same circuit
            }
        }

        // sort circuits by size
        var circuitSizes = circuit2points.values()
            .stream()
            .map(s -> s.size())
            .sorted(Comparator.reverseOrder())
            .toList();

        // multiply 3 top sizes
        long result = circuitSizes.stream()
            .limit(3)
            .reduce((acc, v) -> acc * v)
            .get();

        return result;
    }

    /**
     * Continue connecting the closest unconnected pairs of
     * junction boxes together until they're all in the same circuit.
     * What do you get if you multiply together the X coordinates of
     * the last two junction boxes you need to connect?
     */
    @Override
    public Long solvePartTwo() throws Exception {

        Map<Point[], Double> distances = new HashMap<>();
        for (int i = 0; i < boxes.size(); ++i) {
            for (int j = i + 1; j < boxes.size(); ++j) {
                Point[] k = {boxes.get(i), boxes.get(j)};
                double dist = k[0].distance(k[1]).module();
                distances.put(k, dist);
            }
        }

        List<Map.Entry<Point[], Double>> sortedPairs = distances.entrySet()
            .stream()
            .sorted(Comparator.comparingDouble(Map.Entry::getValue))
            .toList();

        Map<Point, Point> point2circuit = new HashMap<>();
        Map<Point, Set<Point>> circuit2points = new HashMap<>();

        for (var pair : sortedPairs) {

            var p1 = pair.getKey()[0];
            var p2 = pair.getKey()[1];
            var c1 = point2circuit.get(p1);
            var c2 = point2circuit.get(p2);
            if (c1 == null & c2 == null) {
                // create new circuit
                point2circuit.put(p1, p1);
                point2circuit.put(p2, p1);
                circuit2points.computeIfAbsent(p1, k -> new HashSet<>())
                    .addAll(Arrays.asList(p1, p2));
            } else if (c1 != null && c2 == null) {
                // add p2 to p1 circuit
                point2circuit.put(p2, c1);
                circuit2points.get(c1)
                    .add(p2);
            } else if (c1 == null && c2 != null) {
                // add p1 to p2 circuit
                point2circuit.put(p1, c2);
                circuit2points.get(c2)
                    .add(p1);
            } else if (!c1.equals(c2)) {
                // merge c2 into c1
                for (var p : circuit2points.get(c2)) {
                    point2circuit.put(p, c1);
                    circuit2points.get(c1).add(p);
                }
                circuit2points.remove(c2);
            } else {
                // do nothing, they are alredy in the same circuit
            }

            // all boxes are connected in one circuit
            if (circuit2points.size() == 1
                && circuit2points.values().stream().findFirst().get().size() == boxes.size()) {
                return (long) p1.x * (long) p2.x;
            }
        }

        throw new IllegalStateException("Cannot reduce all circuits to one only circuit");
    }
}