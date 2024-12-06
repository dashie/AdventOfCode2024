package adventofcode.y2024;

import adventofcode.commons.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Day 6: Guard Gallivant
 * https://adventofcode.com/2024/day/6
 */
public class Problem06 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem06().solve(false);
    }

    private AoCBoard<Character> board;
    private Set<String> visited = new HashSet<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        this.board = input.toCharBoard();
    }

    /**
     * ...Predict the path of the guard. How many distinct positions will the guard
     * visit before leaving the mapped area?
     */
    @Override
    protected Long partOne() throws Exception {
        AoCVector d0 = AoCVector.SOUTH;
        AoCPoint p0 = board.searchFor('^');
        return countCells(p0, d0);
    }

    private long countCells(AoCPoint p, AoCVector d) {
        visited.add(p.toString());
        for (; ; ) {
            while (board.getSafeWithDeafult(p, d, '#') != '#') {
                p = p.traslate(d);
                visited.add(p.toString());
            }
            if (board.getSafe(p.traslate(d)) == null) {
                break;
            }
            d = d.rotate90L();
        }
        return (long) visited.size();
    }

    /**
     * ...You need to get the guard stuck in a loop by adding a single new obstruction.
     * How many different positions could you choose for this obstruction?
     */
    @Override
    protected Long partTwo() throws Exception {
        final AoCVector d0 = AoCVector.SOUTH;
        final AoCPoint p0 = board.searchFor('^');
        long loopsCount = board.forEach((p, c) -> {
            if (c == '#' || c == '^') return 0;
            if (!visited.contains(p.toString())) return 0;
            board.set(p, '#');
            try {
                return findLoops(p0, d0) ? 1 : 0;
            } finally {
                board.set(p, '.');
            }
        });
        return loopsCount;
    }

    private boolean findLoops(AoCPoint p, AoCVector d) {
        Set<String> directions = new HashSet<>(); // visit cells with directions
        directions.add(p + "-" + d);
        for (; ; ) {
            while (board.getSafeWithDeafult(p, d, '#') != '#') {
                p = p.traslate(d);
                directions.add(p + "-" + d);
            }
            if (board.getSafe(p.traslate(d)) == null)
                break;
            d = d.rotate90L();
            if (directions.contains(p.traslate(d) + "-" + d))
                return true;
        }
        return false;
    }

}
