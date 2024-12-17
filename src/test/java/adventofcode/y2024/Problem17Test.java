package adventofcode.y2024;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem17Test extends AbstractProblemTest {

    public Problem17Test() {
        super(Problem17.class);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem("1,5,7,4,1,6,0,3,0", "108107574778365");
    }
}
