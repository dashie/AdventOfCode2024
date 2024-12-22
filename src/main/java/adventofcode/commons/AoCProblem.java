package adventofcode.commons;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 */
public class AoCProblem<T> {

    private boolean isUsingSampleData = false;

    /**
     *
     */
    public AoCProblem() {
    }

    /**
     *
     */
    public boolean isUsingSampleData() {
        return isUsingSampleData;
    }

    /**
     *
     */
    public final void loadInputData() throws Exception {
        isUsingSampleData = false;
        loadInputData("");
    }

    /**
     *
     */
    public final void loadSampleData() throws Exception {
        isUsingSampleData = true;
        loadInputData("-sample");
    }

    /**
     *
     */
    public final void loadInputData(String fileSuffix) throws Exception {
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
    public final void loadAndSolve() throws Exception {
        loadAndSolve(false);
    }

    /**
     *
     */
    public void loadAndSolve(boolean useSampleData) throws Exception {
        isUsingSampleData = useSampleData;
        String useSampleSuffix = useSampleData ? "-sample" : "";
        loadAndSolve(useSampleSuffix);
    }

    /**
     *
     */
    private void loadAndSolve(String inputDataSuffix) throws Exception {
        loadInputData(inputDataSuffix);

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
