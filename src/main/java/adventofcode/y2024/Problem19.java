package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.*;

/**
 * Day 19: Linen Layout
 * https://adventofcode.com/2024/day/19
 */
public class Problem19 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem19().solve(false);
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
    public Long partOne() throws Exception {
        long result = 0;
        for (String p : patterns) {
            if (isDesignPossible(p)) result++;
        }
        return result;
    }

    private HashMap<String, Boolean> isDesignPossibleCache = new HashMap<>();

    private boolean isDesignPossible(String p) {
        Boolean v = isDesignPossibleCache.get(p);
        if (v == null) {
            v = _isDesignPossible(p);
            isDesignPossibleCache.put(p, v);
        }
        return v;
    }

    private boolean _isDesignPossible(String p) {
        if (p.length() == 0) return true;
        List<Integer> lengths = getBestMatches(p);
        for (int len : lengths) {
            if (isDesignPossible(p.substring(len)))
                return true;
        }
        return false;
    }

    private HashMap<String, List<Integer>> getBestMatchesCache = new HashMap<>();

    private List<Integer> getBestMatches(String p) {
        List<Integer> lengths = getBestMatchesCache.get(p);
        if (lengths == null) {
            lengths = new LinkedList<>();
            SortedSet<String> possibleTowels = towels;
            for (int len = 1; len <= p.length(); len++) {
                String sub = p.substring(0, len);
                possibleTowels = possibleTowels.subSet(sub, sub + AFTER_Z);
                if (possibleTowels.isEmpty()) break;
                if (possibleTowels.contains(sub)) lengths.addFirst(len);
            }
            getBestMatchesCache.put(p, lengths);
        }
        return lengths;
    }

    /**
     * ...They'll let you into the onsen as soon as you have the list.
     * What do you get if you add up the number of different ways
     * you could make each design?
     */
    @Override
    public Long partTwo() throws Exception {
        long result = 0;
        for (String p : patterns) {
            result += countCombinations(p);
        }
        return result;
    }

    private HashMap<String, Long> countCombinationsCache = new HashMap<>();

    private long countCombinations(String p) {
        Long v = countCombinationsCache.get(p);
        if (v == null) {
            v = _countCombinations(p);
            countCombinationsCache.put(p, v);
        }
        return v;
    }

    private Long _countCombinations(String p) {
        if (p.length() == 0) return 1L;
        List<Integer> lengths = getBestMatches(p);
        long n = 0;
        for (int len : lengths) {
            n += countCombinations(p.substring(len));
        }
        return n;
    }
}
