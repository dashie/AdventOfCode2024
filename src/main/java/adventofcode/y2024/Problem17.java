package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.pow;

/**
 * Day 17: Chronospatial Computer
 * https://adventofcode.com/2024/day/17
 */
public class Problem17 extends AoCProblem<String> {

    public static void main(String[] args) throws Exception {
        new Problem17().solve(false);
    }

    class Machine {

        long[] regs = new long[3]; // a, b, c
        List<Integer> program;
        int pc;
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
        machine.program = it.next().getListOfIntegers("[0-9,]+", ",");
    }

    /**
     * ...Using the information provided by the debugger, initialize
     * the registers to the given values, then run the program.
     * Once it halts, what do you get if you use commas to join
     * the values it output into a single string?
     */
    @Override
    public String partOne() throws Exception {
        return programToString(run(machine));
    }

    private String programToString(List<Integer> program) {
        return program.stream()
                      .map(n -> n.toString())
                      .collect(Collectors.joining(","));
    }

    private List<Integer> run(Machine machine) {
        List<Integer> stdout = new ArrayList<>();
        while (machine.pc < machine.program.size()) {
            int opcode = machine.program.get(machine.pc++);
            switch (opcode) {
                case 0 -> { // adv
                    long op = consumeOperand(machine, false);
                    machine.regs[0] = machine.regs[0] / (long) pow(2, op);
                }
                case 1 -> { // bxl
                    long op = consumeOperand(machine, true);
                    machine.regs[1] = machine.regs[1] ^ op;
                }
                case 2 -> { // bst
                    long op = consumeOperand(machine, false);
                    machine.regs[1] = op % 8;
                }
                case 3 -> { // jnz
                    long op = consumeOperand(machine, true);
                    if (machine.regs[0] != 0) {
                        machine.pc = (int) op;
                    }
                }
                case 4 -> { // bxc
                    long op = consumeOperand(machine, true); // read but ignore op
                    machine.regs[1] = machine.regs[1] ^ machine.regs[2];
                }
                case 5 -> { // out
                    long op = consumeOperand(machine, false);
                    int v = (int) (op % 8);
                    stdout.add(v);
                }
                case 6 -> { // bdv
                    long op = consumeOperand(machine, false);
                    machine.regs[1] = machine.regs[0] / (long) pow(2, op);
                }
                case 7 -> { // adv
                    long op = consumeOperand(machine, false);
                    machine.regs[2] = machine.regs[0] / (long) pow(2, op);
                }
                default -> throw new IllegalStateException();
            }
        }

        return stdout;
    }

    private long consumeOperand(Machine machine, boolean isLiteral) {
        int value = machine.program.get(machine.pc++);
        if (isLiteral) return value;
        switch (value) {
            case 0, 1, 2, 3:
                return value;
            case 4:
                return machine.regs[0];
            case 5:
                return machine.regs[1];
            case 6:
                return machine.regs[2];
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * ...What is the lowest positive initial value for register A
     * that causes the program to output a copy of itself?
     */
    @Override
    public String partTwo() throws Exception {
        // Analyzing the sequence manually and based on the machine characteristics (3 bit logic)
        // the resulting pattern changes following the power of 8, so I try to guess the result
        // by approaching it using powers of 8.
        long baseValue = 0;
        for (int i = machine.program.size() - 1; i >= 0; --i) {
            long p8 = (long) Math.pow(8, i);
            for (int n = 0; ; ++n) {
                long regA = baseValue + p8 * n;
                List<Integer> result = runMachineWithRegistryA(regA);
                if (matchProgram(result, machine.program, i)) {
                    // System.out.println(programToString(tryRegA(regA)) + " <<< " + regA);
                    baseValue = regA;
                    break;
                }
            }
        }
        return Long.toString(baseValue);
    }

    private boolean matchProgram(List<Integer> p1, List<Integer> p2, int startFrom) {
        if (p1.size() != p2.size()) return false;
        for (int j = startFrom; j < p1.size(); ++j) {
            if (!p1.get(j).equals(p2.get(j)))
                return false;
        }
        return true;
    }

    private List<Integer> runMachineWithRegistryA(long regA) {
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
