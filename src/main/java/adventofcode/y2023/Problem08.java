package adventofcode.y2023;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCMath;
import adventofcode.commons.AoCProblem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Day 8: Haunted Wasteland
 * https://adventofcode.com/2023/day/8
 */
public class Problem08 extends AoCProblem<Long, Problem08> {

    public static void main(String[] args) throws Exception {
        new Problem08().loadResourceAndSolve(false);
    }

    List<Character> instructions;
    Map<String, String[]> graph = new HashMap<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        instructions = input.before("\n\n").toCharList();

        // FCG = (PLG, GXC)
        input
            .after("\n\n")
            .pattern("([A-Z]+) = \\(([A-Z]+), ([A-Z]+)\\)")
            .forEach(m -> graph.put(m.get(1), new String[]{m.get(2), m.get(3)}));

    }

    /**
     * Starting at AAA, follow the left/right instructions.
     * How many steps are required to reach ZZZ?
     */
    @Override
    public Long solvePartOne() throws Exception {
        String node = "AAA";
        for (int i = 0; ; ++i) {
            if ("ZZZ".equals(node)) return (long) i;
            String[] choice = graph.get(node);
            node = switch (instructions.get(i % instructions.size())) {
                case 'L' -> choice[0];
                default -> choice[1];
            };
        }
    }


    /**
     * ...Simultaneously start on every node that ends with A.
     * How many steps does it take before you're only on nodes
     * that end with Z?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        List<String> nodes = graph.keySet().stream()
            .filter(s -> s.endsWith("A"))
            .toList();

        List<Long> periods = nodes.stream()
            .map(this::findStepsPeriod)
            .toList();

        return AoCMath.lcm(periods);
    }

    private long findStepsPeriod(String node) {
        long period = -1;
        for (int i = 0; ; ++i) {
            if (node.endsWith("Z")) {
                if (period == -1) {
                    period = i;
                } else if (i - period == period) {
                    log("%s %d%n", node, i);
                    return period;
                } else throw new IllegalStateException();
            }
            String[] choice = graph.get(node);
            node = switch (instructions.get(i % instructions.size())) {
                case 'L' -> choice[0];
                default -> choice[1];
            };
        }
    }
}
