package adventofcode.commons;

import java.util.ArrayList;
import java.util.List;

public final class AoCCollectionUtils {

    public static <K, V> List<V> remapList(K k, List<V> v) {
        return v == null ? new ArrayList<>() : v;
    }

    public static <K, Integer> int bagInc(K k, Integer v) {
        return v == null ? 1 : (int) v + 1;
    }

    /**
     *
     */
    private AoCCollectionUtils() {
    }
}
