package adventofcode.commons;

import org.junit.jupiter.api.Assertions;

public abstract class AbstractProblemTest<T> {

    private final Class<AoCProblem<T>> problemClass;

    public AbstractProblemTest(Class problemClass) {
        this.problemClass = (Class<AoCProblem<T>>) problemClass;
    }

    protected AoCProblem<T> _newProblemInstance() throws Exception {
        return problemClass.getConstructor().newInstance();
    }

    protected void _testSample(T expectedResult1, T expectedResult2) throws Exception {
        AoCProblem<T> problem = _newProblemInstance();
        problem.loadInputData("-sample");
        Object result1 = problem.partOne();
        Assertions.assertEquals(expectedResult1, result1, "Part 1");
        Object result2 = problem.partTwo();
        Assertions.assertEquals(expectedResult2, result2, "Part 2");
    }

    protected void _testProblem(T expectedResult1, T expectedResult2) throws Exception {
        AoCProblem<T> problem = _newProblemInstance();
        problem.loadInputData(null);
        Object result1 = problem.partOne();
        Assertions.assertEquals(expectedResult1, result1, "Part 1");
        Object result2 = problem.partTwo();
        Assertions.assertEquals(expectedResult2, result2, "Part 2");
    }
}
