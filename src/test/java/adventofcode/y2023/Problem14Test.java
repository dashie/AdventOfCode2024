package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem14Test extends AbstractProblemTest<Long> {

    public Problem14Test() {
        super(Problem14.class);
    }

    @Test
    public void testPart1() throws Exception {
        _testPart1(112046L);
    }

    @Test
    public void testPart2() throws Exception {
        _testPart2(104619L);
    }
}
