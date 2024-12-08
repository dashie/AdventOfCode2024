package adventofcode.y2024;

import adventofcode.commons.*;

import java.util.*;

/**
 * Day 8: Resonant Collinearity
 * https://adventofcode.com/2024/day/8
 */
public class Problem08 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem08().solve(false);
    }

    AoCBoard<Character> board;
    Map<Character, List<AoCPoint>> signals = new HashMap<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toCharBoard();
        board.forEach((p, v) -> {
            if (v != '.') {
                signals.compute(v, (k, s) -> s == null ? new ArrayList<>() {} : s)
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
    public Long partOne() throws Exception {
        Set<AoCPoint> antinodes = new HashSet<>();
        for (List<AoCPoint> group : signals.values()) {
            if (group.size() < 2) continue;
            for (int i = 0; i < group.size(); i++) {
                for (int j = 0; j < group.size(); j++) {
                    if (i == j) continue;
                    AoCPoint p0 = group.get(i);
                    AoCPoint p1 = group.get(j);
                    AoCVector v = p1.distanceVector(p0);
                    AoCPoint antinode = p0.translate(v.rotate180());
                    if (board.getOrBlank(antinode) != ' ') {
                        antinodes.add(antinode);
                    }
                }
            }
        }
        // dumpBoard();
        return (long) antinodes.size();
    }

    private void dumpBoard(Set<AoCPoint> antinodes) {
        board.dumpBoard("%s", (c) -> {
            if (antinodes.contains(c.p)) {
                return '#';
            }
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
    public Long partTwo() throws Exception {
        Set<AoCPoint> antinodes = new HashSet<>();
        for (List<AoCPoint> group : signals.values()) {
            if (group.size() < 2) continue;
            for (int i = 0; i < group.size(); i++) {
                for (int j = 0; j < group.size(); j++) {
                    if (i == j) continue;
                    AoCPoint p0 = group.get(i);
                    AoCPoint p1 = group.get(j);
                    antinodes.add(p0);
                    antinodes.add(p1);
                    AoCVector v = p1.distanceVector(p0).rotate180();
                    AoCPoint antinode = p0.translate(v);
                    while (board.getOrBlank(antinode) != ' ') {
                        antinodes.add(antinode);
                        antinode = antinode.translate(v);
                    }
                }
            }
        }
        // dumpBoard(antinodes);
        return (long) antinodes.size();
    }
}
