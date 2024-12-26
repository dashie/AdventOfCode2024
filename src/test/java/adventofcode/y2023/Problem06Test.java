package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem06Test extends AbstractProblemTest<Long> {

    public Problem06Test() {
        super(Problem06.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(288L, 71503L);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(3317888L, 24655068L);
    }

}
