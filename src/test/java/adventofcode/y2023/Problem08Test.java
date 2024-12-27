package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem08Test extends AbstractProblemTest<Long> {

    public Problem08Test() {
        super(Problem08.class);
    }

    @Test
    public void testPart1OnSample() throws Exception {
        _testPart1OnSample(2L);
    }

    @Test
    public void testPart1() throws Exception {
        _testPart1(16271L);
    }

    @Test
    public void testPart2() throws Exception {
        _testPart2(14265111103729L);
    }
}
