package adventofcode.commons;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PatternEx {

    public final static PatternEx compile(String regex) {
        return new PatternEx(Pattern.compile(regex));
    }

    private static final Pattern INT_PATTERN = Pattern.compile("-?[0-9]+");

    /**
     *
     */
    private final Pattern pattern;

    private PatternEx(Pattern pattern) {
        this.pattern = pattern;
    }

    public Stream<String> results(String str, int group) {
        return pattern
            .matcher(str)
            .results()
            .map(r -> r.group(group));
    }

    public String[] findGroups(String str) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            String[] groups = new String[matcher.groupCount() + 1];
            for (int i = 0; i < groups.length; ++i) {
                groups[i] = matcher.group(i);
            }
            return groups;
        }
        return null;
    }

    public int toInt(String str) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            String matchedString = matcher.group(0);
            return Integer.parseInt(matcher.group());
        }
        throw new IllegalStateException("Cannot match \"%s\" to int".formatted(str));
    }

    public String[] matchAndSplit(String str, String splitRegex) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            String matchedString = matcher.group(0);
            return matchedString.split(splitRegex);
        }
        return null;
    }

    public Integer[] matchAndSplitToIntArray(String str, String splitRegex) {
        return Arrays.stream(matchAndSplit(str, splitRegex))
                     .map(Integer::parseInt)
                     .toArray(Integer[]::new);
    }

    public List<Integer> matchAndSplitToIntList(String str, String splitRegex) {
        return Arrays.stream(matchAndSplit(str, splitRegex))
                     .map(String::trim)
                     .map(Integer::parseInt)
                     .collect(Collectors.toList());
    }

    public List<Long> matchAndSplitToLongList(String str, String splitRegex) {
        return Arrays.stream(matchAndSplit(str, splitRegex))
                     .map(String::trim)
                     .map(Long::parseLong)
                     .collect(Collectors.toList());
    }

    public static Integer parseInt(String str) {
        Matcher m = INT_PATTERN.matcher(str);
        if (m.find()) {
            return Integer.parseInt(m.group(0));
        }
        return null;
    }
}
