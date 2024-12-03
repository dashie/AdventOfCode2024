package adventofcode.y2024;

import adventofcode.commons.AOCProblem;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Day 3: Mull It Over
 * https://adventofcode.com/2024/day/3
 */
public class Problem03 extends AOCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem03().solve(false);
    }

    private String[] input;

    @Override
    public void processInput(BufferedReader reader) throws Exception {
        input = reader.lines().toArray(String[]::new);
    }

    /**
     * ...Scan the corrupted memory for uncorrupted mul instructions.
     * What do you get if you add up all of the results of the multiplications?
     */
    @Override
    protected Long partOne() throws Exception {
        Pattern pattern = Pattern.compile("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)");
        long result = 0;
        for (String line : input) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                Long n1 = Long.parseLong(matcher.group(1));
                Long n2 = Long.parseLong(matcher.group(2));
                result += n1 * n2;
            }
        }
        return result;
    }

    /**
     * ...Handle the new instructions; what do you get if you add up
     * all of the results of just the enabled multiplications?
     */
    @Override
    protected Long partTwo() throws Exception {
        Pattern pattern = Pattern.compile("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)|do\\(\\)|don't\\(\\)");
        long result = 0;
        boolean enable = true;
        for (String line : input) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String instruction = matcher.group(0);
                if ("do()".equals(instruction)) {
                    enable = true;
                } else if ("don't()".equals(instruction)) {
                    enable = false;
                } else if (enable) {
                    Long n1 = Long.parseLong(matcher.group(1));
                    Long n2 = Long.parseLong(matcher.group(2));
                    result += n1 * n2;
                }
            }
        }
        return result;
    }
}
