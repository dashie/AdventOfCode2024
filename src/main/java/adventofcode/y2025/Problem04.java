package adventofcode.y2025;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.Board;

/**
 * Day 4: Printing Department
 * https://adventofcode.com/2025/day/4
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
     * ...Consider your complete diagram of the paper roll
     * locations. How many rolls of paper can be accessed
     * by a forklift?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = board.forEach((p, c) -> {
            if (c == '@'
                && board.cell(p).adjacents('@').size() < 4) {
                return 1;
            }
            return 0;
        });
        return result;
    }

    /**
     * ...Stop once no more rolls of paper are accessible
     * by a forklift. In this example, a total of N rolls
     * of paper can be removed.
     *
     * Start with your original diagram. How many rolls of
     * paper in total can be removed by the Elves and their
     * forklifts?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long result = 0;

        Board<Character> tmpBoard = board.clone();
        long removed = 0;
        do {
            removed = board.forEach((p, c) -> {
                if (c == '@'
                    && board.cell(p).adjacents('@').size() < 4) {
                    board.set(p, ' ');
                    return 1;
                }
                return 0;
            });
            result += removed;
        } while (removed > 0);
        return result;
    }
}
