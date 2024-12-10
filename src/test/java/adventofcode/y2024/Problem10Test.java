package adventofcode.y2024;

import org.junit.jupiter.api.Test;

public class Problem10Test extends AbstractProblemTest {

    public Problem10Test() {
        super(Problem10.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(1, 5);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(624, 1483);
    }
}
