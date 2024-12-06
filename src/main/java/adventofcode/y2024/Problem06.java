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
        return (long) countCells(p0, d0);
    }

    private int countCells(AoCPoint p, AoCVector d) {
        visited.add(p.toString());
        for (; ; ) {
            while (board.get(p, d, '#') != '#') { // go straight until the first obstacle
                p = p.translate(d);
                visited.add(p.toString());
            }
            if (board.get(p.translate(d)) == null) // if out of board (null) then exit
                return visited.size();
            d = d.rotate90L();
        }
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
            if (c == '#' || c == '^') return 0; // if the position is already an obstacle or is the starting pos, return
            if (!visited.contains(p.toString())) return 0; // if the position is not on the default guard path, return
            board.set(p, '#'); // place new obstacle and remove it then
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
            while (board.get(p, d, '#') != '#') { // go straight until the first obstacle
                p = p.translate(d);
                if (!directions.add(p + "-" + d)) // add & check direction on the same position
                    return true; // if the position has been already visited with the same direction we have a loop
            }
            if (board.get(p.translate(d)) == null) // if out of board (null) then exit
                return false;
            d = d.rotate90L();
        }
    }

}
