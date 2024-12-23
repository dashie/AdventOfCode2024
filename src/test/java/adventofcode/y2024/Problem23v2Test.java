package adventofcode.y2024;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Test;

public class Problem23v2Test extends AbstractProblemTest {

    public Problem23v2Test() {
        super(Problem23v2.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample("7", "co,de,ka,ta");
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem("1599", "av,ax,dg,di,dw,fa,ge,kh,ki,ot,qw,vz,yw");
    }
}
