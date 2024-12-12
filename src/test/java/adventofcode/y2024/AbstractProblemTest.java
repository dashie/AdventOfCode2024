package adventofcode.y2024;

import adventofcode.commons.AoCProblem;
import org.junit.jupiter.api.Assertions;

public abstract class AbstractProblemTest<T extends AoCProblem<Long>> {

    private final Class<T> problemClass;

    public AbstractProblemTest(Class<T> problemClass) {
        this.problemClass = problemClass;
    }

    protected T _newProblemInstance() throws Exception {
        return (T) problemClass.getConstructor().newInstance();
    }

    protected void _testSample(long expectedResult1, long expectedResult2) throws Exception {
        AoCProblem<Long> problem = _newProblemInstance();
        problem.loadInputData("-sample");
        Long result1 = problem.partOne();
        Assertions.assertEquals(expectedResult1, result1, "Part 1");
        Long result2 = problem.partTwo();
        Assertions.assertEquals(expectedResult2, result2, "Part 2");
    }

    protected void _testProblem(long expectedResult1, long expectedResult2) throws Exception {
        AoCProblem<Long> problem = _newProblemInstance();
        problem.loadInputData(null);
        Long result1 = problem.partOne();
        Assertions.assertEquals(expectedResult1, result1, "Part 1");
        Long result2 = problem.partTwo();
        Assertions.assertEquals(expectedResult2, result2, "Part 2");
    }
}
