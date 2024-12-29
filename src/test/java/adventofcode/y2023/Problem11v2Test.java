package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import adventofcode.commons.Point;
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

        Map<Point, Integer> distances_1_5 = problem.buildDistanceMap(Point.of(1, 5), 2);
        assertEquals(9, distances_1_5.get(Point.of(4, 9)));
        Map<Point, Integer> distances_4_9 = problem.buildDistanceMap(Point.of(4, 9), 2);
        assertEquals(9, distances_4_9.get(Point.of(1, 5)));

        Map<Point, Integer> distances_3_0 = problem.buildDistanceMap(Point.of(3, 0), 2);
        assertEquals(15, distances_3_0.get(Point.of(7, 8)));
        Map<Point, Integer> distances_7_8 = problem.buildDistanceMap(Point.of(7, 8), 2);
        assertEquals(15, distances_7_8.get(Point.of(3, 0)));

        Map<Point, Integer> distances_0_9 = problem.buildDistanceMap(Point.of(0, 9), 2);
        assertEquals(5, distances_0_9.get(Point.of(4, 9)));
        assertEquals(5, distances_4_9.get(Point.of(0, 9)));
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

        Map<Point, Integer> distances_1_5 = problem.buildDistanceMap(Point.of(1, 5), 2);
        assertEquals(11, distances_1_5.get(Point.of(4, 9)));
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
