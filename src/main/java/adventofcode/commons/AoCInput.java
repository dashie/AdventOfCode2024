package adventofcode.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Character.getNumericValue;

/**
 *
 */
public class AoCInput {

    private final String inputText;

    private AoCInput(String inputText) {
        this.inputText = inputText;
    }

    @Override
    public String toString() {
        return inputText;
    }

    public int indexOf(String str) {
        return inputText.indexOf(str);
    }

    public AoCInput before(String regex) {
        return new AoCInput(inputText.split(regex)[0]);
    }

    public AoCInput after(String regex) {
        return new AoCInput(inputText.split(regex)[1]);
    }

    public List<String> split(String splitRule) {
        return Arrays.asList(inputText.split(splitRule));
    }

    public List<AoCInput> splitInput(String splitRule) {
        return Arrays.asList(inputText.split(splitRule))
                     .stream().map(AoCInput::new)
                     .toList();
    }

    public static AoCInput fromReader(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder(65536);
        char[] buffer = new char[65536];
        int len = 0;
        while ((len = reader.read(buffer)) != -1) {
            sb.append(buffer, 0, len);
        }
        return new AoCInput(sb.toString());
    }

    public Stream<String> lines() {
        return inputText.lines();
    }

    public BufferedReader newReader() {
        return new BufferedReader(new StringReader(inputText));
    }

    public String[] toArray() {
        return lines()
            .toArray(String[]::new);
    }

    public List<String> toList() {
        return lines()
            .toList();
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

    public Character[][] toCharMatrixUntilEmptyLine() throws Exception {
        return lines()
            .takeWhile(line -> !line.isEmpty())
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
            .collect(Collectors.toList());
    }

    public List<List<String>> toListOfStringList(String splitRule) {
        return lines()
            .map(line -> Arrays.stream(line.split(splitRule))
                               .map(String::trim)
                               .toList())
            .collect(Collectors.toList());
    }

    public List<long[]> toListOfLongArray(String splitRule) {
        return lines()
            .map(line -> Arrays.stream(line.split(splitRule))
                               .map(String::trim)
                               .map(Long::parseLong)
                               .mapToLong(n -> n)
                               .toArray())
            .collect(Collectors.toList());
    }

    public List<List<Long>> toListOfLongList(String splitRule) {
        return lines()
            .map(line -> Arrays.stream(line.split(splitRule))
                               .map(String::trim)
                               .map(Long::parseLong)
                               .toList())
            .collect(Collectors.toList());
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
                                  .collect(Collectors.toList()))
                .iterator();
    }

    public Iterable<List<Integer>> iterateAndSplitInt(String splitRule) {
        return () ->
            lines()
                .map(str -> Arrays.stream(str.split(splitRule))
                                  .map(Integer::parseInt)
                                  .collect(Collectors.toList()))
                .iterator();
    }

    public Iterable<List<Long>> iterateAndSplitLong(String splitRule) {
        return () ->
            lines()
                .map(str -> Arrays.stream(str.split(splitRule))
                                  .map(Long::parseLong)
                                  .collect(Collectors.toList()))
                .iterator();
    }

    public Stream<MatcherEx> pattern(PatternEx pattern) {
        return lines()
            .map(pattern::matches);
    }

    public Stream<MatcherEx> pattern(String regex) {
        PatternEx pattern = PatternEx.compile(regex);
        return lines()
            .map(pattern::matches);
    }
}
