package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;
import adventofcode.commons.MatcherEx;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Day 24:
 * https://adventofcode.com/2024/day/24
 */
public class Problem24 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
//        Problem24 sample = AoCProblem.buildWithSampleResource(Problem24.class);
//        System.out.println(sample.solvePartOne());
//        // sample.dumpDot();
//        System.out.println();
//        System.out.println();
//        System.out.println();


        Problem24 problem = AoCProblem.buildWithInputResource(Problem24.class);
        // problem.dumpDot();
        System.out.println();
        System.out.println();
        System.out.println();

        long part1 = problem.solvePartOne();
        System.out.println(part1);
        if (part1 != 65635066541798L) throw new IllegalStateException();
        System.out.println();

        // part2 : dgr,dtv,fgc,mtj,vvm,z12,z29,z37

        long part2 = problem.solvePartTwo();
        System.out.println(part2);
    }

    enum Operation {
        AND, OR, XOR
    }

    class Node implements Comparable<Node> {

        String id;
        Integer bit = null;
        Operation op = null;
        String input1;
        String input2;

        public Node(String id, Integer bit) {
            this.id = id;
            this.bit = bit;
        }

        public Node(String id, Operation op, String input1, String input2) {
            this.id = id;
            this.op = op;
            this.input1 = input1;
            this.input2 = input2;
        }

        @Override
        public String toString() {
            return id;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(id, node.id);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }

        @Override
        public int compareTo(Node o) {
            boolean startsWithXYZ1 = id.startsWith("x") || id.startsWith("y") || id.startsWith("z");
            boolean startsWithXYZ2 = o.id.startsWith("x") || o.id.startsWith("y") || o.id.startsWith("z");
            if (startsWithXYZ1 && !startsWithXYZ2) return -1;
            if (startsWithXYZ2 && !startsWithXYZ1) return 1;
            return id.compareTo(o.id);
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
        // x00: 1
        for (LineEx line : input.before("\n\n").iterateLineExs()) {
            String id = line.before(":").toString();
            int bit = line.after(": ").getInt();
            nodes.put(id, new Node(id, bit));
        }

        // fcw AND hrn -> jjw
        for (LineEx line : input.after("\n\n").iterateLineExs()) {
            MatcherEx m = line.match("([a-z0-9]+) (AND|OR|XOR) ([a-z0-9]+) -> ([a-z0-9]+)");
            String id = m.get(4);
            nodes.put(id, new Node(
                id,
                Operation.valueOf(m.get(2)),
                m.get(1),
                m.get(3)
            ));
        }
    }

    /**
     *
     */
    @Override
    public Long solvePartOne() throws Exception {
        long n = evalValue("z");
        return n;
    }

    private long evalValue(String prefix) {
        long n = 0;
        for (int i = 0; i < 64; ++i) {
            String id = intToNodeId(prefix, i);
            Node node = nodes.get(id);
            if (node == null) continue;
            long bit = node.evalBit();
            bit = bit << i;
            n += bit;
        }
        return n;
    }

    private String intToNodeId(String prefix, int i) {
        return prefix + (i < 10 ? "0" + i : i);
    }

    record Pair(String a, String b) {}

    /**
     *
     */
    @Override
    public Long solvePartTwo() throws Exception {
//        switchGates("z12", "fgc");
//        switchGates("mtj", "z29");
//        switchGates("dgr", "vvm");
//        switchGates("z37", "dtv");
        // dgr,dtv,fgc,mtj,vvm,z12,z29,z37

//        nodes.values().forEach(Node::reset);
//        dumpSumTest();

        // setValue("x", 0b10101010101010101010101010101010101010101010L);
        // setValue("y", 0b01010101010101010101010101010101010101010101L);
//        nodes.values().forEach(Node::reset);
//        dumpSumTest();

//        setValue("x", 123456789123456789L);
//        setValue("y", 123456789123456789L);
        Set<String> switches = new HashSet<>();
        List<Set<String>> solutions = new ArrayList<>();
        fixCircuit(0, switches, solutions);
        for (var s : solutions) {
            System.out.println(s.stream().toList().stream().sorted().collect(Collectors.joining(",")));
        }

        return 0L;
    }

    public boolean fixCircuit(int zi, Set<String> switches, List<Set<String>> solutions) {
        Set<String> subset = nodeSubsetAroundZBit(zi);
        if (subset.isEmpty()) {
            System.out.println("solution: " + switches);
            checkSumOfFirstNBits(45);
            solutions.add(new HashSet<>(switches));
            return true;
        }

        if (checkSumOfFirstNBits(zi)) {
            return fixCircuit(zi + 1, switches, solutions);
        }

        boolean fixed = false;
        System.out.println("find error at bit: " + zi);
        List<Pair> pairs = generatePairs(subset);
        for (Pair pair : pairs) {
            if (switches.contains(pair.a) || switches.contains(pair.b)) continue;
            if (switchGatesAndCheckLoop(pair.a, pair.b)) {
                if (checkSumOfFirstNBits(zi)) {
                    System.out.println("bit: " + zi + " match: " + pair + " " + switches);
                    switches.add(pair.a);
                    switches.add(pair.b);
                    if (fixCircuit(zi + 1, switches, solutions)) fixed = true;
                    switches.remove(pair.a);
                    switches.remove(pair.b);
                }
                switchGatesAndCheckLoop(pair.b, pair.a);
            }
        }
        return fixed;
    }

    private boolean checkSumOfFirstNBits(int zi) {
        if (!checkSumWithValue(zi,
            0L,
            0L)) return false;

        if (!checkSumWithValue(zi,
            0b11111111111111111111111111111111111111111111L,
            0b11111111111111111111111111111111111111111111L)) return false;

        if (!checkSumWithValue(zi,
            0b10101010101010101010101010101010101010101010L,
            0b01010101010101010101010101010101010101010101L)) return false;

        if (!checkSumWithValue(zi,
            0b01010101010101010101010101010101010101010101L,
            0b10101010101010101010101010101010101010101010L)) return false;

        if (!checkSumWithValue(zi,
            0b01010101010101010101010101010101010101010101L,
            0b01010101010101010101010101010101010101010101L)) return false;

        if (!checkSumWithValue(zi,
            0b10101010101010101010101010101010101010101010L,
            0b10101010101010101010101010101010101010101010L)) return false;

        return true;
    }

    private boolean checkSumWithValue(int zi, long x0, long y0) {
        setValue("x", x0);
        setValue("y", y0);
        nodes.values().forEach(Node::reset);
        long x = evalValue("x");
        long y = evalValue("y");
        long sum = x + y;
        long z = evalValue("z");
        // dumpSumTest();
        long mask = 0xFFFFFFFFFFFFFFFFL << (zi + 1) ^ 0xFFFFFFFFFFFFFFFFL;
        return ((sum & mask) == (z & mask));
    }

    record Visit(String id, int dephth) {}

    public Set<String> nodeSubsetAroundZBit(int zi) {
        String id1 = intToNodeId("z", zi);
        String id2 = intToNodeId("z", zi + 1);
        Set<String> subset = new HashSet<>();
        Deque<Visit> stack = new LinkedList<>();
        stack.add(new Visit(id1, 0));
        stack.add(new Visit(id2, 0));
        while (!stack.isEmpty()) {
            var v = stack.pop();
            if (v.dephth > 3) continue;
            Node n = nodes.get(v.id);
            if (n == null) continue;
            if (!n.isGate()) continue;
            if (!subset.add(v.id)) continue;
            stack.add(new Visit(n.input1, v.dephth + 1));
            stack.add(new Visit(n.input2, v.dephth + 1));
        }
        subset.remove(id2); // remove the next output from the set
        return subset;
    }

    public List<Pair> generatePairs(Set<String> subset) {
        List<Pair> pairs = new ArrayList<>();
        List<String> ids = subset.stream().toList();
        for (int i = 0; i < ids.size(); ++i) {
            for (int j = i + 1; j < ids.size(); ++j) {
                pairs.add(new Pair(ids.get(i), ids.get(j)));
            }
        }
        return pairs;
    }

    private void dumpSumTest() {
        long x = evalValue("x");
        long y = evalValue("y");
        long z = evalValue("z");

        System.out.println();
        System.out.println("        4         3         2         1");
        System.out.println("   5432109876543210987654321098765432109876543210");
        System.out.printf("x: %46s %n", Long.toBinaryString(x));
        System.out.printf("y: %46s %n", Long.toBinaryString(y));
        System.out.printf("z: %46s %s%n", Long.toBinaryString(z), (z != x + y ? "  !!" : ""));
        System.out.printf("s: %46s %n", Long.toBinaryString(x + y));
    }

    private long setValue(String prefix, long value) {
        long n = 0;
        for (int i = 0; i < 64; ++i) {
            String id = intToNodeId(prefix, i);
            Node node = nodes.get(id);
            if (node == null) continue;
            long mask = value & (1L << i);
            node.bit = mask != 0 ? 1 : 0;
        }
        return n;
    }

    public boolean switchGatesAndCheckLoop(String g1, String g2) {
        Node n1 = nodes.remove(g1);
        Node n2 = nodes.remove(g2);
        n1.id = g2;
        n2.id = g1;
        nodes.put(n1.id, n1);
        nodes.put(n2.id, n2);

        if (findLoop(n1) || findLoop(n2)) {
            // restore previous configuration
            n1 = nodes.remove(g1);
            n2 = nodes.remove(g2);
            n1.id = g2;
            n2.id = g1;
            nodes.put(n1.id, n1);
            nodes.put(n2.id, n2);
            return false;
        }

        return true;
    }

    public boolean findLoop(Node n0) {
        String id0 = n0.id;
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

    public void dumpDot() {
        var snodes = nodes.values().stream().sorted().toList();

        for (Node node : snodes) {
            if (node.isGate()) {
                System.out.println("\"" + node.id + "\"" + " [label=\"" + node.op + "\\n" + node.id + "\"]");
            } else {
                System.out.println(node.id + " [label=\"" + node.id + "\\n" + node.bit + "\"]");
            }
        }
        System.out.println();
        List<String> vertexes = new ArrayList<>();
        for (Node node : snodes) {
            if (node.isGate()) {
                vertexes.add(node.input1 + " -> " + "\"" + node.id + "\"");
                vertexes.add(node.input2 + " -> " + "\"" + node.id + "\"");
            }
        }
        vertexes.sort(String::compareTo);
        for (var s : vertexes)
            System.out.println(s);
    }
}
