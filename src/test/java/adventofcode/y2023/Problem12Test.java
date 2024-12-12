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
    public void testGenerateOption1() throws Exception {
        Set<String> expecteds = new HashSet<>();
        expecteds.add(".##");
        expecteds.add("##.");
        List<String> options = _newProblemInstance().generateOptions("?#?", new Integer[]{2});
        expecteds.removeAll(options);
        Assertions.assertEquals(0, expecteds.size());
    }

    @Test
    public void testGenerateOption2() throws Exception {
        Set<String> expecteds = new HashSet<>();
        expecteds.add(".##.###");
        expecteds.add("##..###");
        List<String> options = _newProblemInstance().generateOptions("?#?.##?", new Integer[]{2, 3});
        expecteds.removeAll(options);
        Assertions.assertEquals(0, expecteds.size());
    }
}
