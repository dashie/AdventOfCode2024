package adventofcode.y2025;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem07Test extends AbstractProblemTest<Long> {

    public Problem07Test() {
        super(Problem07.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(1585L, 16716444407407L);
    }
}
