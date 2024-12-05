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
public class Problem05 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem05().solve(false);
    }

    private Map<Long, Set<Long>> prevMap = new HashMap<>();
    private List<List<Long>> updates = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        String line;
        while (!(line = input.reader().readLine()).isEmpty()) {
            String[] tokens = line.split("\\|");
            Set<Long> set = prevMap.getOrDefault(parseLong(tokens[0]), new HashSet<>());
            set.add(parseLong(tokens[1]));
            prevMap.put(parseLong(tokens[0]), set);
        }
        while ((line = input.reader().readLine()) != null) {
            updates.add(Arrays
                .stream(line.split(","))
                .map(Long::parseLong)
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
        for (List<Long> update : updates) {
            if (isCorrect(update)) {
                long middle = update.get(update.size() / 2);
                result += middle;
            }
        }
        return result;
    }

    private boolean isCorrect(List<Long> update) {
        for (int i = update.size() - 1; i > 0; i--) {
            long n = update.get(i);
            for (int j = 0; j < i; j++) {
                long prev = update.get(j);
                Set<Long> set = prevMap.get(n);
                if (set != null && set.contains(prev)) {
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
        for (List<Long> update : updates) {
            if (!isCorrect(update)) {
                List<Long> fixed = fixOrder(update);
                long middle = fixed.get(update.size() / 2);
                result += middle;
            }
        }
        return result;
    }

    private List<Long> fixOrder(List<Long> update) {
        List<Long> fixed = new ArrayList<>();
        fixed.add(update.get(0));
        next:
        for (int i = 1; i < update.size(); i++) {
            long n = update.get(i);
            for (int j = 0; j < i; j++) {
                long n1 = fixed.get(j);
                Set<Long> set = prevMap.get(n);
                if (set != null && set.contains(n1)) {
                    fixed.add(j, n);
                    continue next;
                }
            }
            fixed.add(n);
        }
        return fixed;
    }
}
