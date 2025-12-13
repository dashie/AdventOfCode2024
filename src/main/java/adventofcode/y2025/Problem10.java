package adventofcode.y2025;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;
import adventofcode.commons.TupleGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Day 10: Factory
 * https://adventofcode.com/2025/day/10
 */
public class Problem10 extends AoCProblem<Long, Problem10> {

    public static void main(String[] args) throws Exception {
        new Problem10().loadResourceAndSolve(false);
    }

    private class Machine {

        int len;
        long diagram;
        long[] wiringMasks = new long[0];
        int[][] wiringVectors = new int[0][0];

        int[] requirements;
        int[] requirementsPriority;

        @Override
        public String toString() {
            String d = Long.toBinaryString(diagram);
            String w = Arrays.stream(wiringMasks)
                .mapToObj(Long::toBinaryString)
                .collect(Collectors.joining(","));
            return "%15s %40s".formatted(d, w);
        }

        public void parseDiagram(String str) {
            len = str.length() - 2;
            diagram = Long.parseLong(
                str.replaceAll("[\\[\\]]", "")
                    .replaceAll("\\.", "0")
                    .replaceAll("#", "1"),
                2);
        }

        public void parseWirings(List<String> strs) {
            wiringMasks = new long[strs.size()];
            wiringVectors = new int[strs.size()][len];
            int i = 0;
            for (var str : strs) {
                long mask = 0;
                for (var b : str.replaceAll("[()]", "").split(",")) {
                    int button = Integer.parseInt(b);
                    mask = mask | (0b1 << (len - button - 1));
                    wiringVectors[i][button] = 1;
                }
                wiringMasks[i] = mask;
                i++;
            }
        }

        public void parseRequirements(String str) {
            requirements = Arrays.stream(str.replaceAll("[{}]", "").split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        }
    }

    private List<Machine> machines;

    @Override
    public void processInput(AoCInput input) throws Exception {
        machines = input.lineExs()
            .map(line -> {
                Machine machine = new Machine();
                LineEx diagramPart = line.before(" ");
                machine.parseDiagram(diagramPart.toString());
                List<LineEx> tailParts = line.after(" ").split(" ");
                machine.parseWirings(tailParts
                    .subList(0, tailParts.size() - 1)
                    .stream()
                    .map(LineEx::toString)
                    .toList());
                machine.parseRequirements(tailParts.getLast().toString());
                return machine;
            })
            .toList();
    }

    /**
     * ...Analyze each machine's indicator light diagram and button
     * wiring schematics. What is the fewest button presses required
     * to correctly configure the indicator lights on all of the
     * machines?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = 0;
        for (var m : machines) {
            List<Long> pushes = resolveMachineDiagram(m);
            // log("%s -> %s%n", m, pushes);
            result += pushes.size();
        }
        return result;
    }

    private List<Long> resolveMachineDiagram(Machine m) {
        LinkedList<Long> pushes = new LinkedList<>();
        for (int i = 1; ; ++i) {
            if (resolveMachineDiagram(m, pushes, 0L, i))
                return pushes;
        }
    }

    private boolean resolveMachineDiagram(Machine m, LinkedList<Long> pushes, long state, int maxlen) {
        for (var w : m.wiringMasks) {
            pushes.add(w);
            if (maxlen == 1) {
                if ((state ^ w) == m.diagram)
                    return true;
            } else {
                if (resolveMachineDiagram(m, pushes, state ^ w, maxlen - 1))
                    return true;
            }
            pushes.removeLast();
        }
        return false;
    }

    /**
     * ...Analyze each machine's joltage requirements and button
     * wiring schematics. What is the fewest button presses required
     * to correctly configure the joltage level counters on all of
     * the machines?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long result = 0;
        for (var m : machines) {
            var s = gaussianSolver(m.wiringVectors, m.requirements);
            if (s == null) {
                log("%50s = KO <---%n", Arrays.toString(m.requirements));
            } else {
                long sum = Arrays.stream(s).sum();
                result += sum;
                log("%50s = %-10s %s%n", Arrays.toString(m.requirements), sum, Arrays.toString(s));
            }
        }
        return result;
    }

    private int[] gaussianSolver(int[][] a, int[] r) {

        // eval contraints
        int MAX_PUSHES = Arrays.stream(r).sum();
        int[] constraints = new int[a.length];
        Arrays.fill(constraints, MAX_PUSHES);
        for (int i = 0; i < r.length; ++i) {
            for (int j = 0; j < a.length; ++j) {
                if (a[j][i] > 0 && r[i] < constraints[j])
                    constraints[j] = r[i];
            }
        }

        // create AR matrix
        List<int[]> ar = new ArrayList<>(r.length);
        for (int i = 0; i < r.length; ++i) {
            int[] line = new int[a.length + 1];
            for (int j = 0; j < a.length; j++)
                line[j] = a[j][i];
            line[a.length] = r[i];
            ar.add(line);
        }
        // dumpMatrix(ar);

        // gaussian elimination
        gaussianElimination(ar);
        // dumpMatrix(ar);

        // find best solution
        int[] solution = findBestSolutions(ar, constraints);
        return solution;
    }

    private void gaussianElimination(List<int[]> ar) {
        int m = ar.size();
        int n = ar.get(0).length - 1; // al cols without last one (R)

        int rowIndex = 0;  // current row where to start search for pivot
        for (int colIndex = 0; colIndex < n && rowIndex < m; colIndex++) {

            // search pivot
            int pivotIndex = -1;
            for (int i = rowIndex; i < m; i++) {
                if (ar.get(i)[colIndex] != 0) {
                    pivotIndex = i;
                    break;
                }
            }
            if (pivotIndex == -1) continue; // no pivot found

            // move up the pivot row
            int[] pivotRow = ar.get(pivotIndex);
            if (pivotIndex != rowIndex) {
                int[] tmp = ar.get(rowIndex);
                ar.set(rowIndex, pivotRow);
                ar.set(pivotIndex, tmp);
            }

            // reduce rows under pivot
            for (int i = rowIndex + 1; i < m; i++) {
                int[] row = ar.get(i);
                if (row[colIndex] != 0) {
                    int factorRow = pivotRow[colIndex];
                    int factorPivot = row[colIndex];
                    for (int j = colIndex; j <= n; j++)
                        row[j] = row[j] * factorRow - pivotRow[j] * factorPivot;
                }
            }

            rowIndex++; // next row
        }
    }

    private int[] findBestSolutions(List<int[]> ar, int[] contraints) {
        int m = ar.size();
        int n = ar.get(0).length - 1;  // number of variables

        // find pivot and free variables (-1 for free variables/columns)
        boolean[] isPivot = new boolean[n];
        int[] pivotCol = new int[m];
        Arrays.fill(pivotCol, -1);
        for (int i = 0; i < m; i++) {
            int[] row = ar.get(i);
            for (int c = 0; c < n; c++) {
                if (row[c] != 0) {
                    isPivot[c] = true;
                    pivotCol[i] = c;
                    break;
                }
            }
        }

        // lista variabili libere
        List<Integer> freeVars = new ArrayList<>();
        List<Integer> freeVarsMax = new ArrayList<>();
        for (int c = 0; c < n; c++) {
            if (!isPivot[c]) {
                freeVars.add(c);
                freeVarsMax.add(contraints[c]);
            }
        }

        int[] bestSolution = null;
        int[] sol = new int[n];

        if (freeVars.isEmpty()) {
            if (findSolution(ar, pivotCol, sol))
                bestSolution = sol.clone();
        } else {
            for (var tuple : TupleGenerator.iterableOf(freeVarsMax, freeVars.size())) {
                Arrays.fill(sol, 0);
                for (int i = 0; i < freeVars.size(); ++i)
                    sol[freeVars.get(i)] = tuple[i];
                if (findSolution(ar, pivotCol, sol)) {
                    if (Arrays.stream(sol).allMatch(s -> s >= 0)) {
                        if (bestSolution != null) {
                            int bestSum = Arrays.stream(bestSolution).sum();
                            int solSum = Arrays.stream(sol).sum();
                            if (bestSum <= solSum) continue;
                        }
                        bestSolution = sol.clone();
                    }
                }
            }
        }

        return bestSolution;
    }

    private boolean findSolution(List<int[]> ar, int[] pivotCol, int[] sol) {
        int m = ar.size();
        int n = ar.get(0).length - 1;  // number of variables

        for (int r = m - 1; r >= 0; r--) {

            // row without pivot, skip it
            int pcol = pivotCol[r];
            if (pcol == -1)
                continue;

            int rhs = ar.get(r)[n];
            int[] row = ar.get(r);

            // subtract found values (free or pivot)
            for (int c = pcol + 1; c < n; c++) {
                rhs -= row[c] * sol[c];
            }

            // accept only integer solutions
            if (rhs % row[pcol] != 0)
                return false;

            sol[pcol] = rhs / row[pcol];
        }

        return true;
    }

    private void dumpMatrix(List<int[]> m) {
        log("-----%n");
        for (var l : m) {
            for (int i = 0; i < l.length - 1; ++i)
                log("%3s", l[i]);
            log(" : %s%n", l[l.length - 1]);
        }
        log("-----%n");
    }
}
