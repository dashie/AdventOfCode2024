package adventofcode.y2024;

import org.junit.jupiter.api.Test;

public class Problem09Test extends AbstractProblemTest {

    public Problem09Test() {
        super(Problem09.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(1928, 2858);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(6398608069280L, 6427437134372L);
    }
}
