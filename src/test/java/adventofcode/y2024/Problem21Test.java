package adventofcode.y2024;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem21Test extends AbstractProblemTest {

    public Problem21Test() {
        super(Problem21.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(126384L, null);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(224326L, 279638326609472L);
    }
}
