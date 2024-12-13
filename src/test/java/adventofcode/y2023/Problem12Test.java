package adventofcode.y2023;

import adventofcode.y2024.AbstractProblemTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Problem12Test extends AbstractProblemTest<Problem12> {

    public Problem12Test() {
        super(Problem12.class);
    }

    @Test
    public void testSample() throws Exception {
        _testSample(21, 525152);
    }

    @Test
    public void testProblem() throws Exception {
        _testProblem(8075, 4232520187524L);
    }

}
