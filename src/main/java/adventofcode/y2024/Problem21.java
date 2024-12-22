package adventofcode.y2024;

import adventofcode.commons.*;

import java.util.*;

import static java.util.Arrays.asList;

/**
 * Day 21: Keypad Conundrum
 * https://adventofcode.com/2024/day/21
 */
public class Problem21 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem21().loadAndSolve(false);
    }

    AoCBoard<Character> numericKeypad = new AoCBoard<>(new Character[][]{
        {'7', '8', '9'},
        {'4', '5', '6'},
        {'1', '2', '3'},
        {' ', '0', 'A'},
    });
    AoCBoard<Character> directionalKeypad = new AoCBoard<>(new Character[][]{
        {' ', '^', 'A'},
        {'<', 'v', '>'},
    });

    Map<String, List<String>> numericPaths = new HashMap<>();
    Map<String, List<String>> directionalPaths = new HashMap<>();
    List<String> codes;


    @Override
    public void processInput(AoCInput input) throws Exception {
        codes = input.toList();
        evalShortestPaths(numericKeypad, numericPaths);
        evalShortestPaths(directionalKeypad, directionalPaths);
    }

    private void evalShortestPaths(AoCBoard<Character> keypad, Map<String, List<String>> map) {
        keypad.forEach((p, v) -> {
            if (v == ' ') return 0;
            evalShortestPath(p, keypad, map);
            return 1;
        });
    }

    record Visit(AoCPoint p, String sequence, int cost) {}

    private void evalShortestPath(AoCPoint p0, AoCBoard<Character> keypad, Map<String, List<String>> map) {
        char c0 = keypad.get(p0);
        Map<AoCPoint, Integer> costMap = new HashMap<>();
        PriorityQueue<Visit> stack = new PriorityQueue<>(Comparator.comparingInt(a -> a.cost));
        stack.add(new Visit(p0, "", 0));

        while (!stack.isEmpty()) {
            var v = stack.poll();
            char cp = keypad.get(v.p, ' ');
            if (cp == ' ') continue;

            int lastCost = costMap.getOrDefault(v.p, Integer.MAX_VALUE);
            if (v.cost > lastCost) continue;
            costMap.put(v.p, v.cost);

            String code = "%c%c".formatted(c0, cp);
            if (v.cost < lastCost) {
                // store all min-path combinations because, with nested patterns,
                // expanding a sequence can result in the same inner sequence
                // being expanded into sequences of different lengths.
                // for example:
                //    if you want to press "2" you have 2 options: "^<A" and "<^A" (same length)
                //    with 1 layer you still have 2 sequence of same length
                //        ^<A : <Av<A>>^A
                //        <^A : v<<A>^A>A
                //    but with 2 layers the sequences are:
                //        ^<A : v<<A>>^A<vA<A>>^AvAA^<A>A
                //        <^A : <vA<AA>>^AvA^<A>AvA^A
                //    it's like if with the 2nd option you need less moves to return the robot to "A"
                map.put(code, new ArrayList<>(asList(v.sequence)));
            } else {
                map.get(code).add(v.sequence);
            }

            stack.add(nextVisit(v, '^'));
            stack.add(nextVisit(v, '<'));
            stack.add(nextVisit(v, 'v'));
            stack.add(nextVisit(v, '>'));
        }
    }

    private Visit nextVisit(Visit v, char dirCode) {
        int slen = v.sequence.length();
        int cost = (slen > 0 && v.sequence.charAt(slen - 1) != dirCode) ? 1000 : 1;
        AoCVector d = AoCVector.charArrowToMatrixDirection(dirCode);
        return new Visit(v.p.translate(d), v.sequence + dirCode, v.cost + cost);
    }

    class Keypad {

        private String pos;
        private Map<String, List<String>> paths;
        private Keypad wrapper;
        private final MemoizationCache<Long> decodePushSequeceCache = new MemoizationCache<>();

        public Keypad(String pos, Map<String, List<String>> paths, Keypad wrapper) {
            this.pos = pos;
            this.paths = paths;
            this.wrapper = wrapper;
        }

        /**
         * Can be use only with low recursion, or the string concatenation consumes all the memory
         */
        public String evalPushSequece(String digits) {
            String sequence = "";
            for (int i = 0; i < digits.length(); ++i) {
                char digit = digits.charAt(i);
                String code = pos + digit;
                pos = Character.toString(digit);
                List<String> moves = paths.get(code);
                if (moves == null) throw new IllegalStateException();
                if (wrapper == null) {
                    sequence += moves.getFirst() + "A";
                } else {
                    sequence += moves.stream()
                                     .map(m -> wrapper.evalPushSequece(m + "A"))
                                     .reduce((a, b) -> a.length() < b.length() ? a : b)
                                     .get();
                }
            }
            return sequence;
        }

        public long evalPushSequeceLength(String digits) {
            return decodePushSequeceCache.key(digits).andCompute(() -> {
                long cost = 0;
                for (int i = 0; i < digits.length(); ++i) {
                    char digit = digits.charAt(i);
                    String code = pos + digit;
                    pos = Character.toString(digit);
                    List<String> moves = paths.get(code);
                    if (moves == null) throw new IllegalStateException();
                    if (wrapper == null) {
                        cost += moves.getFirst().length() + 1; // +1 because we add "A";
                    } else {
                        cost += moves.stream()
                                     .mapToLong(m -> wrapper.evalPushSequeceLength(m + "A"))
                                     .min().getAsLong();
                    }
                }
                return cost;
            });
        }
    }

    /**
     * ...Find the fewest number of button presses you'll need to perform in order
     * to cause the robot in front of the door to type each code.
     * What is the sum of the complexities of the five codes on your list?
     */
    @Override
    public Long solvePartOne() throws Exception {
        Keypad numericKeypad = prepareKeypads(2);
        return evalComplecityScore(numericKeypad);
    }

    // prepare keypads for N numericKeypads + 1 for the numericKeypad
    private Keypad prepareKeypads(int robots) {
        Keypad keypad = null;
        for (int i = 0; i < robots; ++i) {
            keypad = new Keypad("A", directionalPaths, keypad);
        }
        return new Keypad("A", numericPaths, keypad);
    }

    private long evalComplecityScore(Keypad numericKeypad) {
        long result = 0;
        for (String code : codes) {
            long numPart = Long.parseLong(code.substring(0, code.length() - 1));
            long len = numericKeypad.evalPushSequeceLength(code);
            result += numPart * len;
        }
        return result;
    }

    /**
     * ...This time, many more robots (25+1) are involved...
     * ...Find the fewest number of button presses you'll need to perform in order
     * to cause the robot in front of the door to type each code.
     * What is the sum of the complexities of the five codes on your list?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        Keypad numericKeypad = prepareKeypads(25);
        return evalComplecityScore(numericKeypad);
    }
}
