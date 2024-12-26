package adventofcode.commons;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class LineEx implements CharSequence {

    private final String line;

    public LineEx(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return line;
    }

    @Override
    public int length() {
        return line.length();
    }

    @Override
    public char charAt(int index) {
        return line.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return line.subSequence(start, end);
    }

    public LineEx before(String regex) {
        return new LineEx(line.split(regex)[0]);
    }

    public LineEx after(String regex) {
        return new LineEx(line.split(regex)[1]);
    }

    public boolean isEmpty() {
        return "".equals(line);
    }

    public List<LineEx> split(String regex) {
        return Arrays.stream(line.split(regex))
                     .map(LineEx::new)
                     .toList();
    }

    public List<String> splitToString(String regex) {
        return Arrays.stream(line.split(regex))
                     .toList();
    }

    public List<Integer> splitToInteger(String regex) {
        return Arrays.stream(line.split(regex))
                     .map(Integer::parseInt)
                     .toList();
    }

    public List<Long> splitToLong(String regex) {
        return Arrays.stream(line.split(regex))
                     .map(Long::parseLong)
                     .toList();
    }

    public int getInt() {
        return parseInt(line);
    }

    public int getInt(String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        if (!m.find()) throw new IllegalArgumentException();
        return parseInt(m.group(0));
    }

    public long getLong() {
        return parseLong(line);
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
        return match(regex, 0);
    }

    public MatcherEx match(String regex, int flags) {
        Pattern p = Pattern.compile(regex, flags);
        Matcher m = p.matcher(line);
        if (!m.find()) throw new IllegalArgumentException("Invalid pattern \"%s\" on \"%s\"".formatted(regex, line));
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
        String match = m.groupCount() > 0 ? m.group(1) : m.group(0);
        return Arrays.stream(match.trim().split(splitRegex))
                     .mapToInt(Integer::parseInt)
                     .toArray();
    }

    public Integer[] getArrayOfInteger(String regex, String splitRegex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        if (!m.find()) throw new IllegalArgumentException();
        String match = m.groupCount() > 0 ? m.group(1) : m.group(0);
        return Arrays.stream(match.trim().split(splitRegex))
                     .map(Integer::parseInt)
                     .toArray(Integer[]::new);
    }

    public List<Integer> getListOfInteger(String regex, String splitRegex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        if (!m.find()) throw new IllegalArgumentException();
        String match = m.groupCount() > 0 ? m.group(1) : m.group(0);
        return Arrays.stream(match.trim().split(splitRegex))
                     .map(Integer::parseInt)
                     .toList();
    }

    public List<Long> getListOfLong(String regex, String splitRegex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        if (!m.find()) throw new IllegalArgumentException();
        String match = m.groupCount() > 0 ? m.group(1) : m.group(0);
        return Arrays.stream(match.trim().split(splitRegex))
                     .map(Long::parseLong)
                     .toList();
    }

}
