package adventofcode.y2024;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem11Test extends AbstractProblemTest<Long> {

    public Problem11Test() {
        super(Problem11.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(220722L, 261952051690787L);
    }
}
