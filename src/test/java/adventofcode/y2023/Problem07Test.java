package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem07Test extends AbstractProblemTest<Long> {

    public Problem07Test() {
        super(Problem07.class);
    }

    @Test
    public void testPart1OnSample() throws Exception {
        _testPart1OnSample(6440L);
    }

    @Test
    public void testPart2OnSample() throws Exception {
        _testPart2OnSample(5905L);
    }

    @Test
    public void testPart1() throws Exception {
        _testPart1(248559379L);
    }

    @Test
    public void testPart2() throws Exception {
        _testPart2(249631254L);
    }
}
