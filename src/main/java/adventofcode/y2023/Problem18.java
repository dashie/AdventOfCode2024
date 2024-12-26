package adventofcode.y2023;

import adventofcode.commons.*;

import java.util.*;

import static adventofcode.commons.AoCVector.charUDRLToDirection;

/**
 * Day 18: Lavaduct Lagoon
 * https://adventofcode.com/2023/day/18
 */
public class Problem18 extends AoCProblem<Long, Problem18> {

    public static void main(String[] args) throws Exception {
        new Problem18().loadResourceAndSolve(false);
    }

    private List<AoCVector> moves = new ArrayList<>();
    private List<String> colors = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        for (MatcherEx m : input.pattern("(.) ([0-9]+) \\(#(.+)\\)").toList()) {
            AoCVector d = charUDRLToDirection(m.get(1));
            d = d.mul(m.getInt(2));
            moves.add(d);
            String color = m.get(3);
            colors.add(color);
        }
    }

    /**
     * ...The Elves are concerned the lagoon won't be large enough;
     * if they follow their dig plan, how many cubic meters of lava could it hold?
     */
    @Override
    public Long solvePartOne() throws Exception {
        // First part is solved with fill algorithm.
        return evalShapeSizeWithFillAlgorithm(moves);
    }

    private long evalShapeSizeWithFillAlgorithm(List<AoCVector> moves) {
        AoCPoint p0 = new AoCPoint(0, 0);
        Map<AoCPoint, Integer> shape = drawShape(moves);

        AoCRect rect = boundingRect(shape);
        rect = rect.expand(1);
        fill(rect.p1, rect, 0, shape);
        // dumpBoard(shape, p0);

        // count shape size
        AoCBoard<Integer> board = AoCBoard.from(shape, Integer.class);
        return (long) board.forEach((p, v) -> v == null || v == 1 ? 1 : 0);
    }

    private Map<AoCPoint, Integer> drawShape(List<AoCVector> moves) {
        Map<AoCPoint, Integer> shape = new HashMap<>();
        AoCPoint from = new AoCPoint(0, 0);
        for (AoCVector d : moves) {
            for (AoCPoint p : from.follow(d)) {
                shape.put(p, 1);
                from = p;
            }
        }
        return shape;
    }

    private AoCRect boundingRect(Map<AoCPoint, Integer> points) {
        int x1 = Integer.MAX_VALUE, x2 = Integer.MIN_VALUE;
        int y1 = Integer.MAX_VALUE, y2 = Integer.MIN_VALUE;
        for (AoCPoint p : points.keySet()) {
            if (p.y < y1) y1 = p.y;
            if (p.y > y2) y2 = p.y;
            if (p.x < x1) x1 = p.x;
            if (p.x > x2) x2 = p.x;
        }
        return AoCRect.of(x1, y1, x2, y2);
    }

    private void fill(AoCPoint p0, AoCRect rect, Integer v, Map<AoCPoint, Integer> shape) {
        LinkedList<AoCPoint> points = new LinkedList<>();
        points.add(p0);
        while (!points.isEmpty()) {
            AoCPoint p = points.pollFirst();
            if (p.isOut(rect)) continue;
            if (shape.containsKey(p)) continue;
            shape.put(p, 0);
            for (AoCPoint np : p.neighbors())
                points.add(np);
        }
    }

    private static void dumpBoard(Map<AoCPoint, Integer> shape, AoCPoint p0) {
        AoCBoard.dumpBoard("shape", shape, (p, v) -> {
            if (p == p0) return "@";
            if (v == null) return " ";
            return switch (v) {
                case 0 -> ".";
                case 1 -> "#";
                default -> throw new IllegalStateException();
            };
        });
    }

    /**
     * ...Convert the hexadecimal color codes into the correct instructions;
     * if the Elves follow this new dig plan, how many cubic meters of lava
     * could the lagoon hold?
     */
    @Override
    public Long solvePartTwo() throws Exception {

        List<AoCVector> moves = new ArrayList<>();
        colors.forEach(str -> {
            int len = Integer.valueOf(str.substring(0, 5), 16);
            int direction = Integer.valueOf(str.substring(5, 6), 16).intValue();
            AoCVector d = switch (direction) { // 0 means R, 1 means D, 2 means L, and 3 means U
                case 0 -> AoCVector.EAST;
                case 1 -> AoCVector.SOUTH;
                case 2 -> AoCVector.WEST;
                case 3 -> AoCVector.NORTH;
                default -> throw new IllegalStateException();
            };
            d = d.mul(len);
            moves.add(d);
        });

        // Using Shoelace formula for the internal area
        // https://en.wikipedia.org/wiki/Shoelace_formula
        // And specifically the Pick's theorem, because we are in
        // integer's math and we want the border perimeter too
        // https://en.wikipedia.org/wiki/Pick%27s_theorem
        //
        // example:
        // rectangle described by 4 points (0,5),(10, 0),(0, -5),(-10, 0)
        // total pixels = 66
        // internal area = 10 * 5 = 50
        // perimeter = 10 + 5 + 10 + 5 = 30
        // area = internal area + perimeter/2 + 1 (corner pixel);

        long internalArea = 0;
        var p = AoCPoint.of(0, 0);
        for (var d : moves) {
            var p1 = p.translate(d);
            long a1 = (long) p.x * (long) p1.y - (long) p.y * (long) p1.x;
            internalArea += a1;
            p = p1;
        }
        internalArea = Math.abs(internalArea) / 2;

        // add perimeter because we need to add also the border pixels
        // but the area from Shoelace_formula does not count it
        // (then it is divided by 2)
        long perimeter = moves.stream().mapToLong(AoCVector::manhattam).sum();
        perimeter /= 2;

        long totalArea = internalArea + perimeter + 1;
        return totalArea;
    }
}
