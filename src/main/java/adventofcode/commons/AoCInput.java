package adventofcode.commons;

import java.io.BufferedReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Stream<String> lines() {
        return reader.lines();
    }

    public BufferedReader reader() {
        return reader;
    }
}
