package adventofcode.y2025;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem05Test extends AbstractProblemTest<Long> {

    public Problem05Test() {
        super(Problem05.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(865L, 352556672963116L);
    }
}
