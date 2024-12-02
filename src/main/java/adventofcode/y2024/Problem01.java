package adventofcode.y2024;

import adventofcode.commons.AOCProblem;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Day 1: Historian Hysteria
 * https://adventofcode.com/2024/day/1
 */
public class Problem01 extends AOCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem01().solve(false);
    }

    private List<Long> list1 = new ArrayList<>();
    private List<Long> list2 = new ArrayList<>();

    @Override
    public void processInput(BufferedReader reader) throws Exception {
        reader.lines()
              .forEach(line -> {
                  String[] parts = line.split("\s+");
                  list1.add(Long.parseLong(parts[0]));
                  list2.add(Long.parseLong(parts[1]));
              });

        list1.sort(Long::compareTo);
        list2.sort(Long::compareTo);
    }

    /**
     * ...To find the total distance between the left list and the right list,
     * add up the distances between all of the pairs you found.
     * In the example above, this is 2 + 1 + 0 + 1 + 2 + 5, a total distance of 11!
     *
     * Your actual left and right lists contain many location IDs.
     * What is the total distance between your lists?
     */
    @Override
    protected Long partOne() throws Exception {
        long result = 0;
        for (int i = 0; i < list1.size(); i++) {
            result += Math.abs(list1.get(i) - list2.get(i));
        }
        return result;
    }

    /**
     * ...So, for these example lists, the similarity score at the end
     * of this process is 31 (9 + 4 + 0 + 0 + 9 + 9).
     *
     * Once again consider your left and right lists.
     * What is their similarity score?
     */
    @Override
    protected Long partTwo() throws Exception {
        HashMap<Long, Long> map = new HashMap<>();
        for (int i = 0; i < list2.size(); i++) {
            long n = list2.get(i);
            map.compute(n, (k, v) -> v == null ? 1 : v + 1);
        }

        long result = 0;
        for (int i = 0; i < list1.size(); i++) {
            long n = list1.get(i);
            long count = map.getOrDefault(n, 0L);
            result += n * count;
        }
        return result;
    }
}
