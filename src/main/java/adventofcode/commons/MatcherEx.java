package adventofcode.commons;

import java.util.regex.Pattern;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

/**
 *
 */
public class MatcherEx {

    private static final Pattern BOOLEAN_PATTERN = Pattern.compile("0|f|false", Pattern.CASE_INSENSITIVE);

    private final String[] groups;

    public MatcherEx(String[] groups) {
        this.groups = groups;
    }

    public int count() {
        return groups.length;
    }

    public String get(int i) {
        return groups[i];
    }

    public Integer getInt(int i) {
        String group = groups[i];
        if (group == null) return null;
        return parseInt(group);
    }

    public Long getLong(int i) {
        String group = groups[i];
        if (group == null) return null;
        return parseLong(group);
    }

    public Float getFloat(int i) {
        String group = groups[i];
        if (group == null) return null;
        return parseFloat(group);
    }

    public Double getDouble(int i) {
        String group = groups[i];
        if (group == null) return null;
        return parseDouble(group);
    }

    public Boolean getFlag(int i) {
        String group = groups[i];
        if (group == null) return null;
        return !BOOLEAN_PATTERN.matcher(group).matches();
    }
}
