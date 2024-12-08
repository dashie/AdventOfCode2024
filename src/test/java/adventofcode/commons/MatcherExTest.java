package adventofcode.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

public class MatcherExTest {

    // TODO test compile
    private static final PatternEx PATTERN = PatternEx.compile("^(\\w{3}) ([ab]|(?:[+-]\\d+))(?:, ([+-]\\d+|t|f|0|false))?$", Pattern.CASE_INSENSITIVE);

    @Test
    public void testError() {
        Assertions.assertNull(PATTERN.matches(null));
        Assertions.assertNull(PATTERN.matches(""));
        Assertions.assertNull(PATTERN.matches("tpl"));
        Assertions.assertNull(PATTERN.matches("tpl 3"));
        Assertions.assertNull(PATTERN.matches("tpl 31"));
        Assertions.assertNull(PATTERN.matches("tpl c"));
        Assertions.assertNull(PATTERN.matches("tpl ab"));
        Assertions.assertNull(PATTERN.matches("tpl cde"));
        Assertions.assertNull(PATTERN.matches("tpl a 6"));
        Assertions.assertNull(PATTERN.matches("tpl a 62"));
        Assertions.assertNull(PATTERN.matches("tpl a, 4"));
        Assertions.assertNull(PATTERN.matches("tpl a, 62"));
        Assertions.assertNull(PATTERN.matches("tpl a, =62"));
        Assertions.assertNull(PATTERN.matches("tplg a, -3"));
    }

    @Test
    public void test2Params() {
        MatcherEx m;

        m = PATTERN.matches("tpl +3");
        Assertions.assertEquals(4, m.count());
        Assertions.assertEquals("tpl", m.get(1));
        Assertions.assertEquals(3, m.getInt(2));
        Assertions.assertEquals(null, m.get(3));

        m = PATTERN.matches("inc -52");
        Assertions.assertEquals(4, m.count());
        Assertions.assertEquals("inc", m.get(1));
        Assertions.assertEquals(-52, m.getInt(2));
        Assertions.assertEquals(null, m.get(3));

        m = PATTERN.matches("jmp a");
        Assertions.assertEquals(4, m.count());
        Assertions.assertEquals("jmp", m.get(1));
        Assertions.assertEquals("a", m.get(2));
        Assertions.assertEquals(null, m.get(3));

        m = PATTERN.matches("tpl b, +6");
        Assertions.assertEquals(4, m.count());
        Assertions.assertEquals("tpl", m.get(1));
        Assertions.assertEquals("b", m.get(2));
        Assertions.assertEquals(6, m.getInt(3));

        m = PATTERN.matches("jmp b, F");
        Assertions.assertEquals(4, m.count());
        Assertions.assertEquals("jmp", m.get(1));
        Assertions.assertEquals("b", m.get(2));
        Assertions.assertEquals(false, m.getFlag(3));

        m = PATTERN.matches("jmp b, fAlse");
        Assertions.assertEquals(4, m.count());
        Assertions.assertEquals("jmp", m.get(1));
        Assertions.assertEquals("b", m.get(2));
        Assertions.assertEquals(false, m.getFlag(3));

        m = PATTERN.matches("jmp b, 0");
        Assertions.assertEquals(4, m.count());
        Assertions.assertEquals("jmp", m.get(1));
        Assertions.assertEquals("b", m.get(2));
        Assertions.assertEquals(false, m.getFlag(3));

        m = PATTERN.matches("jmp a, t");
        Assertions.assertEquals(4, m.count());
        Assertions.assertEquals("jmp", m.get(1));
        Assertions.assertEquals("a", m.get(2));
        Assertions.assertEquals(true, m.getFlag(3));
    }
}
