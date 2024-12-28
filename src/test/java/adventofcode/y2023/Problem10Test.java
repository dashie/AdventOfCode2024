package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Problem10Test extends AbstractProblemTest<Long> {

    public Problem10Test() {
        super(Problem10.class);
    }

    @Test
    public void testSimpleLoop() throws Exception {
        Problem10 problem = _newProblemInstance();
        problem.loadInputData("""
            .....
            .S-7.
            .|.|.
            .L-J.
            .....""");
        assertEquals(4, problem.solvePartOne());
    }

    @Test
    public void testComplexLoop() throws Exception {
        Problem10 problem = _newProblemInstance();
        problem.loadInputData("""
            ..F7.
            .FJ|.
            SJ.L7
            |F--J
            LJ...""");
        assertEquals(8, problem.solvePartOne());
    }

    @Test
    public void testPart1() throws Exception {
        _testPart1(7012L);
    }

    @Test
    public void testPart2() throws Exception {
        _testPart2(395L);
    }
}
