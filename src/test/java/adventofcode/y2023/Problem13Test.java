package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import adventofcode.commons.AoCBoard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Problem13Test extends AbstractProblemTest<Long> {

    public Problem13Test() {
        super(Problem13.class);
    }

    @Test
    public void testHorizontalSymmetry() throws Exception {
        Problem13 problem = _newProblemInstance();
        AoCBoard<Character> board = AoCBoard.from("""
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#""");
        List<Long> bitset = problem.patternToBitset(board.rows());
        assertEquals(0, problem.evalReflectionDiffScore(bitset, 4));
    }

    @Test
    public void testVerticalSymmetry() throws Exception {
        Problem13 problem = _newProblemInstance();
        AoCBoard<Character> board = AoCBoard.from("""
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.""");
        List<Long> bitset = problem.patternToBitset(board.cols());
        assertEquals(0, problem.evalReflectionDiffScore(bitset, 5));
    }

    @Test
    public void testPart1WithSample() throws Exception {
        Problem13 problem = _newProblemInstance();
        problem.loadInputData("""
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
            
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#""");
        assertEquals(405L, problem.solvePartOne());
    }

    @Test
    public void testPart1() throws Exception {
        _testPart1(30487L);
    }

    @Test
    public void testPart2() throws Exception {
        _testPart2(31954L);
    }
}
