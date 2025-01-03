package adventofcode.y2024;

import adventofcode.commons.*;
import adventofcode.commons.Vector;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Day 8: Resonant Collinearity
 * https://adventofcode.com/2024/day/8
 */
public class Problem08 extends AoCProblem<Long, Problem08> {

    public static void main(String[] args) throws Exception {
        new Problem08().loadResourceAndSolve(false);
    }

    Board<Character> board;
    Map<Character, List<Point>> antennas = new HashMap<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toCharBoard();
        board.forEach((p, v) -> {
            if (v != '.') {
                antennas.compute(v, AoCCollectionUtils::remapList)
                        .add(p);
            }
            return 0;
        });
    }

    /**
     * ...Calculate the impact of the signal.
     * How many unique locations within the bounds of the map contain an antinode?
     */
    @Override
    public Long solvePartOne() throws Exception {
        Set<Point> antinodes = new HashSet<>();
        searchForAntinodes((p, v) -> {
            Point antinode = p.translate(v);
            if (board.getOrBlank(p, v) != ' ') {
                antinodes.add(antinode);
            }
        });
        // dumpBoard(antinodes);
        return (long) antinodes.size();
    }

    private void searchForAntinodes(BiConsumer<Point, Vector> fn) {
        for (List<Point> group : antennas.values()) {
            if (group.size() < 2) continue;
            for (Point p0 : group) {
                for (Point p1 : group) {
                    if (p0.equals(p1)) continue;
                    Vector v = p0.distance(p1);
                    fn.accept(p0, v);
                }
            }
        }
    }

    private void dumpBoard(Set<Point> antinodes) {
        board.dumpBoard("%s", (c) -> {
            if (antinodes.contains(c.p))
                return '#';
            return c.v;
        });
    }

    /**
     * ...After updating your model, it turns out that an antinode occurs at any
     * grid position exactly in line with at least two antennas of the same frequency,
     * regardless of distance...
     *
     * ...Calculate the impact of the signal using this updated model.
     * How many unique locations within the bounds of the map contain an antinode?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        Set<Point> antinodes = new HashSet<>();
        searchForAntinodes((p, v) -> {
            antinodes.add(p);
            while (board.getOrBlank(p, v) != ' ') {
                p = p.translate(v);
                antinodes.add(p);
            }
        });
        // dumpBoard(antinodes);
        return (long) antinodes.size();
    }
}
