package adventofcode.commons;

import java.util.ArrayList;
import java.util.List;

public final class AoCCollectionUtils {

    public static final <K, V> List<V> remapList(K keys, List<V> v) {
        return v == null ? new ArrayList<>() : v;
    }

    /**
     *
     */
    private AoCCollectionUtils() {
    }
}
