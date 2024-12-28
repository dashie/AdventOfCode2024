package adventofcode.y2023;

import adventofcode.commons.AoCBoard;
import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCPoint;
import adventofcode.commons.AoCProblem;

import java.util.*;

/**
 * Day 10: Pipe Maze
 * https://adventofcode.com/2023/day/10
 */
public class Problem10 extends AoCProblem<Long, Problem10> {

    public static void main(String[] args) throws Exception {
        new Problem10().loadResourceAndSolve(false);
    }

    AoCBoard<Character> board;

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toCharBoard();
    }

    /**
     * ...Find the single giant loop starting at S. How many steps
     * along the loop does it take to get from the starting position
     * to the point farthest from the starting position?
     */
    @Override
    public Long solvePartOne() throws Exception {
        return evalFarthestPointDistance(new HashSet<>());
    }

    record Step(AoCPoint p, long distance) {}

    public long evalFarthestPointDistance(Set<AoCPoint> visited) {
        var p0 = board.searchFor('S');

        long farthestDistance = 0;

        Deque<Step> stack = new LinkedList<>();
        stack.add(new Step(p0, 0));
        while (!stack.isEmpty()) {
            Step s = stack.pollFirst();
            if (!visited.add(s.p)) continue;
            if (s.distance > farthestDistance) farthestDistance = s.distance;
            connectedPoints(s.p, resolveCell(s.p))
                .forEach(p -> stack.add(new Step(p, s.distance + 1)));
        }

        return farthestDistance;
    }

    public Character resolveCell(AoCPoint p) {
        Character c = board.get(p, '.');
        if (c != 'S') return c;
        Character n = board.get(p.north(), '.');
        Character s = board.get(p.south(), '.');
        Character e = board.get(p.east(), '.');
        Character w = board.get(p.west(), '.');
        //   7|F   L   J            7|F                  7|F
        //   (|)   -(-)-   (F)-7J   (L)-7J   FL-(7)   FL-(J)
        //   J|L   F   7   J|L                  J|L
        if ("7|F".indexOf(s) != -1 && "J|L".indexOf(n) != -1) return '|';
        if ("L-F".indexOf(w) != -1 && "J-7".indexOf(n) != -1) return '-';
        if ("J|L".indexOf(n) != -1 && "-7J".indexOf(e) != -1) return 'F';
        if ("7|F".indexOf(s) != -1 && "-7J".indexOf(e) != -1) return 'L';
        if ("FL-".indexOf(w) != -1 && "J|L".indexOf(n) != -1) return '7';
        if ("7|F".indexOf(s) != -1 && "FL-".indexOf(w) != -1) return 'J';
        throw new IllegalStateException();
    }

    public List<AoCPoint> connectedPoints(AoCPoint p, Character c) {
        return switch (c) {
            case '|' -> List.of(p.north(), p.south());
            case '-' -> List.of(p.east(), p.west());
            case 'L' -> List.of(p.south(), p.east());
            case 'J' -> List.of(p.south(), p.west());
            case '7' -> List.of(p.north(), p.west());
            case 'F' -> List.of(p.north(), p.east());
            default -> throw new IllegalStateException();
        };
    }

    /**
     * ...Figure out whether you have time to search for the nest
     * by calculating the area within the loop. How many tiles are
     * enclosed by the loop?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        Set<AoCPoint> perimeter = new HashSet<>();
        evalFarthestPointDistance(perimeter);

        Set<AoCPoint> points = board.pointSet();
        points.removeAll(perimeter);

        Set<AoCPoint> insideArea = new HashSet<>();
        Set<AoCPoint> outsideArea = new HashSet<>();
        evalAreaInsideOutsideOfPerimeter(points, perimeter, insideArea, outsideArea);
        // dumpSolutionArea(perimeter, insideArea);

        return (long) insideArea.size();
    }

    public long evalAreaInsideOutsideOfPerimeter(Set<AoCPoint> points, Set<AoCPoint> perimeter, Set<AoCPoint> insideArea, Set<AoCPoint> outsideArea) {
        while (!points.isEmpty()) {
            evalAreaInsideOutsideOfPerimeterFill(
                points.iterator().next(),
                perimeter,
                insideArea,
                outsideArea);
            points.removeAll(insideArea);
            points.removeAll(outsideArea);
        }
        return insideArea.size();
    }

    private void evalAreaInsideOutsideOfPerimeterFill(AoCPoint p0, Set<AoCPoint> perimeter, Set<AoCPoint> insideArea, Set<AoCPoint> outsideArea) {
        boolean isOutOfPerimeter = isOutOfShape(p0, perimeter);
        Set<AoCPoint> visited = isOutOfPerimeter ? outsideArea : insideArea;

        Deque<AoCPoint> stack = new LinkedList<>();
        stack.add(p0);
        while (!stack.isEmpty()) {
            AoCPoint p = stack.pollFirst();
            char c = board.get(p, ' ');
            if (c == ' ') {
                // hit board border, we are 100% out of perimeter
                if (isOutOfPerimeter == false)
                    throw new IllegalStateException("Out of border from a point inside area, p0=%s p=%s".formatted(p0, p));
                continue;
            }
            if (perimeter.contains(p)) continue;
            if (!visited.add(p)) continue;
            p.neighbors().forEach(stack::add);
        }
    }

    private boolean isOutOfShape(AoCPoint p0, Set<AoCPoint> perimeter) {
        // moving the point to right, counts the times it crosses the perimeter
        // if the count is odd the point is inside the shape
        // id the count is even the point is outside the shape
        // because we are on a discrete plane, we pay attention to border cases
        // like "F-J" or "L-7" (here the cross is counted one)
        long countRightPerimeterCrosses = 0;
        char crossAt = ' ';
        for (AoCPoint p = p0; p.x < board.N; p = p.east()) {
            char c = board.get(p);
            if (perimeter.contains(p)) {
                if ("-".indexOf(c) != -1) {
                    // ignore, follow perimeter horizontal line
                } else if ((crossAt == 'F' && c == 'J') || (crossAt == 'L' && c == '7')) {
                    // ignore, end of perimeter horizontal line
                    crossAt = ' ';
                } else if (crossAt == ' ' && "FL".indexOf(c) != -1) { // start a new horizontal part F or L
                    crossAt = c;
                    countRightPerimeterCrosses++;
                } else {
                    crossAt = ' ';
                    countRightPerimeterCrosses++;
                }
            } else {
                crossAt = ' ';
            }
        }
        return countRightPerimeterCrosses % 2 == 0;
    }

    private void dumpSolutionArea(Set<AoCPoint> perimeter, Set<AoCPoint> insideArea) {
        board.dumpBoard("%c", (c -> {
            if (perimeter.contains(c.p)) return c.v;
            if (insideArea.contains(c.p)) return '•';
            return ' ';
        }));
    }
}
