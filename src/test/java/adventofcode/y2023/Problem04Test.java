package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem04Test extends AbstractProblemTest<Long> {

    public Problem04Test() {
        super(Problem04.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(13L, 30L);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(28538L, 9425061L);
    }

}
