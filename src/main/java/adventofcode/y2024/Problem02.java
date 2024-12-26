package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

/**
 * Day 2: Red-Nosed Reports
 * https://adventofcode.com/2024/day/2
 */
public class Problem02 extends AoCProblem<Long, Problem02> {

    public static void main(String[] args) throws Exception {
        new Problem02().loadResourceAndSolve(false);
    }

    private List<List<Long>> reports = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        reports = input.toListOfLongList(" ");
    }

    /**
     * ...Analyze the unusual data from the engineers. How many reports are safe?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = 0;
        for (List<Long> report : reports) {
            if (isSafe(report)) {
                result++;
            }
        }
        return result;
    }

    private static boolean isSafe(List<Long> report) {
        float signum = 0;
        for (int i = 1; i < report.size(); i++) {
            long diff = report.get(i) - report.get(i - 1);
            long step = abs(diff);
            if (step < 1 || step > 3) {
                return false;
            }
            if (i == 1) {
                signum = signum(diff);
            } else if (signum != signum(diff)) {
                return false;
            }
        }
        return true;
    }

    /**
     * ...Update your analysis by handling situations where the Problem Dampener
     * can remove a single level from unsafe reports. How many reports are now safe?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long result = 0;
        next:
        for (List<Long> report : reports) {
            if (isSafe(report)) {
                result++;
            } else {
                for (int i = 0; i < report.size(); i++) {
                    List<Long> subReport = new ArrayList<>(report);
                    subReport.remove(i);
                    if (isSafe(subReport)) {
                        result++;
                        continue next;
                    }
                }
            }
        }
        return result;
    }
}
