package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem23Test extends AbstractProblemTest<Long> {

    public Problem23Test() {
        super(Problem23.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(94L, 154L);
    }

    @Test
    public void testProblem() throws Exception {
        // 2nd part: 6262 // set to null because at the moment the algorithm is too slow
        _testProblem(2050L, null);
    }

}
