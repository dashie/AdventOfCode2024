package adventofcode.y2024;

import adventofcode.commons.*;

/**
 * Day 4: Ceres Search
 * https://adventofcode.com/2024/day/4
 */
public class Problem04 extends AoCProblem<Long, Problem04> {

    public static void main(String[] args) throws Exception {
        new Problem04().loadResourceAndSolve(false);
    }

    private Board<Character> board;

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toCharBoard();
    }

    /**
     * ...Take a look at the little Elf's word search. How many times does XMAS appear?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = board.forEach((p, c) -> {
            int n = 0;
            if (c == 'X') {
                n += searchXmas(p, Vector.NORTH);
                n += searchXmas(p, Vector.EAST);
                n += searchXmas(p, Vector.SOUTH);
                n += searchXmas(p, Vector.WEST);
                n += searchXmas(p, Vector.NE);
                n += searchXmas(p, Vector.NW);
                n += searchXmas(p, Vector.SE);
                n += searchXmas(p, Vector.SW);
            }
            return n;
        });
        return result;
    }

    private int searchXmas(Point p, Vector v) {
        if (board.getOrBlank(p, v) != 'M') return 0;
        if (board.getOrBlank(p, v.mul(2)) != 'A') return 0;
        if (board.getOrBlank(p, v.mul(3)) != 'S') return 0;
        return 1;
    }

    /**
     * ...Flip the word search from the instructions back over to the word search side and try again.
     * How many times does an X-MAS appear?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long result = board.forEach((p, c) -> {
            return c == 'A' && checkMaxCross(p) ? 1 : 0;
        });
        return result;
    }

    private boolean checkMaxCross(Point p) {
        if ((board.getOrBlank(p, 1, 1) == 'M'
            && board.getOrBlank(p, -1, -1) == 'S')
            ||
            (board.getOrBlank(p, 1, 1) == 'S'
                && board.getOrBlank(p, -1, -1) == 'M')) {

            if ((board.getOrBlank(p, -1, 1) == 'M'
                && board.getOrBlank(p, 1, -1) == 'S')
                ||
                (board.getOrBlank(p, -1, 1) == 'S'
                    && board.getOrBlank(p, 1, -1) == 'M')) {
                return true;
            }
        }
        return false;
    }
}
