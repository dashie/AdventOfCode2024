package adventofcode.y2024;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem15Test extends AbstractProblemTest<Long> {

    public Problem15Test() {
        super(Problem15.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(1505963L, 1543141L);
    }
}
