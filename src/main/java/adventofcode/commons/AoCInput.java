package adventofcode.commons;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Character.getNumericValue;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 *
 */
public class AoCInput {

    private final BufferedReader reader;

    public AoCInput(BufferedReader reader) {
        this.reader = reader;
    }

    public Stream<String> lines() {
        return reader.lines();
    }

    public BufferedReader reader() {
        return reader;
    }

    public String[] toArray() {
        return lines()
            .toArray(String[]::new);
    }

    // I cannot use toString or when I am in debug and the IDE try call toString then I consume all the reader
    public String toSingleString() {
        return lines()
            .collect(joining("\n"));
    }

    public int[][] toIntMatrix() {
        return lines()
            .map(s -> s.chars()
                       .map(Character::getNumericValue)
                       .toArray())
            .toArray(int[][]::new);
    }

    public Character[][] toCharMatrix() {
        return lines()
            .map(str -> str.chars()
                           .mapToObj(c -> (char) c)
                           .toArray(Character[]::new))
            .toArray(Character[][]::new);
    }

    public Character[][] toCharMatrixEmptyLine() throws Exception {
        List<String> boardLines = new ArrayList<>();
        String line;
        while (!(line = reader().readLine()).isEmpty()) {
            boardLines.add(line);
        }
        return boardLines.stream()
                         .map(str -> str.chars()
                                        .mapToObj(c -> (char) c)
                                        .toArray(Character[]::new))
                         .toArray(Character[][]::new);
    }

    public Integer[][] toIntegerMatrix() {
        return lines()
            .map(str -> str.chars()
                           .mapToObj(c -> getNumericValue(c))
                           .toArray(Integer[]::new))
            .toArray(Integer[][]::new);
    }

    public AoCBoard<Character> toCharBoard() {
        return new AoCBoard<>(toCharMatrix());
    }

    public AoCBoard<Integer> toIntBoard() {
        return new AoCBoard<>(toIntegerMatrix());
    }

    public List<String[]> toListOfStringArray(String splitRule) {
        return lines()
            .map(line -> Arrays.stream(line.split(splitRule))
                               .map(String::trim)
                               .toArray(String[]::new))
            .collect(toList());
    }

    public List<List<String>> toListOfStringList(String splitRule) {
        return lines()
            .map(line -> Arrays.stream(line.split(splitRule))
                               .map(String::trim)
                               .toList())
            .collect(toList());
    }

    public List<long[]> toListOfLongArray(String splitRule) {
        return lines()
            .map(line -> Arrays.stream(line.split(splitRule))
                               .map(String::trim)
                               .map(Long::parseLong)
                               .mapToLong(n -> n)
                               .toArray())
            .collect(toList());
    }

    public List<List<Long>> toListOfLongList(String splitRule) {
        return lines()
            .map(line -> Arrays.stream(line.split(splitRule))
                               .map(String::trim)
                               .map(Long::parseLong)
                               .toList())
            .collect(toList());
    }

    public Iterable<String> iterateLines() {
        return () -> lines().iterator();
    }

    public Iterable<LineEx> iterateLineExs() {
        return () -> lines()
            .map(LineEx::new)
            .iterator();
    }

    public Iterable<Integer> iterateInt() {
        return () -> lines()
            .map(Integer::parseInt)
            .iterator();
    }

    public Iterable<Long> iterateLong() {
        return () -> lines()
            .map(Long::parseLong)
            .iterator();
    }

    public Iterable<List<String>> iterateAndSplit(String splitRule) {
        return () ->
            lines()
                .map(str -> Arrays.stream(str.split(splitRule))
                                  .collect(toList()))
                .iterator();
    }

    public Iterable<List<Integer>> iterateAndSplitInt(String splitRule) {
        return () ->
            lines()
                .map(str -> Arrays.stream(str.split(splitRule))
                                  .map(Integer::parseInt)
                                  .collect(toList()))
                .iterator();
    }

    public Iterable<List<Long>> iterateAndSplitLong(String splitRule) {
        return () ->
            lines()
                .map(str -> Arrays.stream(str.split(splitRule))
                                  .map(Long::parseLong)
                                  .collect(toList()))
                .iterator();
    }

    public Stream<MatcherEx> patter(PatternEx pattern) {
        return lines()
            .map(pattern::matches);
    }
}
