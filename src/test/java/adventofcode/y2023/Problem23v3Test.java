package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem23v3Test extends AbstractProblemTest<Long> {

    public Problem23v3Test() {
        super(Problem23v3.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(94L, 154L);
    }

    @Test
    public void testPart1() throws Exception {
        _testPart1(2050L);
    }

    @Test
    public void testPart2() throws Exception {
        _testPart2(6262L);
    }

}
