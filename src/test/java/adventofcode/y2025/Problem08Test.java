package adventofcode.y2025;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem08Test extends AbstractProblemTest<Long> {

    public Problem08Test() {
        super(Problem08.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(131580L, 6844224L);
    }
}
