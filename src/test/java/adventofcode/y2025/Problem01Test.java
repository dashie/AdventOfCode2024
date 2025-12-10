package adventofcode.y2025;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem01Test extends AbstractProblemTest<Long> {

    public Problem01Test() {
        super(Problem01.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(1036L, 6228L);
    }
}
