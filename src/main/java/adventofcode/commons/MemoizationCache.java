package adventofcode.commons;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static adventofcode.commons.ArrayKey.toKey;

public class MemoizationCache<V> {

    public static final <V> MemoizationCache<V> build() {
        return new MemoizationCache<>();
    }

    private final Map<ArrayKey, MemoizationValue> cache = new HashMap<>();

    public MemoizationValue key(Object... keys) {
        return cache.computeIfAbsent(toKey(keys), k -> new MemoizationValue());
    }

    /**
     *
     */
    public class MemoizationValue {

        private V value;

        public V andCompute(Supplier<V> mappingFunction) {
            if (value == null)
                value = mappingFunction.get();
            return value;
        }

        public V getValue() {
            return value;
        }
    }
}
