package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem16Test extends AbstractProblemTest<Long> {

    public Problem16Test() {
        super(Problem16.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(46L, 51L);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(6605L, 6766L);
    }

}
