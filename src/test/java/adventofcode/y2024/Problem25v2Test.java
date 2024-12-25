package adventofcode.y2024;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem25v2Test extends AbstractProblemTest {

    public Problem25v2Test() {
        super(Problem25v2.class);
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
