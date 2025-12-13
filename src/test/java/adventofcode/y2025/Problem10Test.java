package adventofcode.y2025;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Problem10Test extends AbstractProblemTest<Long> {

    public Problem10Test() {
        super(Problem10.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(404L, 16474L);
    }

    @Test
    public void test_163_183_34_36() throws Exception {
        Problem10 problem = _newProblemInstance();
        problem.loadInputData("[#.##] (1,3) (1,2,3) (2,3) (0,1,3) (0,1) {163,183,34,36}");
        Assertions.assertEquals(197, problem.solvePartTwo());
    }

    @Test
    public void test_31_51_38_61_77_74_49_72v2() throws Exception {
        Problem10 problem = _newProblemInstance();
        problem.loadInputData("[######..] (0,1,2,3,4,5) (0,4,5,7) (1,5,7) (3,4,6) (2,6,7) (0,5,7) (2,3,4,7) (5,6,7) (1,3,4,5,6) (1,3,4) {31,51,38,61,77,74,49,72}");
        Assertions.assertEquals(114, problem.solvePartTwo());
    }
}
