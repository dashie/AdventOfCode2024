package adventofcode.y2025;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.Point;

import java.util.*;

/**
 * Day 9: Movie Theater
 * https://adventofcode.com/2025/day/9
 */
public class Problem09 extends AoCProblem<Long, Problem09> {

    public static void main(String[] args) throws Exception {
        new Problem09().loadResourceAndSolve(false);
    }

    private List<Point> points;

    @Override
    public void processInput(AoCInput input) throws Exception {

        points = input.lines()
            .map(Point::parsePoint)
            .toList();
    }

    /**
     * ...Using two red tiles as opposite corners,
     * what is the largest area of any rectangle you can make?
     */
    @Override
    public Long solvePartOne() throws Exception {

        Map<Point[], Long> rects = evalAllRects();
        long result = rects.values().stream()
            .sorted(Comparator.reverseOrder())
            .findFirst()
            .get();

        return result;
    }

    private Map<Point[], Long> evalAllRects() {

        Map<Point[], Long> rects = new HashMap<>();
        for (var i = 0; i < points.size(); ++i) {
            for (int j = i + 1; j < points.size(); ++j) {
                var p1 = points.get(i);
                var p2 = points.get(j);
                var dist = p1.distance(p2).absolute();
                long area = (dist.x + 1L) * (dist.y + 1L);
                rects.put(new Point[]{p1, p2}, area);
            }
        }

        return rects;
    }

    /**
     * ...Using two red tiles as opposite corners, what is the
     * largest area of any rectangle you can make using only red
     * and green tiles?
     */
    @Override
    public Long solvePartTwo() throws Exception {

        List<Point[]> segments = evalSegments();
        Map<Point[], Long> rects = evalAllRects();

        long result = rects.entrySet().stream()
            .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
            .filter(r -> !intesect(r.getKey(), segments))
            .findFirst()
            .map(r -> r.getValue())
            .get();

        return result;
    }

    private List<Point[]> evalSegments() {

        List<Point[]> segments = new ArrayList<>();
        for (int i = 0; i < points.size(); ++i) {
            int j = (i + 1) % points.size();
            var p1 = points.get(i);
            var p2 = points.get(j);
            // sort points from origin
            if (p1.x < p2.x || p1.y < p2.y) {
                segments.add(new Point[]{p1, p2});
            } else {
                segments.add(new Point[]{p2, p1});
            }
        }

        return segments;
    }

    /**
     * ATTENTION!! This is a simplification.
     * It does not work for any shape but for for diamond
     * shape of the game.
     */
    private boolean intesect(Point[] rect, List<Point[]> segments) {
        long rMinX = Math.min(rect[0].x, rect[1].x);
        long rMaxX = Math.max(rect[0].x, rect[1].x);
        long rMinY = Math.min(rect[0].y, rect[1].y);
        long rMaxY = Math.max(rect[0].y, rect[1].y);
        for (var s : segments) {
            if (s[1].x > rMinX // maxX of the segment come after minX of the rect
                && s[0].x < rMaxX // minX of the segment come before maxX of the rect
                && s[1].y > rMinY // maxY of the segment come after minY of the rect
                && s[0].y < rMaxY) { // minY of the segment come before maxY of the rect
                return true;
            }
        }
        return false;
    }
}
