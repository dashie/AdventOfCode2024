package adventofcode.y2025;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem04Test extends AbstractProblemTest<Long> {

    public Problem04Test() {
        super(Problem04.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(1435L, 8623L);
    }
}
