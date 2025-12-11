package adventofcode.y2025;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;

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
        List<List<Integer>> wiringsInt = new ArrayList<>();
        List<Long> wiringsBin = new ArrayList<>();
        int[] requirements;

        @Override
        public String toString() {
            String d = Long.toBinaryString(diagram);
            String w = wiringsBin.stream()
                .map(Long::toBinaryString)
                .collect(Collectors.joining(","));
            return "%15s %s".formatted(d, w);
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
            for (var str : strs) {
                List<Integer> wiringInt = new ArrayList<>();
                long wiringBin = 0;
                for (var b : str.replaceAll("[()]", "").split(",")) {
                    wiringInt.add(Integer.parseInt(b));
                    int button = Integer.parseInt(b);
                    wiringBin = wiringBin | (0b1 << (len - button - 1));
                }
                wiringsInt.add(wiringInt);
                wiringsBin.add(wiringBin);
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
            log("%s -> %s%n", m, pushes);
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
        for (var w : m.wiringsBin) {
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
     * ...
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long result = 0;
        for (var m : machines) {
            List<Integer> pushes = resolveMachineJoltage(m);
            log("%s -> %s%n", m, pushes);
            result += pushes.size();
        }
        return result;
    }

    private List<Integer> resolveMachineJoltage(Machine m) {
        LinkedList<Integer> pushes = new LinkedList<>();
        int minpushes = Arrays.stream(m.requirements).max().getAsInt();
        int[] state = new int[m.requirements.length];
        for (int i = minpushes; ; ++i) {
            if (resolveMachineJoltage(m, pushes, state, i))
                return pushes;
        }
    }

    private boolean resolveMachineJoltage(Machine m, LinkedList<Integer> pushes, int[] state, int maxlen) {
        for (int i = 0; i < state.length; ++i) {
            if (state[i] > m.requirements[i])
                return false;
        }

        for (int i = 0; i < m.wiringsInt.size(); ++i) {
            var w = m.wiringsInt.get(i);
            pushes.add(i);
            state = toggle(state, w);
            if (maxlen == 1) {
                if (Arrays.equals(m.requirements, state))
                    return true;
            } else {
                if (resolveMachineJoltage(m, pushes, state, maxlen - 1))
                    return true;
            }
            state = untoggle(state, w);
            pushes.removeLast();
        }
        return false;
    }

    private int[] toggle(int[] state, List<Integer> wiring) {
        for (int b : wiring) {
            state[b]++;
        }
        return state;
    }

    private int[] untoggle(int[] state, List<Integer> wiring) {
        for (int b : wiring) {
            state[b]--;
        }
        return state;
    }
}
