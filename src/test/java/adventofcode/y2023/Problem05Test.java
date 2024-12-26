package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem05Test extends AbstractProblemTest<Long> {

    public Problem05Test() {
        super(Problem05.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(35L, 46L);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(3374647L, 6082852L);
    }

}
