package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem22Test extends AbstractProblemTest<Long> {

    public Problem22Test() {
        super(Problem22.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(5L, 7L);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(418L, 70702L);
    }
}
