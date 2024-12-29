package adventofcode.y2023;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Day 15: Lens Library
 * https://adventofcode.com/2023/day/15
 */
public class Problem15 extends AoCProblem<Long, Problem15> {

    public static void main(String[] args) throws Exception {
        new Problem15().loadResourceAndSolve(false);
    }

    List<String> steps;

    @Override
    public void processInput(AoCInput input) throws Exception {
        steps = input.split(",");
    }

    /**
     * ...Run the HASH algorithm on each step in the initialization
     * sequence. What is the sum of the results? (The initialization
     * sequence is one long line; be careful when copy-pasting it.)
     */
    @Override
    public Long solvePartOne() throws Exception {
        long result = steps.stream().mapToLong(this::hash).sum();
        return result;
    }

    public int hash(String str) {
        // Determine the ASCII code for the current character of the string.
        // Increase the current value by the ASCII code you just determined.
        // Set the current value to itself multiplied by 17.
        // Set the current value to the remainder of dividing itself by 256.
        int hash = 0;
        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            hash += (int) c;
            hash *= 17;
            hash %= 256;
        }
        return hash;
    }

    /**
     * ...
     */
    @Override
    public Long solvePartTwo() throws Exception {
        Pattern stepPattern = Pattern.compile("([a-zA-Z0-9]+)([=-])([1-9])?");
        Map<Integer, List<Lens>> boxes = new HashMap<>();
        next:
        for (String step : steps) {
            Matcher m = stepPattern.matcher(step);
            if (!m.find()) throw new IllegalStateException();
            String label = m.group(1);
            String op = m.group(2);
            int hash = hash(label);
            List<Lens> box = boxes.computeIfAbsent(hash, k -> new LinkedList<>());
            switch (op) {
                case "-" -> box.removeIf(lens -> lens.label.equals(label));
                case "=" -> {
                    int fl = Integer.parseInt(m.group(3));
                    ListIterator<Lens> it = box.listIterator();
                    while (it.hasNext()) {
                        Lens lens = it.next();
                        if (lens.label.equals(label)) {
                            it.set(new Lens(label, fl));
                            continue next;
                        }
                    }
                    box.add(new Lens(label, fl));
                }
            }
        }

        long focusingPower = 0;
        for (var e : boxes.entrySet()) {
            if (e.getValue() != null) {
                int i = 1;
                for (var lens : e.getValue()) {
                    focusingPower += (1 + e.getKey()) * i * lens.focalLength;
                    i++;
                }
            }
        }
        return focusingPower;
    }

    record Lens(String label, int focalLength) {}

}
