package adventofcode.y2023;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.ArrayList;
import java.util.List;

/**
 * Day 9: Mirage Maintenance
 * https://adventofcode.com/2023/day/9
 */
public class Problem09 extends AoCProblem<Long, Problem09> {

    public static void main(String[] args) throws Exception {
        new Problem09().loadResourceAndSolve(false);
    }

    List<List<Long>> sequences;

    @Override
    public void processInput(AoCInput input) throws Exception {
        sequences = input.lineExs()
            .map(l -> l.splitToLong("\\s+"))
            .toList();
    }

    /**
     * ...Analyze your OASIS report and extrapolate the next
     * value for each history.
     * What is the sum of these extrapolated values?
     */
    @Override
    public Long solvePartOne() throws Exception {
        return sequences.stream()
            .mapToLong(this::extrapolateValue)
            .sum();
    }

    public long extrapolateValue(List<Long> seq0) {
        List<Long> lastValues = new ArrayList<>();
        List<Long> seq = seq0;
        // to optmize time start check the last value and only then
        // all the other values if the last is != 0
        while (seq.getLast() != 0 || seq.stream().anyMatch(n -> n != 0)) {
            List<Long> subseq = new ArrayList<>();
            for (int i = 1; i < seq.size(); ++i)
                subseq.add(seq.get(i) - seq.get(i - 1));
            lastValues.add(subseq.getLast());
            seq = subseq;
        }
        long diff = lastValues.stream()
            .mapToLong(Long::longValue)
            .sum();
        return seq0.getLast() + diff;
    }

    /**
     * ...Analyze your OASIS report again, this time extrapolating
     * the previous value for each history.
     * What is the sum of these extrapolated values?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        return sequences.stream()
            .map(List::reversed) // revert the string to extrapolate the first value
            .mapToLong(this::extrapolateValue)
            .sum();
    }
}
