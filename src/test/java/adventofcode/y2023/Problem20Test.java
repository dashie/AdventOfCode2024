package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem20Test extends AbstractProblemTest<Long> {

    public Problem20Test() {
        super(Problem20.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(836127690L, 240914003753369L);
    }
}
