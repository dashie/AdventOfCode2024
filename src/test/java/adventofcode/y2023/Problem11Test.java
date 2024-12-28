package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import adventofcode.commons.AoCPoint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Problem11Test extends AbstractProblemTest<Long> {

    public Problem11Test() {
        super(Problem11.class);
    }

    @Test
    public void testDistance() throws Exception {
        Problem11 problem = _newProblemInstance();
        problem.loadInputData("""
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....""");

        assertEquals(9, problem.evalDistance(AoCPoint.of(1, 5), AoCPoint.of(4, 9), 2));
        assertEquals(9, problem.evalDistance(AoCPoint.of(4, 9), AoCPoint.of(1, 5), 2));
        assertEquals(15, problem.evalDistance(AoCPoint.of(3, 0), AoCPoint.of(7, 8), 2));
        assertEquals(15, problem.evalDistance(AoCPoint.of(7, 8), AoCPoint.of(3, 0), 2));
        assertEquals(5, problem.evalDistance(AoCPoint.of(0, 9), AoCPoint.of(4, 9), 2));
        assertEquals(5, problem.evalDistance(AoCPoint.of(4, 9), AoCPoint.of(0, 9), 2));
    }

    @Test
    public void testDistanceWithMultiFreeLines() throws Exception {
        Problem11 problem = _newProblemInstance();
        problem.loadInputData("""
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            ..........
            ..........
            ..........
            #...#.....""");

        assertEquals(11, problem.evalDistance(AoCPoint.of(1, 5), AoCPoint.of(4, 9), 2));
    }

    @Test
    public void testPart1WithSample() throws Exception {
        Problem11 problem = _newProblemInstance();
        problem.loadInputData("""
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....""");
        assertEquals(374L, problem.solvePartOne());
    }

    @Test
    public void evalExpansionFactorWithSample() throws Exception {
        Problem11 problem = _newProblemInstance();
        problem.loadInputData("""
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....""");
        assertEquals(1030L, problem.evalDistancesSum(10));
        assertEquals(8410L, problem.evalDistancesSum(100));
    }

    @Test
    public void testPart1() throws Exception {
        _testPart1(9565386L);
    }

    @Test
    public void testPart2() throws Exception {
        _testPart2(857986849428L);
    }
}