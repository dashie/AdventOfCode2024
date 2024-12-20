package adventofcode.y2024;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem20Test extends AbstractProblemTest {

    public Problem20Test() {
        super(Problem20.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(8L, 41L);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(1358L, 1005856L);
    }
}
