package adventofcode.commons;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class LineEx {

    private final String line;

    public LineEx(String line) {
        this.line = line;
    }

    public int getInt(String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        if (!m.find()) throw new IllegalArgumentException();
        return parseInt(m.group(0));
    }
}
