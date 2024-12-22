package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Day 17: Chronospatial Computer
 * https://adventofcode.com/2024/day/17
 */
public class Problem17v2 extends AoCProblem<String> {

    public static void main(String[] args) throws Exception {
        new Problem17v2().loadResourceAndSolve(false);
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
        machine.program = it.next().getListOfIntegers("[0-9,]+", ",");
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
        // Analyzing the program code
        //
        // 2,4    b = a % 8                    take last 3 bit of A
        // 1,3    b = b ^ 3                    play with these 3 digits using b and c
        // 7,5    c = a / (1L << b)
        // 4,0    b = b ^ c
        // 1,3    b = b ^ 3
        // 0,3    a = a / (1L << 3) = a / 8    divide a/8, that is shift 3 digit right
        // 5,5    out b % 8                    out b
        // 3,0    a != 0 goto 0                repeat
        //
        // We see that the output number depends every time by the first 3 bits of a,
        // and then a is shifted.
        // So trying to guess the result starting from the end, and once we find
        // the solutions for the most right 3 digits we shift left the result and
        // try to guess the next 3 digits.
        // Total attempts:
        //       8 + 8 + 8 + 8 + ... ≈ 8 * 16 (len of the program) ≈ 128
        // plus some combinations if at the same level with have more matches.

        Long regA = guessRegA(0, 0);
        if (regA == null) return "?";
        return Long.toString(regA);
    }

    private Long guessRegA(long prevRegA, int level) {
        List<Integer> program = machine.program;
        int pindex = program.size() - 1 - level;
        if (pindex == -1) return prevRegA;

        Long minorRegA = null;
        prevRegA = prevRegA << 3;

        for (long j = 0; j < 8; ++j) {
            long v = prevRegA + j;
            List<Integer> result = runNewMachine(v);
            if (v != 0 && result.equals(program.subList(pindex, program.size()))) {
                Long regA = guessRegA(v, level + 1);
                if (minorRegA == null || (regA != null && minorRegA > regA)) {
                    minorRegA = regA;
                }
            }
        }

        return minorRegA;
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

