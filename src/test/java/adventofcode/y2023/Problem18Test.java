package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem18Test extends AbstractProblemTest<Long> {

    public Problem18Test() {
        super(Problem18.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(34329L, 42617947302920L);
    }

}
