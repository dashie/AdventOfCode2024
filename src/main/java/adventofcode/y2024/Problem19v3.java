package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.MemoizationCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Day 19: Linen Layout
 * https://adventofcode.com/2024/day/19
 */
public class Problem19v3 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem19v3().loadAndSolve(false);
    }

    Pattern towelsPattern;
    List<String> patterns = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        List<String> lines = input.toList();

        // sequences are sorted by length so that larger sequences match before the smaller ones
        String towelsPatternRegex = Arrays.stream(lines.get(0).split(", "))
                                          .sorted((a, b) -> Integer.compare(b.length(), a.length()))
                                          .collect(Collectors.joining("|"));
        towelsPattern = Pattern.compile("^(?:%s)".formatted(towelsPatternRegex));

        lines.subList(2, lines.size()).forEach(patterns::add);
    }

    /**
     * ...To get into the onsen as soon as possible, consult your list of
     * towel patterns and desired designs carefully.
     * How many designs are possible?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = 0;
        for (String p : patterns) {
            if (countCombinations(p, true) > 0) result++;
        }
        return result;
    }

    private MemoizationCache<Long> countCombinationsCache = MemoizationCache.build();

    private Long countCombinations(String p, boolean onlyFirst) {
        return countCombinationsCache.key(p, onlyFirst).andCompute(() -> {
            if (p.length() == 0) return 1L;
            long n = 0;
            // find all matches from the largest to the smallest
            Matcher m = towelsPattern.matcher(p);
            while (m.find()) {
                int len = m.group().length();
                n += countCombinations(p.substring(len), onlyFirst);
                if (len < 2 || (onlyFirst && n > 0)) break;
                m = towelsPattern.matcher(p.substring(0, len - 1));
            }
            return n;
        });
    }

    /**
     * ...They'll let you into the onsen as soon as you have the list.
     * What do you get if you add up the number of different ways
     * you could make each design?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long result = 0;
        for (String p : patterns) {
            result += countCombinations(p, false);
        }
        return result;
    }
}
