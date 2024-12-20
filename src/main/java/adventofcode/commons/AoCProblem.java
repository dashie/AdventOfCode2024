package adventofcode.commons;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 */
public class AoCProblem<T> {

    private boolean isUsingSample = false;

    public AoCProblem() {
    }

    public boolean isUsingSample() {
        return isUsingSample;
    }

    /**
     *
     */
    public final void loadSampleData() throws Exception {
        isUsingSample = true;
        loadInputData("-sample");
    }

    /**
     *
     */
    public final void loadInputData() throws Exception {
        isUsingSample = false;
        loadInputData("");
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
    public final void solve() throws Exception {
        solve(false);
    }

    /**
     *
     */
    public void solve(boolean useSample) throws Exception {
        isUsingSample = useSample;
        String useSampleSuffix = useSample ? "-sample" : "";
        solve(useSampleSuffix);
    }

    /**
     *
     */
    private void solve(String fileSuffix) throws Exception {
        loadInputData(fileSuffix);

        System.out.println(getClass().getName());
        T result1 = partOne();
        System.out.printf("  Part One: %s%n", result1);
        T result2 = partTwo();
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
    public T partOne() throws Exception {
        return null;
    }

    /**
     *
     */
    public T partTwo() throws Exception {
        return null;
    }
}
