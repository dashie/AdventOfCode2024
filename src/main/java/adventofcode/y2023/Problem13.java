package adventofcode.y2023;

import adventofcode.commons.AoCBoard;
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

    // List[2] = { rowBitset, colBitset }
    List<List<Long>[]> patterns = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        input.splitInput("\n\n").stream().map(AoCInput::toCharBoard).forEach(b -> {
            List<Long> rowBitsets = buildBitsets(b.rows());
            List<Long> colBitsets = buildBitsets(b.cols());
            patterns.add(new List[]{rowBitsets, colBitsets});
        });
    }

    public List<Long> buildBitsets(Iterable<AoCBoard.Dimension<Character>> dimensions) {
        List<Long> bitsets = new ArrayList<>();
        for (var d : dimensions) bitsets.add(dimensionToBitset(d));
        return bitsets;
    }

    public long dimensionToBitset(AoCBoard.Dimension<Character> dimension) {
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
        for (List<Long>[] p : patterns) {
            for (int i = 1; i < p[0].size(); ++i) {
                if (evalReflectionDiffScore(p[0], i) == 0)
                    result += 100 * i;
            }
            for (int i = 1; i < p[1].size(); ++i) {
                if (evalReflectionDiffScore(p[1], i) == 0)
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
        for (List<Long>[] p : patterns) {
            for (int i = 1; i < p[0].size(); ++i) {
                if (evalReflectionDiffScore(p[0], i) == 1)
                    result += 100 * i;
            }
            for (int i = 1; i < p[1].size(); ++i) {
                if (evalReflectionDiffScore(p[1], i) == 1)
                    result += i;
            }
        }
        return result;
    }
}
