package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.MemoizationCache;

import java.util.*;

/**
 * Day 19: Linen Layout
 * https://adventofcode.com/2024/day/19
 */
public class Problem19v2 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem19v2().loadResourceAndSolve(false);
    }

    public static final char AFTER_Z = (char) ('z' + 1);

    TreeSet<String> towels = new TreeSet<>();
    List<String> patterns = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        List<String> lines = input.toList();
        Arrays.stream(lines.get(0).split(", ")).forEach(towels::add);
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
            List<Integer> lengths = getBestMatches(p);
            long n = 0;
            for (int len : lengths) {
                n += countCombinations(p.substring(len), onlyFirst);
                if (onlyFirst && n > 0) break;
            }
            return n;
        });
    }

    private List<Integer> getBestMatches(String p) {
        List<Integer> lengths = new LinkedList<>();
        SortedSet<String> possibleTowels = towels;
        for (int len = 1; len <= p.length(); len++) {
            String sub = p.substring(0, len);
            possibleTowels = possibleTowels.subSet(sub, sub + AFTER_Z);
            if (possibleTowels.isEmpty()) break;
            if (possibleTowels.contains(sub)) lengths.addFirst(len);
        }
        return lengths;
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
