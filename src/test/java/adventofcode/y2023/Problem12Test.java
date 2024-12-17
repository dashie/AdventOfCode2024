package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem12Test extends AbstractProblemTest<Long> {

    public Problem12Test() {
        super(Problem12.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(21L, 525152L);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(8075L, 4232520187524L);
    }

}
