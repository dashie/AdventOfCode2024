package adventofcode.commons;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 *
 */
public class AoCInput {

    private final BufferedReader reader;

    public AoCInput(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public String toString() {
        return reader
            .lines()
            .collect(Collectors.joining("\n"));
    }

    public String[] toArray() {
        return reader
            .lines()
            .toArray(String[]::new);
    }

    public Character[][] toCharMatrix() {
        return lines()
            .map(str -> str
                .chars()
                .mapToObj(c -> (char) c)
                .toArray(Character[]::new))
            .toArray(Character[][]::new);
    }

    public AoCBoard<Character> toCharBoard() {
        return new AoCBoard<>(toCharMatrix());
    }

    public List<String[]> toListOfStringSplits(String splitter) {
        return lines()
            .map(line -> Arrays
                .stream(line.split(splitter))
                .map(String::trim)
                .toArray(String[]::new))
            .collect(Collectors.toUnmodifiableList());
    }

    public List<long[]> toListOfLongSplits(String splitter) {
        return lines()
            .map(line -> Arrays
                .stream(line.split(splitter))
                .map(String::trim)
                .map(Long::parseLong)
                .mapToLong(n -> n)
                .toArray())
            .collect(Collectors.toUnmodifiableList());
    }

    public Stream<String> lines() {
        return reader.lines();
    }

    public BufferedReader reader() {
        return reader;
    }

    public Iterable<String> iterateLines() {
        return () -> lines().iterator();
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

    public Iterable<List<String>> iterateAndSplit(String splitter) {
        return () ->
            lines()
                .map(str -> Arrays
                    .stream(str.split(splitter))
                    .collect(toList()))
                .iterator();
    }

    public Iterable<List<Integer>> iterateAndSplitInt(String splitter) {
        return () ->
            lines()
                .map(str -> Arrays
                    .stream(str.split(splitter))
                    .map(Integer::parseInt)
                    .collect(toList()))
                .iterator();
    }

    public Iterable<List<Long>> iterateAndSplitLong(String splitter) {
        return () ->
            lines()
                .map(str -> Arrays
                    .stream(str.split(splitter))
                    .map(Long::parseLong)
                    .collect(toList()))
                .iterator();
    }
}
