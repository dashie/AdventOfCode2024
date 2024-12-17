package adventofcode.y2024;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem10Test extends AbstractProblemTest<Long> {

    public Problem10Test() {
        super(Problem10.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(1L, 5L);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(624L, 1483L);
    }
}
