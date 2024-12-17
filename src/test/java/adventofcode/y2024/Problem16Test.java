package adventofcode.y2024;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem16Test extends AbstractProblemTest<Long> {

    public Problem16Test() {
        super(Problem16.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(134588L, 631L);
    }
}
