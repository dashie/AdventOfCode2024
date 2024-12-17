package adventofcode.y2024;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem09Test extends AbstractProblemTest<Long> {

    public Problem09Test() {
        super(Problem09.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(1928L, 2858L);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(6398608069280L, 6427437134372L);
    }
}
