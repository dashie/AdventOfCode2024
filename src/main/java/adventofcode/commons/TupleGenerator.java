package adventofcode.commons;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TupleGenerator implements Iterator<int[]> {

    public static Stream<int[]> streamOf(int[] max, int sz) {
        return StreamSupport.stream(iterableOf(max, sz).spliterator(), false);
    }

    public static Stream<int[]> streamOf(List<Integer> max, int sz) {
        return StreamSupport.stream(iterableOf(max, sz).spliterator(), false);
    }

    public static Iterable<int[]> iterableOf(int[] max, int sz) {
        TupleGenerator generator = new TupleGenerator(max, sz);
        Iterable<int[]> iterable = () -> generator;
        return iterable;
    }

    public static Iterable<int[]> iterableOf(List<Integer> max, int sz) {
        int[] maxa = max.stream().mapToInt(i -> i).toArray();
        TupleGenerator generator = new TupleGenerator(maxa, sz);
        Iterable<int[]> iterable = () -> generator;
        return iterable;
    }

    private int[] max;
    private int[] tuple;

    public TupleGenerator(int[] max, int sz) {
        this.max = max;
        this.tuple = new int[sz];
        this.tuple[0] = -1;
    }

    @Override
    public boolean hasNext() {
        for (int i = 0; i < tuple.length; ++i) {
            if (tuple[i] < max[i])
                return true;
        }
        return false;
    }

    @Override
    public int[] next() {
        for (int i = 0; i < tuple.length; ++i) {
            tuple[i]++;
            if (tuple[i] <= max[i])
                break;
            tuple[i] = 0;
            if (i == tuple.length - 1)
                throw new NoSuchElementException();
        }
        return tuple;
    }
}
