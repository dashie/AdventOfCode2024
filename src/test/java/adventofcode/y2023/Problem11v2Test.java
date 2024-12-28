package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import adventofcode.commons.AoCPoint;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Problem11v2Test extends AbstractProblemTest<Long> {

    public Problem11v2Test() {
        super(Problem11v2.class);
    }

    @Test
    public void testDistance() throws Exception {
        Problem11v2 problem = _newProblemInstance();
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

        Map<AoCPoint, Integer> distances_1_5 = problem.buildDistanceMap(AoCPoint.of(1, 5), 2);
        assertEquals(9, distances_1_5.get(AoCPoint.of(4, 9)));
        Map<AoCPoint, Integer> distances_4_9 = problem.buildDistanceMap(AoCPoint.of(4, 9), 2);
        assertEquals(9, distances_4_9.get(AoCPoint.of(1, 5)));

        Map<AoCPoint, Integer> distances_3_0 = problem.buildDistanceMap(AoCPoint.of(3, 0), 2);
        assertEquals(15, distances_3_0.get(AoCPoint.of(7, 8)));
        Map<AoCPoint, Integer> distances_7_8 = problem.buildDistanceMap(AoCPoint.of(7, 8), 2);
        assertEquals(15, distances_7_8.get(AoCPoint.of(3, 0)));

        Map<AoCPoint, Integer> distances_0_9 = problem.buildDistanceMap(AoCPoint.of(0, 9), 2);
        assertEquals(5, distances_0_9.get(AoCPoint.of(4, 9)));
        assertEquals(5, distances_4_9.get(AoCPoint.of(0, 9)));
    }

    @Test
    public void testDistanceWithMultiFreeLines() throws Exception {
        Problem11v2 problem = _newProblemInstance();
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

        Map<AoCPoint, Integer> distances_1_5 = problem.buildDistanceMap(AoCPoint.of(1, 5), 2);
        assertEquals(11, distances_1_5.get(AoCPoint.of(4, 9)));
    }

    @Test
    public void testPart1WithSample() throws Exception {
        Problem11v2 problem = _newProblemInstance();
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
        Problem11v2 problem = _newProblemInstance();
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
