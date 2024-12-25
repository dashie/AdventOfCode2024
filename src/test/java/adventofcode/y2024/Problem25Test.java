package adventofcode.y2024;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem25Test extends AbstractProblemTest {

    public Problem25Test() {
        super(Problem25.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(3L, null);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(3301L, null);
    }
}
