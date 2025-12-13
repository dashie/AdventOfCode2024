package adventofcode.y2025;

import adventofcode.commons.AbstractProblemTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Problem11Test extends AbstractProblemTest<Long> {

    public Problem11Test() {
        super(Problem11.class);
    }


    @Test
    public void testPartTwo() throws Exception {
        Problem11 problem = _newProblemInstance();
        problem.loadInputData("""
            svr: aaa bbb
            aaa: fft
            fft: ccc
            bbb: tty
            tty: ccc
            ccc: ddd eee
            ddd: hub
            hub: fff
            eee: dac
            dac: fff
            fff: ggg hhh
            ggg: out
            hhh: out""");
        Assertions.assertEquals(2, problem.solvePartTwo());
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(699L, 388893655378800L);
    }

}
