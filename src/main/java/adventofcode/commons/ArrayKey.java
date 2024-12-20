package adventofcode.commons;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 */
public record ArrayKey(Object[] keys) {

    public static final ArrayKey toKey(Object[] keys) {
        return new ArrayKey(keys);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ArrayKey that = (ArrayKey) o;
        return Objects.deepEquals(keys, that.keys);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(keys);
    }
}
