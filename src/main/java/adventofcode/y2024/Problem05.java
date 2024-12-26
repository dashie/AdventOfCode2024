package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.io.BufferedReader;
import java.util.*;

import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.toUnmodifiableList;

/**
 * Day 5: Print Queue
 * https://adventofcode.com/2024/day/5
 */
public class Problem05 extends AoCProblem<Long, Problem05> {

    public static void main(String[] args) throws Exception {
        new Problem05().loadResourceAndSolve(false);
    }

    private Map<Long, Set<Long>> orderMap = new HashMap<>();
    private List<List<Long>> updates = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        BufferedReader reader = input.newReader();
        String line;
        while (!(line = reader.readLine()).isEmpty()) {
            String[] tokens = line.split("\\|");
            long n = parseLong(tokens[0]);
            long u = parseLong(tokens[1]);
            orderMap.compute(n, (k, v) -> v == null ? new HashSet<>() : v)
                    .add(u);
        }
        while ((line = reader.readLine()) != null) {
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
    public Long solvePartOne() throws Exception {
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
                Set<Long> set = orderMap.get(n);
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
    public Long solvePartTwo() throws Exception {
        long result = 0;
        for (List<Long> update : updates) {
            if (!isCorrect(update)) {
                List<Long> fixed = fixOrder(update);
                long middle = fixed.get(fixed.size() / 2);
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
                long prev = fixed.get(j);
                Set<Long> set = orderMap.get(n);
                if (set != null && set.contains(prev)) {
                    fixed.add(j, n);
                    continue next;
                }
            }
            fixed.add(n);
        }
        return fixed;
    }
}
