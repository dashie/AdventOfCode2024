package adventofcode.y2023;

import adventofcode.commons.AoCProblem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Problem21Test {

    private static String SAMPLE_INPUT = """
        ...........
        .....###.#.
        .###.##..#.
        ..#.#...#..
        ....#.#....
        .##..S####.
        .##..#...#.
        .......##..
        .##.#.####.
        .##..##.##.
        ...........""";

    @Test
    public void test1StepOnSample() throws Exception {
        Problem21 p = AoCProblem.build(Problem21.class);
        p.loadInputData(SAMPLE_INPUT);
        long plots = p.countPlots(1);
        assertEquals(2, plots);
    }

    @Test
    public void test2StepsOnSample() throws Exception {
        Problem21 p = AoCProblem.build(Problem21.class);
        p.loadInputData(SAMPLE_INPUT);
        long plots = p.countPlots(2);
        assertEquals(4, plots);
    }

    @Test
    public void test3StepsOnSample() throws Exception {
        Problem21 p = AoCProblem.build(Problem21.class);
        p.loadInputData(SAMPLE_INPUT);
        long plots = p.countPlots(3);
        assertEquals(6, plots);
    }

    @Test
    public void test6StepsOnSample() throws Exception {
        Problem21 p = AoCProblem.build(Problem21.class);
        p.loadInputData(SAMPLE_INPUT);
        long plots = p.countPlots(6);
        assertEquals(16, plots);
    }

    @Test
    public void testPart1() throws Exception {
        Problem21 p = AoCProblem.build(Problem21.class);
        p.loadInputResource();
        long plots = p.solvePartOne();
        assertEquals(3770, plots);
    }

    @Test
    public void testCountWithVirtualBoardOnSample() throws Exception {
        Problem21 p = AoCProblem.build(Problem21.class);
        p.loadInputData(SAMPLE_INPUT);
        assertEquals(6, p.countPlotsWithVirtualBoard(3));
        assertEquals(16, p.countPlotsWithVirtualBoard(6));
        assertEquals(1594, p.countPlotsWithVirtualBoard(50));
        assertEquals(6536, p.countPlotsWithVirtualBoard(100));
        assertEquals(167004, p.countPlotsWithVirtualBoard(500));
    }

    @Test
    public void testCountProjectionRandomStepsOnSample() throws Exception {
        // TODO still need to be fixed
        Problem21 p = AoCProblem.build(Problem21.class);
        p.loadInputData(SAMPLE_INPUT);
        assertEquals(6, p.countPlotsWithProjection(3));
        assertEquals(16, p.countPlotsWithProjection(6));
        assertEquals(1594, p.countPlotsWithProjection(50));
        assertEquals(6536, p.countPlotsWithProjection(100));
        assertEquals(167004, p.countPlotsWithProjection(500));
        assertEquals(668697, p.countPlotsWithProjection(1000));
        assertEquals(16733044, p.countPlotsWithProjection(5000));
    }

    @Test
    public void testCountProjectionRandomSteps() throws Exception {
        // TODO still need to be fixed
        Problem21 p = AoCProblem.build(Problem21.class);
        p.loadInputResource();
        assertEquals(p.countPlotsWithVirtualBoard(1000), p.countPlotsWithProjection(1000));
        // assertEquals(p.countPlotsWithVirtualBoard(5000), p.countPlotsWithProjection(5000));
    }

    @Test
    public void testCountProjectionBoardSizeNumbers() throws Exception {
        Problem21 p = AoCProblem.build(Problem21.class);
        p.loadInputResource();
        assertEquals(p.countPlotsWithVirtualBoard(65 + 131 * 9), p.countPlotsWithProjection(65 + 131 * 9));
        // assertEquals(p.countPlotsWithVirtualBoard(65 + 131 * 20), p.countPlotsWithProjection(65 + 131 * 20));
    }

    @Test
    public void test26501365StepsWithProjection() throws Exception {
        Problem21 p = AoCProblem.build(Problem21.class);
        p.loadInputResource();
        assertEquals(628206330073385L, p.countPlotsWithProjection(26501365));
    }

    @Test
    public void test26501365StepsWithPolynomial() throws Exception {
        Problem21 p = AoCProblem.build(Problem21.class);
        p.loadInputResource();
        assertEquals(628206330073385L, p.countPlotsWithPolynomial(26501365));
    }

    @Test
    public void testPart2() throws Exception {
        Problem21 p = AoCProblem.build(Problem21.class);
        p.loadInputResource();
        assertEquals(628206330073385L, p.solvePartTwo());
    }

}
