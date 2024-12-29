package adventofcode.y2023;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem15Test extends AbstractProblemTest<Long> {

    public Problem15Test() {
        super(Problem15.class);
    }

    @Test
    public void testPart1() throws Exception {
        _testPart1(512797L);
    }

    @Test
    public void testPart2() throws Exception {
        _testPart2(262454L);
    }
}
