package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem09Test extends AbstractProblemTest<Long> {

    public Problem09Test() {
        super(Problem09.class);
    }

    @Test
    public void testPart1OnSample() throws Exception {
        _testPart1OnSample(114L);
    }

    @Test
    public void testPart1() throws Exception {
        _testPart1(1782868781L);
    }

    @Test
    public void testPart2() throws Exception {
        _testPart2(1057L);
    }
}
