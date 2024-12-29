package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Problem17Test extends AbstractProblemTest<Long> {

    public Problem17Test() {
        super(Problem17.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(102L, 94L);
    }

    @Test
    public void testUltraCrucible() throws Exception {
        Problem17 problem = AoCProblem.build(Problem17.class);
        problem.loadInputData("""
            111111111111
            999999999991
            999999999991
            999999999991
            999999999991""");
        long v = problem.findMinHeatLost(Point.of(0, 0), true);
        Assertions.assertEquals(71, v);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(1001L, 1197L);
    }

}
