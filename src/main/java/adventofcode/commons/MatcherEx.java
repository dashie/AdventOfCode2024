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

    public static final Pattern BOOLEAN_PATTERN = Pattern.compile("0|f|false|n|no", Pattern.CASE_INSENSITIVE);

    private final String[] groups;

    public MatcherEx(String[] groups) {
        this.groups = groups;
    }

    public int count() {
        return groups.length;
    }

    public boolean has(int i) {
        return get(i) != null;
    }

    public String get(int i) {
        return groups[i];
    }

    public String get(int i, String def) {
        String group = groups[i];
        return (group == null) ? def : group;
    }

    public char getChar(int i, char def) {
        String group = groups[i];
        if (group == null) return def;
        return group.charAt(0);
    }

    public Integer getInt(int i) {
        String group = groups[i];
        if (group == null) return null;
        return parseInt(group);
    }

    public Integer getInt(int i, int def) {
        String group = groups[i];
        if (group == null) return def;
        return parseInt(group);
    }

    public boolean isInt(int i) {
        String group = groups[i];
        if (group == null) return false;
        try {
            parseInt(group);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
