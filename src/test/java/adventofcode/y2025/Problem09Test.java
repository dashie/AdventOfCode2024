package adventofcode.y2025;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem09Test extends AbstractProblemTest<Long> {

    public Problem09Test() {
        super(Problem09.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(4777967538L, 1439894345L);
    }
}
