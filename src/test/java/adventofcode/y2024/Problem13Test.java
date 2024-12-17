package adventofcode.y2024;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem13Test extends AbstractProblemTest<Long> {

    public Problem13Test() {
        super(Problem13.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(27157L, 104015411578548L);
    }
}
