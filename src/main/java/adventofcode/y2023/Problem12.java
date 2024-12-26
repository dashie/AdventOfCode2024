package adventofcode.y2023;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Day 12: Hot Springs
 * https://adventofcode.com/2023/day/12
 */
public class Problem12 extends AoCProblem<Long, Problem12> {

    public static void main(String[] args) throws Exception {
        new Problem12().loadResourceAndSolve(false);
    }

    record Record(String pattern, List<Integer> groups) {}

    private List<Record> records = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        for (LineEx line : input.iterateLineExs()) {
            String pattern = line.getString("^[^ ]+");
            List<Integer> groups = line.getListOfInteger("[0-9,]+$", ",");
            records.add(new Record(pattern, groups));
        }
    }

    /**
     * ...For each row, count all of the different arrangements of
     * operational and broken springs that meet the given criteria.
     * What is the sum of those counts?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = 0;
        for (Record record : records) {
            result += countOptions(record.pattern, record.groups);
        }
        return result;
    }

    private Map<String, Long> countOptionsCache = new HashMap<>();

    public long countOptions(String pattern, List<Integer> groups) {
        String key = pattern + "-" + groups;
        Long count = countOptionsCache.get(key);
        if (count == null) {
            count = _countOptions(pattern, groups);
            countOptionsCache.put(key, count);
        }
        return count;
    }

    public long _countOptions(String pattern, List<Integer> groups) {
        if (pattern.length() == 0) return groups.isEmpty() ? 1 : 0;
        if (groups.isEmpty()) return pattern.contains("#") ? 0 : 1;

        long count = 0;
        char c = pattern.charAt(0);

        int group0 = groups.get(0);
        if (c != '.' && pattern.length() >= group0) { // c == ? or #
            String match = pattern.substring(0, group0);
            if (!match.contains(".")) {
                if (pattern.length() == group0) {
                    count += countOptions(pattern.substring(group0), groups.subList(1, groups.size()));
                } else if (pattern.charAt(group0) != '#') {
                    count += countOptions(pattern.substring(group0 + 1), groups.subList(1, groups.size()));
                }
            }
        }

        if (c != '#') { // c == ? or .
            count += countOptions(pattern.substring(1), groups);
        }

        return count;
    }

    /**
     * ...Unfold your condition records; what is the new sum
     * of possible arrangement counts?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long result = 0;
        for (Record record : records) {
            String pattern5 = record.pattern
                + "?" + record.pattern
                + "?" + record.pattern
                + "?" + record.pattern
                + "?" + record.pattern;

            List<Integer> groups5 = new ArrayList(record.groups.size() * 5);
            groups5.addAll(record.groups);
            groups5.addAll(record.groups);
            groups5.addAll(record.groups);
            groups5.addAll(record.groups);
            groups5.addAll(record.groups);

            result += countOptions(pattern5, groups5);
        }
        return result;
    }
}
