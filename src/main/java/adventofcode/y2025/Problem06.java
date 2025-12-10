package adventofcode.y2025;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Day 6: Trash Compactor
 * https://adventofcode.com/2025/day/6
 */
public class Problem06 extends AoCProblem<Long, Problem06> {

    public static void main(String[] args) throws Exception {
        new Problem06().loadResourceAndSolve(false);
    }

    private Character[][] worksheet;
    private int MAXCOL;
    private int OPROW;

    @Override
    public void processInput(AoCInput input) throws Exception {
        worksheet = input.toCharMatrix();
        MAXCOL = worksheet[0].length;
        OPROW = worksheet.length - 1;
    }

    /**
     * ...Of course, the actual worksheet is much wider.
     * You'll need to make sure to unroll it completely
     * so that you can read the problems clearly.
     *
     * Solve the problems on the math worksheet.
     * What is the grand total found by adding together
     * all of the answers to the individual problems?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = 0;
        int col = 0;
        while (col < MAXCOL) {

            char opchar = worksheet[OPROW][col];
            var op = evalOperation(opchar);
            long groupvalue = parseLineValue(col, 0);
            for (int i = 1; i < OPROW; ++i) {
                long n = parseLineValue(col, i);
                groupvalue = op.apply(groupvalue, n);
            }
            result += groupvalue;

            col++;
            while (col < MAXCOL && worksheet[OPROW][col] == ' ')
                col++;
        }
        return result;
    }

    /**
     * ...Cephalopod math is written right-to-left in columns.
     * Each number is given in its own column, with the most
     * significant digit at the top and the least significant
     * digit at the bottom...
     *
     * Solve the problems on the math worksheet again.
     * What is the grand total found by adding together
     * all of the answers to the individual problems?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long result = 0;
        int col = MAXCOL - 1; // from RIGHT to LEFT
        while (col >= 0) {

            BiFunction<Long, Long, Long> op = null;
            LinkedList<Long> values = new LinkedList<>();
            while (op == null) {
                var v = parseColumnValue(col);
                values.add(v);
                op = evalOperation(worksheet[OPROW][col]);
                col--;
            }
            col--; // next group

            var groupvalue = values.removeFirst();
            for (var v : values)
                groupvalue = op.apply(groupvalue, v);
            result += groupvalue;
        }
        return result;
    }

    private long parseLineValue(int col, int row) {
        while (worksheet[row][col] == ' ') col++;
        long v = Character.getNumericValue(worksheet[row][col++]);
        while (col < MAXCOL && worksheet[row][col] != ' ') {
            v = v * 10 + Character.getNumericValue(worksheet[row][col]);
            col++;
        }
        return v;
    }

    private long parseColumnValue(int col) {
        int row = 0;
        while (worksheet[row][col] == ' ') row++;
        long v = Character.getNumericValue(worksheet[row++][col]);
        while (row < OPROW && worksheet[row][col] != ' ') {
            v = v * 10 + Character.getNumericValue(worksheet[row][col]);
            row++;
        }
        return v;
    }

    private BiFunction<Long, Long, Long> evalOperation(char c) {
        return switch (c) {
            case '+' -> (a, b) -> a + b;
            case '*' -> (a, b) -> a * b;
            default -> null;
        };
    }
}
