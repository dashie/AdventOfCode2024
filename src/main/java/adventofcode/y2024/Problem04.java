package adventofcode.y2024;

import adventofcode.commons.*;

/**
 * Day 4: Ceres Search
 * https://adventofcode.com/2024/day/4
 */
public class Problem04 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem04().solve(false);
    }

    private AoCBoard<Character> board;

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toCharBoard();
    }

    /**
     * ...Take a look at the little Elf's word search. How many times does XMAS appear?
     */
    @Override
    protected Long partOne() throws Exception {
        long result = board.forEach((p, c) -> {
            int n = 0;
            if (c == 'X') {
                n += searchXmas(p, AoCVector.NORTH);
                n += searchXmas(p, AoCVector.EAST);
                n += searchXmas(p, AoCVector.SOUTH);
                n += searchXmas(p, AoCVector.WEST);
                n += searchXmas(p, AoCVector.NE);
                n += searchXmas(p, AoCVector.NW);
                n += searchXmas(p, AoCVector.SE);
                n += searchXmas(p, AoCVector.SW);
            }
            return n;
        });
        return result;
    }

    private int searchXmas(AoCPoint p0, AoCVector v) {
        AoCPoint p = p0.clone();
        if (board.getOrBlank(p.traslate(v)) != 'M') return 0;
        if (board.getOrBlank(p.traslate(v)) != 'A') return 0;
        if (board.getOrBlank(p.traslate(v)) != 'S') return 0;
        return 1;
    }

    /**
     * ...Flip the word search from the instructions back over to the word search side and try again.
     * How many times does an X-MAS appear?
     */
    @Override
    protected Long partTwo() throws Exception {
        long result = board.forEach((p, c) -> {
            int n = 0;
            if (c == 'A') {
                if (checkMaxCross(p)) {
                    n++;
                }
            }
            return n;
        });
        return result;
    }

    private boolean checkMaxCross(AoCPoint p) {
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
