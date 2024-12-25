package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Day 25: Code Chronicle
 * https://adventofcode.com/2024/day/25
 */
public class Problem25 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        Problem25 problem = AoCProblem.buildWithInputResource(Problem25.class);
        // Problem25 problem = AoCProblem.buildWithSampleResource(Problem25.class);
        problem.solve();
    }

    List<String> locks = new ArrayList<>();
    List<String> keys = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        for (AoCInput si : input.splitInput("\\n\\n")) {
            Character[][] schema = si.toCharMatrix();
            if (schema[0][0] == '.') { // is a key
                keys.add(schemaToString(schema));
            } else { // is a lock
                locks.add(schemaToString(schema));
            }
        }

        // sort locks
        locks.sort(Comparator.reverseOrder());
    }

    public String schemaToString(Character[][] schema) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < schema[0].length; ++i) {
            int n = 0;
            if (schema[0][0] == '#') { // lock
                while (schema[n][i] == '#') n++;
                // store the complement, that is the size of free space
                // to compare easily, based on alphabetical criteria, with keys
                str.append(6 - n);
            } else {
                while (schema[n][i] == '.') n++;
                str.append(6 - n);
            }
        }
        return str.toString();
    }

    /**
     * ...Analyze your lock and key schematics.
     * How many unique lock/key pairs fit together without overlapping in any column?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = 0;
        for (String key : keys) {
            for (String lock : locks) {
                if (key.compareTo(lock) > 0) break; // locks collection is sorted
                if (keyMatch(key, lock))
                    result++;
            }
        }
        return result;
    }

    private boolean keyMatch(String key, String lock) {
        for (int i = 0; i < key.length(); ++i)
            if (key.charAt(i) > lock.charAt(i)) return false;
        return true;
    }
}
