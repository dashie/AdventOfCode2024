package adventofcode.y2025;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem03Test extends AbstractProblemTest<Long> {

    public Problem03Test() {
        super(Problem03.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(17144L, 170371185255900L);
    }
}
