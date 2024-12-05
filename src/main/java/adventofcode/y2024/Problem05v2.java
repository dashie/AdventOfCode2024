package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.*;

import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.toUnmodifiableList;

/**
 * Day 5: Print Queue
 * https://adventofcode.com/2024/day/4
 */
public class Problem05v2 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem05v2().solve(false);
    }

    private Set<String> orderSet = new HashSet<>();
    private List<List<String>> updates = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        for (String line : input) {
            if (line.isEmpty()) break;
            orderSet.add(line);
        }
        for (String line : input) {
            updates.add(Arrays
                .stream(line.split(","))
                .collect(toUnmodifiableList()));
        }
    }

    /**
     * ...Determine which updates are already in the correct order.
     * What do you get if you add up the middle page number from those
     * correctly-ordered updates?
     */
    @Override
    protected Long partOne() throws Exception {
        long result = 0;
        for (List<String> update : updates) {
            if (isCorrect(update)) {
                result += parseLong(update.get(update.size() / 2));
            }
        }
        return result;
    }

    private boolean isCorrect(List<String> update) {
        for (int i = update.size() - 1; i > 0; i--) {
            String n = update.get(i);
            for (int j = 0; j < i; j++) {
                String prev = update.get(j);
                if (orderSet.contains(n + "|" + prev)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * ...Find the updates which are not in the correct order.
     * What do you get if you add up the middle page numbers after
     * correctly ordering just those updates?
     */
    @Override
    protected Long partTwo() throws Exception {
        long result = 0;
        for (List<String> update : updates) {
            if (!isCorrect(update)) {
                List<String> fixed = fixOrder(update);
                result += parseLong(fixed.get(fixed.size() / 2));
            }
        }
        return result;
    }

    private List<String> fixOrder(List<String> update) {
        List<String> fixed = new ArrayList<>();
        fixed.add(update.get(0));
        next:
        for (int i = 1; i < update.size(); i++) {
            String n = update.get(i);
            for (int j = 0; j < i; j++) {
                String prev = fixed.get(j);
                if (orderSet.contains(n + "|" + prev)) {
                    fixed.add(j, n);
                    continue next;
                }
            }
            fixed.add(n);
        }
        return fixed;
    }
}
