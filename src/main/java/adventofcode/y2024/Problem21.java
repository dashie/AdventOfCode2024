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
        new Problem21().solve(false);
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
            String code = "%c%c".formatted(c0, cp);

            int lastCost = costMap.getOrDefault(v.p, Integer.MAX_VALUE);
            if (v.cost > lastCost) continue;
            costMap.put(v.p, v.cost);
            if (v.cost < lastCost) {
                // store all minpath combinations because with nested pattern
                // when we expand the sequence the same inner sequence can be expanded
                // in pattern with different length
                // for example: "^<A" and "<^A'"
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
        int cost = (v.sequence.length() > 0
            && !v.sequence.substring(v.sequence.length() - 1).equals(dirCode)) ? 1000 : 1;
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
                        cost += moves.getFirst().length() + 1; //  +1 because we add "A";
                    } else {
                        long shortest = Long.MAX_VALUE;
                        for (String move : moves) {
                            long tmp = wrapper.evalPushSequeceLength(move + "A");
                            if (tmp < shortest) shortest = tmp;
                        }
                        cost += shortest;
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
    public Long partOne() throws Exception {
        Keypad numericKeypad = prepareKeypads(2);
        return evalComplecityScore(numericKeypad);
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
    public Long partTwo() throws Exception {
        Keypad numericKeypad = prepareKeypads(25);
        return evalComplecityScore(numericKeypad);
    }

    // prepare keypads for N "keypadRobots" + 1 for the numericKeypad
    private Keypad prepareKeypads(int keypadRobots) {
        Keypad robotKeypad = null;
        for (int i = 0; i < keypadRobots; ++i) {
            robotKeypad = new Keypad("A", directionalPaths, robotKeypad);
        }
        return new Keypad("A", numericPaths, robotKeypad);
    }
}
