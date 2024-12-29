package adventofcode.y2023;

import adventofcode.commons.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Day 11: Cosmic Expansion
 * https://adventofcode.com/2023/day/11
 */
public class Problem11 extends AoCProblem<Long, Problem11> {

    public static void main(String[] args) throws Exception {
        new Problem11().loadResourceAndSolve(false);
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
            .toList();

        long result = 0;
        for (int i = 0; i < galaxies.size(); ++i) {
            for (int j = i; j < galaxies.size(); ++j) {
                result += evalDistance(galaxies.get(i), galaxies.get(j), expansionFactor);
            }
        }
        return result;
    }

    public long evalDistance(Point p1, Point p2, int expansionFactor) {
        long distance = 0;
        Vector dir = p2.distance(p1).signs();

        var x = p1.x;
        while (x != p2.x) {
            x += dir.x;
            distance += freeCols.contains(x) ? expansionFactor : 1;
        }

        var y = p1.y;
        while (y != p2.y) {
            y += dir.y;
            distance += freeRows.contains(y) ? expansionFactor : 1;
        }

        return distance;
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
