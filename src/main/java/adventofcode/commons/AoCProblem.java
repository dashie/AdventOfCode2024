package adventofcode.commons;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Objects;

/**
 *
 */
public class AoCProblem<T, P extends AoCProblem<T, P>> {

    private boolean isUsingSampleResource = false;

    public static final <T, P extends AoCProblem<T, P>> P build(Class<P> problemClass) throws Exception {
        P problem = problemClass.getConstructor().newInstance();
        return (P) problem;
    }

    public static final <T, P extends AoCProblem<T, P>> P buildWithInputResource(Class<P> problemClass) throws Exception {
        P problem = problemClass.getConstructor().newInstance();
        return (P) problem.loadInputResource();
    }

    public static final <T, P extends AoCProblem<T, P>> P buildWithSampleResource(Class<P> problemClass) throws Exception {
        P problem = problemClass.getConstructor().newInstance();
        return (P) problem.loadSampleResource();
    }

    public static final <T, P extends AoCProblem<T, P>> P buildWithSampleResource(Class<P> problemClass, String fileSuffix) throws Exception {
        P problem = problemClass.getConstructor().newInstance();
        return (P) problem.loadSampleResource(fileSuffix);
    }

    /**
     *
     */
    public AoCProblem() {
    }

    /**
     *
     */
    public boolean isUsingSampleResource() {
        return isUsingSampleResource;
    }

    /**
     *
     */
    public final P loadInputResource() throws Exception {
        isUsingSampleResource = false;
        loadInputResource("");
        return (P) this;
    }

    /**
     *
     */
    public final P loadSampleResource() throws Exception {
        isUsingSampleResource = true;
        loadInputResource("-sample");
        return (P) this;
    }

    /**
     *
     */
    public final P loadSampleResource(String fileSuffix) throws Exception {
        isUsingSampleResource = true;
        loadInputResource(fileSuffix);
        return (P) this;
    }

    /**
     *
     */
    public final P loadInputResource(String fileSuffix) throws Exception {
        if (fileSuffix == null) {
            fileSuffix = "";
        } else if (!fileSuffix.isEmpty() && !fileSuffix.startsWith("-")) {
            fileSuffix = '-' + fileSuffix;
        }
        Class thisClass = this.getClass();
        String clasName = thisClass.getSimpleName();
        String dataURL = String.format("%s%s.txt", clasName, fileSuffix);
        dataURL = dataURL.replaceAll("v\\d+([.-])", "$1");

        AoCInput input;
        try (InputStream is = thisClass.getResourceAsStream(dataURL)) {
            if (is == null) {
                throw new IllegalStateException(String.format("Missing input data: %s", dataURL));
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            input = AoCInput.fromReader(reader);
        }
        processInput(input);
        return (P) this;
    }

    /**
     *
     */
    public final P loadInputData(String data) throws Exception {
        Objects.requireNonNull(data, "data");
        AoCInput input;
        try (BufferedReader reader = new BufferedReader(new StringReader(data))) {
            input = AoCInput.fromReader(reader);
        }
        processInput(input);
        return (P) this;
    }

    /**
     *
     */
    public final Solution loadResourceAndSolve() throws Exception {
        return loadResourceAndSolve(false);
    }

    /**
     *
     */
    public Solution loadResourceAndSolve(boolean useSampleData) throws Exception {
        isUsingSampleResource = useSampleData;
        String useSampleSuffix = useSampleData ? "-sample" : "";
        return loadResourceAndSolve(useSampleSuffix);
    }

    /**
     *
     */
    private Solution loadResourceAndSolve(String inputDataSuffix) throws Exception {
        loadInputResource(inputDataSuffix);
        return solve();
    }

    /**
     *
     */
    public Solution solve() throws Exception {
        System.out.println(getClass().getName());
        T result1 = solvePartOne();
        System.out.printf("  Part One: %s%n", result1);
        T result2 = solvePartTwo();
        if (result2 != null) {
            System.out.printf("  Part Two: %s%n", result2);
        }
        System.out.println();
        return new Solution(result1, result2);
    }

    /**
     *
     */
    public void processInput(AoCInput input) throws Exception {

    }

    /**
     *
     */
    public T solvePartOne() throws Exception {
        return null;
    }

    /**
     *
     */
    public T solvePartTwo() throws Exception {
        return null;
    }

    /**
     *
     */
    public P log(String format, Object... args) {
        System.out.printf(format, args);
        return (P) this;
    }

    /**
     *
     */
    public class Solution {

        public final T r1;
        public final T r2;

        private Solution(T r1, T r2) {
            this.r1 = r1;
            this.r2 = r2;
        }
    }
}
