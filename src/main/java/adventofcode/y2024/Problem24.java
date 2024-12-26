package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;
import adventofcode.commons.MatcherEx;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Day 24: Crossed Wires
 * https://adventofcode.com/2024/day/24
 */
public class Problem24 extends AoCProblem<String, Problem24> {

    public static void main(String[] args) throws Exception {
        Problem24 problem = AoCProblem.buildWithInputResource(Problem24.class);
        // problem.dumpDotGraph();
        problem.solve();
        // problem.dumpDotGraph();
    }

    public static final int MAX_BITS = 64;

    enum Operation {
        AND, OR, XOR
    }

    class Node implements Comparable<Node> {

        String output;
        Integer bit = null;
        Operation op = null;
        final String input1;
        final String input2;

        public Node(String output, Integer bit) {
            this.output = output;
            this.bit = bit;
            this.input1 = null;
            this.input2 = null;
        }

        public Node(String output, Operation op, String input1, String input2) {
            this.output = output;
            this.op = op;
            this.input1 = input1;
            this.input2 = input2;
        }

        @Override
        public String toString() {
            return output;
        }

        @Override
        public int compareTo(Node o) {
            // using this sort criteria to improve serialization to dot language
            boolean startsWithXYZ1 = output.startsWith("x") || output.startsWith("y") || output.startsWith("z");
            boolean startsWithXYZ2 = o.output.startsWith("x") || o.output.startsWith("y") || o.output.startsWith("z");
            if (startsWithXYZ1 && !startsWithXYZ2) return -1;
            if (startsWithXYZ2 && !startsWithXYZ1) return 1;
            return output.compareTo(o.output);
        }

        public boolean isGate() {
            return op != null;
        }

        public void reset() {
            if (isGate()) bit = null;
        }

        public Integer evalBit() {
            if (bit == null) {
                bit = switch (op) {
                    case OR -> nodes.get(input1).evalBit() | nodes.get(input2).evalBit();
                    case AND -> nodes.get(input1).evalBit() & nodes.get(input2).evalBit();
                    default -> nodes.get(input1).evalBit() ^ nodes.get(input2).evalBit();
                };
            }
            return bit;
        }
    }

    Map<String, Node> nodes = new HashMap<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        for (LineEx line : input.before("\n\n").iterateLineExs()) {
            String output = line.before(":").toString();
            int bit = line.after(": ").getInt();
            nodes.put(output, new Node(output, bit));
        }
        for (LineEx line : input.after("\n\n").iterateLineExs()) {
            MatcherEx m = line.match("([a-z0-9]+) (AND|OR|XOR) ([a-z0-9]+) -> ([a-z0-9]+)");
            String output = m.get(4);
            nodes.put(output, new Node(output, Operation.valueOf(m.get(2)), m.get(1), m.get(3)));
        }
    }

    /**
     * ...Simulate the system of gates and wires.
     * What decimal number does it output on the wires starting with z?
     */
    @Override
    public String solvePartOne() throws Exception {
        long n = readRegister("z");
        return Long.toString(n);
    }

    /**
     * It reads all zX bits and returns the related integer value.
     * Prefix can be "z", "x" or "y".
     */
    private long readRegister(String prefix) {
        long n = 0;
        for (int i = 0; i < MAX_BITS; ++i) {
            String output = intToOutput(prefix, i);
            Node node = nodes.get(output);
            if (node == null) continue;
            long bit = node.evalBit();
            bit = bit << i;
            n += bit;
        }
        return n;
    }

    /**
     * Prefix can be "z", "x" or "y".
     */
    private String intToOutput(String prefix, int i) {
        return prefix + (i < 10 ? "0" + i : i);
    }

    /**
     * ...Your system of gates and wires has four pairs of gates which need
     * their output wires swapped - eight wires in total.
     * Determine which four pairs of gates need their outputs swapped so that
     * your system correctly performs addition; what do you get if you sort
     * the names of the eight wires involved in a swap and then join those
     * names with commas?
     */
    @Override
    public String solvePartTwo() throws Exception {

        Set<String> swaps = new HashSet<>();
        List<Set<String>> solutions = new ArrayList<>();
        fixCircuit(0, swaps, solutions);
        if (solutions.size() > 1) throw new IllegalStateException("Too many solutions");

        String solution = solutions.stream()
                                   .findFirst().get().stream()
                                   .toList().stream().sorted().collect(Collectors.joining(","));
        return solution;
    }

    private void resetAll() {
        nodes.values().forEach(Node::reset);
    }

    record Pair(String a, String b) {}

    public boolean fixCircuit(int zth, Set<String> swaps, List<Set<String>> solutions) {
        Set<String> fullAdderSubset = collectFullAdderNodesSubset(zth);
        if (fullAdderSubset.isEmpty()) {
            checkSumOnNBits(MAX_BITS);
            solutions.add(new HashSet<>(swaps));
            return true;
        }

        if (checkSumOnNBits(zth)) {
            return fixCircuit(zth + 1, swaps, solutions);
        }

        boolean fixed = false;
        List<Pair> pairs = generateAllPairs(fullAdderSubset);
        for (Pair pair : pairs) {
            if (swaps.contains(pair.a) || swaps.contains(pair.b)) continue;
            if (switchOutputsAndCheckForLoops(pair.a, pair.b)) {
                if (checkSumOnNBits(zth)) {
                    swaps.add(pair.a);
                    swaps.add(pair.b);
                    if (fixCircuit(zth + 1, swaps, solutions)) fixed = true;
                    swaps.remove(pair.a);
                    swaps.remove(pair.b);
                }
                switchOutputsAndCheckForLoops(pair.b, pair.a);
            }
        }
        return fixed;
    }

    public boolean checkSumOnNBits(int nbits) {
        if (!checkSumOnNBits(nbits, 0L, 0L)) return false;
        if (!checkSumOnNBits(nbits, 1L, 1L)) return false;

        if (!checkSumOnNBits(nbits,
            0b10000000000000000000000000000000000000000000L,
            0b10000000000000000000000000000000000000000000L)) return false;

        if (!checkSumOnNBits(nbits,
            0b11111111111111111111111111111111111111111111L,
            0b11111111111111111111111111111111111111111111L)) return false;

        if (!checkSumOnNBits(nbits,
            0b10101010101010101010101010101010101010101010L,
            0b01010101010101010101010101010101010101010101L)) return false;

        if (!checkSumOnNBits(nbits,
            0b01010101010101010101010101010101010101010101L,
            0b10101010101010101010101010101010101010101010L)) return false;

        if (!checkSumOnNBits(nbits,
            0b01010101010101010101010101010101010101010101L,
            0b01010101010101010101010101010101010101010101L)) return false;

        if (!checkSumOnNBits(nbits,
            0b10101010101010101010101010101010101010101010L,
            0b10101010101010101010101010101010101010101010L)) return false;

        if (!checkSumOnNBits(nbits,
            0b11111111111111111111111111111111111111111111L,
            0b1L)) return false;

        return true;
    }

    private boolean checkSumOnNBits(int nbits, long x0, long y0) {
        writeRegister("x", x0);
        writeRegister("y", y0);
        resetAll();
        long x = readRegister("x");
        long y = readRegister("y");
        long z = readRegister("z");
        long sum = x + y;
        // dumpSumTest();
        long mask = 0xFFFFFFFFFFFFFFFFL << (nbits + 1) ^ 0xFFFFFFFFFFFFFFFFL;
        return (sum & mask) == (z & mask);
    }

    private long writeRegister(String prefix, long value) {
        long n = 0;
        for (int i = 0; i < MAX_BITS; ++i) {
            String id = intToOutput(prefix, i);
            Node node = nodes.get(id);
            if (node == null) continue;
            long mask = value & (1L << i);
            node.bit = mask != 0 ? 1 : 0;
        }
        return n;
    }

    record Visit(String output, int dephth) {}

    /**
     * Collect the subset of nodes that determines the value of the Z-th bit.
     */
    public Set<String> collectFullAdderNodesSubset(int zth) {
        String out1 = intToOutput("z", zth);
        String out2 = intToOutput("z", zth + 1);
        Set<String> subset = new HashSet<>();
        Deque<Visit> stack = new LinkedList<>();
        stack.add(new Visit(out1, 0));
        stack.add(new Visit(out2, 0));
        while (!stack.isEmpty()) {
            var v = stack.pop();
            if (v.dephth > 3) continue;
            Node n = nodes.get(v.output);
            if (n == null) continue;
            if (!n.isGate()) continue;
            if (!subset.add(v.output)) continue;
            stack.add(new Visit(n.input1, v.dephth + 1));
            stack.add(new Visit(n.input2, v.dephth + 1));
        }
        subset.remove(out2); // remove the next output from the set
        return subset;
    }

    public List<Pair> generateAllPairs(Set<String> subset) {
        List<Pair> pairs = new ArrayList<>();
        List<String> ids = subset.stream().toList();
        for (int i = 0; i < ids.size(); ++i) {
            for (int j = i + 1; j < ids.size(); ++j) {
                pairs.add(new Pair(ids.get(i), ids.get(j)));
            }
        }
        return pairs;
    }

    public boolean switchOutputsAndCheckForLoops(String g1, String g2) {
        // try the switch
        Node n1 = nodes.remove(g1);
        Node n2 = nodes.remove(g2);
        n1.output = g2;
        n2.output = g1;
        nodes.put(n1.output, n1);
        nodes.put(n2.output, n2);

        // check for loops
        if (findLoops(n1) || findLoops(n2)) {
            // restore previous configuration
            n1 = nodes.remove(g1);
            n2 = nodes.remove(g2);
            n1.output = g2;
            n2.output = g1;
            nodes.put(n1.output, n1);
            nodes.put(n2.output, n2);
            return false;
        }
        return true;
    }

    public boolean findLoops(Node n0) {
        String id0 = n0.output;
        Set<String> visited = new HashSet<>();
        Deque<String> stack = new LinkedList<>();
        stack.add(id0);
        while (!stack.isEmpty()) {
            var id = stack.pop();
            Node n = nodes.get(id);
            if (n == null) continue;
            if (!visited.add(id)) {
                if (id.equals(id0))
                    return true; // loop found
                continue;
            }
            stack.add(n.input1);
            stack.add(n.input2);
        }
        return false;
    }

    private void dumpSumTest() {
        long x = readRegister("x");
        long y = readRegister("y");
        long z = readRegister("z");

        System.out.println();
        System.out.println("        4         3         2         1");
        System.out.println("   5432109876543210987654321098765432109876543210");
        System.out.printf("x: %46s %n", Long.toBinaryString(x));
        System.out.printf("y: %46s %n", Long.toBinaryString(y));
        System.out.printf("z: %46s %s%n", Long.toBinaryString(z), (z != x + y ? "  !!" : ""));
        System.out.printf("s: %46s %n", Long.toBinaryString(x + y));
    }

    public void dumpDotGraph() {
        var sortedNodes = nodes.values().stream().sorted().toList();

        System.out.println("digraph G {");
        System.out.println();

        for (Node node : sortedNodes) {
            if (node.isGate()) {
                System.out.println("\"" + node.output + "\"" + " [label=\"" + node.op + "\\n" + node.output + "\"]");
            } else {
                System.out.println(node.output + " [label=\"" + node.output + "\\n" + node.bit + "\"]");
            }
        }
        System.out.println();

        List<String> vertexes = new ArrayList<>();
        for (Node node : sortedNodes) {
            if (node.isGate()) {
                vertexes.add(node.input1 + " -> " + "\"" + node.output + "\"");
                vertexes.add(node.input2 + " -> " + "\"" + node.output + "\"");
            }
        }
        vertexes.sort(String::compareTo);
        for (var s : vertexes)
            System.out.println(s);

        System.out.println("}");
        System.out.println();
    }
}
