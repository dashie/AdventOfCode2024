package adventofcode.y2015;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.PatternEx;

import java.util.HashMap;
import java.util.Map;

/**
 * Day 23: Opening the Turing Lock
 * https://adventofcode.com/2015/day/23
 */
public class Problem23 extends AoCProblem<Long, Problem23> {

    public static void main(String[] args) throws Exception {
        new Problem23().loadResourceAndSolve(false);
    }

    record Instruction(String id, String r, Integer offset) {}

    PatternEx pattern = PatternEx.compile("^([a-z]{3}) ([^ ,]+)(?:, ([^ ]+))?$");
    private Instruction[] code;

    @Override
    public void processInput(AoCInput input) throws Exception {
        code = input.pattern(pattern)
                    .map(m -> {
                        return new Instruction(
                            m.get(1),
                            m.isInt(2) ? null : m.get(2),
                            m.isInt(2) ? m.getInt(2) : m.getInt(3)
                        );
                    })
                    .toArray(Instruction[]::new);
    }

    /**
     * ...What is the value in register b when the program in your
     * puzzle input is finished executing?
     */
    @Override
    public Long solvePartOne() throws Exception {
        Map<String, Integer> regs = new HashMap<>();
        regs.put("a", 0);
        regs.put("b", 0);
        runCode(regs);
        return (long) regs.get("b");
    }

    private void runCode(Map<String, Integer> regs) {
        int pc = 0;
        endOfProgram:
        while (pc < code.length) {
            Instruction instruction = code[pc];
            switch (instruction.id) {
                case "hlf": // half
                    regs.compute(instruction.r, (k, v) -> v / 2);
                    break;
                case "tpl": // triple
                    regs.compute(instruction.r, (k, v) -> v * 3);
                    break;
                case "inc":
                    regs.compute(instruction.r, (k, v) -> v + 1);
                    break;
                case "jmp":
                    pc += instruction.offset;
                    continue;
                case "jie": // jump if even
                    if (regs.get(instruction.r) % 2 == 0) {
                        pc += instruction.offset;
                        continue;
                    }
                    break;
                case "jio": // jump if 1
                    if (regs.get(instruction.r) == 1) {
                        pc += instruction.offset;
                        continue;
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + instruction.id);
            }
            pc++;
        }
    }

    /**
     * ...The unknown benefactor is very thankful for releasi-- er, helping
     * little Jane Marie with her computer. Definitely not to distract you,
     * what is the value in register b after the program is finished executing
     * if register a starts as 1 instead?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        Map<String, Integer> regs = new HashMap<>();
        regs.put("a", 1);
        regs.put("b", 0);
        runCode(regs);
        return (long) regs.get("b");
    }
}
