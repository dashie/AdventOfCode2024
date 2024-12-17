package adventofcode.y2024;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem14Test extends AbstractProblemTest<Long> {

    public Problem14Test() {
        super(Problem14.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(223020000L, 7338L);
    }
}
