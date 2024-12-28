package adventofcode.commons;

import org.junit.jupiter.api.Assertions;

public abstract class AbstractProblemTest<T> {

    private final Class<AoCProblem<T, ? extends AoCProblem<T, ?>>> problemClass;

    public AbstractProblemTest(Class problemClass) {
        this.problemClass = (Class<AoCProblem<T, ?>>) problemClass;
    }

    protected <P extends AoCProblem<T, P>> P _newProblemInstance() throws Exception {
        return (P) problemClass.getConstructor().newInstance();
    }

    protected void _testSample(T expectedResult1, T expectedResult2) throws Exception {
        AoCProblem<T, ?> problem = _newProblemInstance();
        problem.loadSampleResource();
        Object result1 = problem.solvePartOne();
        Assertions.assertEquals(expectedResult1, result1, "Part 1");
        if (expectedResult2 != null) {
            Object result2 = problem.solvePartTwo();
            Assertions.assertEquals(expectedResult2, result2, "Part 2");
        }
    }

    protected void _testPart1OnSample(T expectedResult) throws Exception {
        AoCProblem<T, ?> problem = _newProblemInstance();
        problem.loadSampleResource();
        Object result = problem.solvePartOne();
        Assertions.assertEquals(expectedResult, result, "Part 1");
    }

    protected void _testPart2OnSample(T expectedResult) throws Exception {
        AoCProblem<T, ?> problem = _newProblemInstance();
        problem.loadSampleResource();
        Object result = problem.solvePartTwo();
        Assertions.assertEquals(expectedResult, result, "Part 2");
    }

    protected void _testProblem(T expectedResult1, T expectedResult2) throws Exception {
        AoCProblem<T, ?> problem = _newProblemInstance();
        problem.loadInputResource();
        Object result1 = problem.solvePartOne();
        Assertions.assertEquals(expectedResult1, result1, "Part 1");
        Object result2 = problem.solvePartTwo();
        Assertions.assertEquals(expectedResult2, result2, "Part 2");
    }

    protected void _testPart1(T expectedResult) throws Exception {
        AoCProblem<T, ?> problem = _newProblemInstance();
        problem.loadInputResource();
        Object result = problem.solvePartOne();
        Assertions.assertEquals(expectedResult, result, "Part 1");
    }

    protected void _testPart2(T expectedResult) throws Exception {
        AoCProblem<T, ?> problem = _newProblemInstance();
        problem.loadInputResource();
        Object result = problem.solvePartTwo();
        Assertions.assertEquals(expectedResult, result, "Part 2");
    }
}
