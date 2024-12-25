package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.reverseOrder;

/**
 * Day 25: Code Chronicle
 * https://adventofcode.com/2024/day/25
 */
public class Problem25v2 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        Problem25v2 problem = AoCProblem.buildWithInputResource(Problem25v2.class);
        // Problem25 problem = AoCProblem.buildWithSampleResource(Problem25.class);
        problem.solve();
    }

    List<Integer> locks = new ArrayList<>();
    List<Integer> keys = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        for (AoCInput si : input.splitInput("\\n\\n")) {
            Character[][] schema = si.toCharMatrix();
            if (schema[0][0] == '.') { // is a key
                keys.add(schemaToCode(schema));
            } else { // is a lock
                locks.add(schemaToCode(schema));
            }
        }

        // sort locks
        locks.sort(reverseOrder());
    }

    public Integer schemaToCode(Character[][] schema) {
        int code = 0;
        for (int i = 0; i < schema[0].length; ++i) {
            int n = 0;
            // '#' lock, '.' keys
            char matcher = schema[0][0] == '#' ? '#' : '.';
            while (schema[n][i] == matcher) n++;
            n = 6 - n;
            code += n << (4 * i);
        }
        return code;
    }

    /**
     * ...Analyze your lock and key schematics.
     * How many unique lock/key pairs fit together without overlapping in any column?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = 0;
        for (int key : keys) {
            for (int lock : locks) {
                if (key > lock) break; // locks collection is sorted
                if (((lock - key) & 0x88888) == 0)
                    result++;
            }
        }
        return result;
    }
}
