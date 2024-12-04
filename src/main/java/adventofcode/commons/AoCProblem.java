package adventofcode.commons;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 */
public class AoCProblem<T> {

    public AoCProblem() {
    }

    public final void solve() throws Exception {
        solve(false);
    }

    public void solve(boolean useSample) throws Exception {
        String useSampleSuffix = useSample ? "-sample" : "";
        this.solve(useSampleSuffix);
    }

    /**
     *
     */
    private void solve(String fileSuffix) throws Exception {
        if (fileSuffix == null) {
            fileSuffix = "";
        } else if (!fileSuffix.isEmpty() && !fileSuffix.startsWith("-")) {
            fileSuffix = '-' + fileSuffix;
        }
        Class thisClass = this.getClass();
        String clasName = thisClass.getSimpleName();
        String dataURL = String.format("%s%s.txt", clasName, fileSuffix);
        dataURL = dataURL.replaceAll("v\\d+([.-])", "$1");

        try (InputStream is = thisClass.getResourceAsStream(dataURL)) {
            if (is == null) {
                throw new IllegalStateException(String.format("Missing input data: %s", dataURL));
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            processInput(new AoCInput(reader));
        }

        System.out.println(getClass().getName());
        T result1 = partOne();
        System.out.printf("  Part One: %s%n", result1);
        T result2 = partTwo();
        if (result2 != null) {
            System.out.printf("  Part Two: %s%n", result2);
        }
        System.out.println();
    }

    public void processInput(AoCInput input) throws Exception {

    }

    protected T partOne() throws Exception {
        return null;
    }

    protected T partTwo() throws Exception {
        return null;
    }
}