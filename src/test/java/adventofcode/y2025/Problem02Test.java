package adventofcode.y2025;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem02Test extends AbstractProblemTest<Long> {

    public Problem02Test() {
        super(Problem02.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(41294979841L, 66500947346L);
    }
}
