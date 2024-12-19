package adventofcode.commons;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class MemoizationCache<V> {

    public static final <V> MemoizationCache<V> build() {
        return new MemoizationCache<>();
    }

    private final Map<MemoizationKey, MemoizationValue> cache = new HashMap<>();

    public MemoizationValue get(Object... keys) {
        return cache.computeIfAbsent(MemoizationKey.toKey(keys), k -> new MemoizationValue());
    }

    /**
     *
     */
    private record MemoizationKey(Object[] keys) {

        public static final MemoizationKey toKey(Object[] keys) {
            return new MemoizationKey(keys);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            MemoizationKey that = (MemoizationKey) o;
            return Objects.deepEquals(keys, that.keys);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(keys);
        }
    }

    /**
     *
     */
    public class MemoizationValue {

        private V value;

        public V orCompute(Supplier<V> mappingFunction) {
            if (value == null)
                value = mappingFunction.get();
            return value;
        }

        public V getValue() {
            return value;
        }
    }
}
