package adventofcode.commons;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class LineEx {

    private final String line;

    public LineEx(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return line;
    }

    public int getInt(String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        if (!m.find()) throw new IllegalArgumentException();
        return parseInt(m.group(0));
    }

    public long getLong(String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        if (!m.find()) throw new IllegalArgumentException();
        return parseLong(m.group(0));
    }

    public String getString(String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        if (!m.find()) throw new IllegalArgumentException();
        return m.group(0);
    }

    public MatcherEx match(String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        if (!m.find()) throw new IllegalArgumentException();
        String[] groups = new String[m.groupCount() + 1];
        for (int i = 0; i < groups.length; ++i) {
            groups[i] = m.group(i);
        }
        return new MatcherEx(groups);
    }

    public int[] getArrayOfInt(String regex, String splitRegex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        if (!m.find()) throw new IllegalArgumentException();
        return Arrays.stream(m.group(0).split(splitRegex))
                     .mapToInt(Integer::parseInt)
                     .toArray();
    }

    public Integer[] getArrayOfIntegers(String regex, String splitRegex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        if (!m.find()) throw new IllegalArgumentException();
        return Arrays.stream(m.group(0).split(splitRegex))
                     .map(Integer::parseInt)
                     .toArray(Integer[]::new);
    }

    public List<Integer> getListOfIntegers(String regex, String splitRegex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        if (!m.find()) throw new IllegalArgumentException();
        return Arrays.stream(m.group(0).split(splitRegex))
                     .map(Integer::parseInt)
                     .toList();
    }
}
