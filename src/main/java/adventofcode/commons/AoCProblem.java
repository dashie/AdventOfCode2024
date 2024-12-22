package adventofcode.commons;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Objects;

/**
 *
 */
public class AoCProblem<T> {

    private boolean isUsingSampleResource = false;

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
    public final void loadInputResource() throws Exception {
        isUsingSampleResource = false;
        loadInputResource("");
    }

    /**
     *
     */
    public final void loadSampleResource() throws Exception {
        isUsingSampleResource = true;
        loadInputResource("-sample");
    }

    /**
     *
     */
    public final void loadInputResource(String fileSuffix) throws Exception {
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
    }

    /**
     *
     */
    public final AoCProblem<T> loadInputData(String data) throws Exception {
        Objects.requireNonNull(data, "data");
        AoCInput input;
        try (BufferedReader reader = new BufferedReader(new StringReader(data))) {
            input = AoCInput.fromReader(reader);
        }
        processInput(input);
        return this;
    }

    /**
     *
     */
    public final void loadResourceAndSolve() throws Exception {
        loadResourceAndSolve(false);
    }

    /**
     *
     */
    public void loadResourceAndSolve(boolean useSampleData) throws Exception {
        isUsingSampleResource = useSampleData;
        String useSampleSuffix = useSampleData ? "-sample" : "";
        loadResourceAndSolve(useSampleSuffix);
    }

    /**
     *
     */
    private void loadResourceAndSolve(String inputDataSuffix) throws Exception {
        loadInputResource(inputDataSuffix);

        System.out.println(getClass().getName());
        T result1 = solvePartOne();
        System.out.printf("  Part One: %s%n", result1);
        T result2 = solvePartTwo();
        if (result2 != null) {
            System.out.printf("  Part Two: %s%n", result2);
        }
        System.out.println();
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
}
