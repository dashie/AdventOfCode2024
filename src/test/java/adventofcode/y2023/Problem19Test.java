package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem19Test extends AbstractProblemTest<Long> {

    public Problem19Test() {
        super(Problem19.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(19114L, 167409079868000L);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(376008L, 124078207789312L);
    }

}
