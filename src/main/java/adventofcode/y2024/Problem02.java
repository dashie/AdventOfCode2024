package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static java.util.stream.Collectors.toUnmodifiableList;

/**
 * Day 2: Red-Nosed Reports
 * https://adventofcode.com/2024/day/2
 */
public class Problem02 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem02().solve(false);
    }

    private List<List<Long>> reports = new ArrayList<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        input.lines()
             .forEach(line -> {
                 reports.add(Arrays
                     .stream(line.split(" "))
                     .map(Long::parseLong)
                     .collect(toUnmodifiableList()));
             });

    }

    /**
     * ...Analyze the unusual data from the engineers. How many reports are safe?
     */
    @Override
    protected Long partOne() throws Exception {
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
    protected Long partTwo() throws Exception {
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
