package adventofcode.y2023;

import adventofcode.commons.Board;
import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.ArrayList;
import java.util.List;

/**
 * Day 13: Point of Incidence
 * https://adventofcode.com/2023/day/13
 */
public class Problem13 extends AoCProblem<Long, Problem13> {

    public static void main(String[] args) throws Exception {
        new Problem13().loadResourceAndSolve(false);
    }

    record Pattern(Board<Character> schema, List<Long> rowsbits, List<Long> colsbits) {}

    // List[2] = { rowBitset, colBitset }
    List<Pattern> patterns = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        input.splitInput("\n\n").stream().map(AoCInput::toCharBoard).forEach(b -> {
            List<Long> rowsbits = patternToBitset(b.rows());
            List<Long> colsbits = patternToBitset(b.cols());
            patterns.add(new Pattern(b, rowsbits, colsbits));
        });
    }

    public List<Long> patternToBitset(Iterable<Board.Dimension<Character>> dimensions) {
        List<Long> bitset = new ArrayList<>();
        for (var d : dimensions) bitset.add(dimensionToBitset(d));
        return bitset;
    }

    public long dimensionToBitset(Board.Dimension<Character> dimension) {
        long mask = 0;
        int i = 0;
        for (var cell : dimension) {
            if (cell.v == '#') mask = mask | (1 << i);
            i++;
        }
        return mask;
    }

    /**
     * ...Find the line of reflection in each of the patterns
     * in your notes.
     * What number do you get after summarizing all of your notes?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = 0;
        for (Pattern p : patterns) {
            for (int i = 1; i < p.rowsbits.size(); ++i) {
                if (evalReflectionDiffScore(p.rowsbits, i) == 0)
                    result += 100 * i;
            }
            for (int i = 1; i < p.colsbits.size(); ++i) {
                if (evalReflectionDiffScore(p.colsbits, i) == 0)
                    result += i;
            }
        }
        return result;
    }

    // return the number of differences
    public int evalReflectionDiffScore(List<Long> bitsets, int i) {
        if (i < 1 || i >= bitsets.size()) throw new IllegalStateException();
        int score = 0;
        int size = 0;
        while (i + size < bitsets.size() && i - 1 - size >= 0) {
            long diff = bitsets.get(i + size) ^ bitsets.get(i - 1 - size);
            score += Long.bitCount(diff);
            size++;
        }
        return score;
    }

    public void dumpMapBitset(List<Long> bitset) {
        for (var n : bitset) {
            String str = "00000000000000000000" + Long.toBinaryString(n);
            str = str.substring(str.length() - 20);
            log("%s%n", str);
        }
        log("%n");
    }

    /**
     * ...In each pattern, fix the smudge and find the different
     * line of reflection.
     * What number do you get after summarizing the new reflection
     * line in each pattern in your notes?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long result = 0;
        for (Pattern p : patterns) {
            for (int i = 1; i < p.rowsbits.size(); ++i) {
                if (evalReflectionDiffScore(p.rowsbits, i) == 1)
                    result += 100 * i;
            }
            for (int i = 1; i < p.colsbits.size(); ++i) {
                if (evalReflectionDiffScore(p.colsbits, i) == 1)
                    result += i;
            }
        }
        return result;
    }
}
