package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Day 17: Chronospatial Computer
 * https://adventofcode.com/2024/day/17
 */
public class Problem17 extends AoCProblem<String, Problem17> {

    public static void main(String[] args) throws Exception {
        new Problem17().loadResourceAndSolve(false);
    }

    class Machine {

        long[] regs = new long[3]; // a, b, c
        List<Integer> program;
        int pc;

        long combo(long op) {
            return switch ((int) op) {
                case 0, 1, 2, 3 -> op;
                case 4 -> regs[0];
                case 5 -> regs[1];
                case 6 -> regs[2];
                default -> throw new IllegalStateException();
            };
        }
    }

    private Machine machine;

    @Override
    public void processInput(AoCInput input) throws Exception {
        this.machine = new Machine();
        Iterator<LineEx> it = input.iterateLineExs().iterator();
        machine.regs[0] = it.next().getLong("[0-9]+");
        machine.regs[1] = it.next().getLong("[0-9]+");
        machine.regs[2] = it.next().getLong("[0-9]+");
        it.next(); // skip blank line
        machine.program = it.next().getListOfInteger("[0-9,]+", ",");
    }

    /**
     * ...Using the information provided by the debugger, initialize
     * the registers to the given values, then run the program.
     * Once it halts, what do you get if you use commas to join
     * the values it output into a single string?
     */
    @Override
    public String solvePartOne() throws Exception {
        return programToString(run(machine));
    }

    private String programToString(List<Integer> program) {
        return program.stream()
                      .map(n -> n.toString())
                      .collect(Collectors.joining(","));
    }

    private List<Integer> run(Machine m) {
        List<Integer> stdout = new ArrayList<>();
        while (m.pc < m.program.size()) {
            int opcode = m.program.get(m.pc++);
            long op = m.program.get(m.pc++);
            switch (opcode) {
                case 0 -> m.regs[0] = m.regs[0] / (1L << m.combo(op)); // adv
                case 1 -> m.regs[1] = m.regs[1] ^ op; // bxl
                case 2 -> m.regs[1] = m.combo(op) % 8; // bst
                case 3 -> m.pc = m.regs[0] != 0 ? (int) op : m.pc; // jnz
                case 4 -> m.regs[1] = m.regs[1] ^ m.regs[2]; // bxc
                case 5 -> stdout.add((int) (m.combo(op) % 8)); // out
                case 6 -> m.regs[1] = m.regs[0] / (1L << m.combo(op)); // bdv
                case 7 -> m.regs[2] = m.regs[0] / (1L << m.combo(op)); // adv
                default -> throw new IllegalStateException();
            }
        }
        return stdout;
    }

    /**
     * ...What is the lowest positive initial value for register A
     * that causes the program to output a copy of itself?
     */
    @Override
    public String solvePartTwo() throws Exception {
        // Analyzing the sequence manually and based on the machine characteristics (3 bit logic)
        // the resulting pattern changes following the power of 8, so I try to guess the result
        // by approaching it using powers of 8.
        List<Integer> program = machine.program;

        List<Long> matches = Arrays.asList(0L);
        for (int i = program.size() - 1; i >= 0; --i) {
            long p8 = (long) Math.pow(8, i);
            List<Long> newMatches = new ArrayList<>();
            for (Long m : matches) {
                for (int n = 0; n < 8; ++n) {
                    long regA = m + p8 * n;
                    List<Integer> result = runNewMachine(regA);
                    if (matchListFromIndex(result, program, i)) {
                        // collect all the options that match partially the output
                        // we see that the same level value can be the result of different regA values
                        newMatches.add(regA);
                    }
                }
            }
            matches = newMatches;
        }

        // get the lowest value
        return matches.stream().min(Long::compare).get().toString();
    }

    private boolean matchListFromIndex(List<Integer> p1, List<Integer> p2, int startFrom) {
        if (p1.size() != p2.size()) return false;
        for (int j = startFrom; j < p1.size(); ++j) {
            if (!p1.get(j).equals(p2.get(j)))
                return false;
        }
        return true;
    }

    private List<Integer> runNewMachine(long regA) {
        try {
            Machine m = new Machine();
            m.regs[0] = regA;
            m.program = machine.program;
            return run(m);
        } catch (Exception ex) {
            return Collections.EMPTY_LIST;
        }
    }
}

