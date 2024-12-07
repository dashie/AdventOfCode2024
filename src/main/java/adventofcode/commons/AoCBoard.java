package adventofcode.commons;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AoCBoard<T> {

    public T[][] buffer; // create a safe method to replace
    public final int N;
    public final int M;

    public AoCBoard(Class<T> clazz, int n, int m) {
        N = n;
        M = m;
        buffer = (T[][]) Array.newInstance(clazz, M, N);
    }

    public AoCBoard(T[][] data) {
        N = data[0].length;
        M = data.length;
        buffer = data;
    }

    @Override
    public AoCBoard<T> clone() throws CloneNotSupportedException {
        return new AoCBoard<>(buffer.clone());
    }

    public void fill(T value) {
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                buffer[m][n] = value;
            }
        }
    }

    public void fillRow(int y, T value) {
        for (int n = 0; n < N; ++n) {
            buffer[y][n] = value;
        }
    }

    public void fillRow(int y, T[] values) {
        for (int n = 0; n < values.length; ++n) {
            buffer[y][n] = values[n];
        }
    }

    public boolean isValidCell(AoCPoint p) {
        return p.x >= 0 && p.x < N
            && p.y >= 0 && p.y < M;
    }

    public T get(int x, int y, T defaultValue) {
        try {
            return buffer[y][x];
        } catch (IndexOutOfBoundsException ex) {
            return defaultValue;
        }
    }

    public T get(AoCPoint p, T defaultValue) {
        return get(p.x, p.y, defaultValue);
    }

    public T get(AoCPoint p) {
        return get(p.x, p.y, null);
    }

    public T get(AoCPoint p, int xOffset, int yOffset, T defaultValue) {
        return get(p.x + xOffset, p.y + yOffset, defaultValue);
    }

    public T get(AoCPoint p, int xOffset, int yOffset) {
        return get(p, xOffset, yOffset, null);
    }

    public T get(AoCPoint p, AoCVector v, T defaultValue) {
        return get(p, v.x, v.y, defaultValue);
    }

    public T getOrBlank(AoCPoint p) {
        return get(p, (T) (Character) ' ');
    }

    public T getOrBlank(AoCPoint p, int xOffset, int yOffset) {
        return get(p, xOffset, yOffset, (T) (Character) ' ');
    }

    public T getOrBlank(AoCPoint p, AoCVector v) {
        return get(p, v.x, v.y, (T) (Character) ' ');
    }

    public boolean check(AoCPoint p, T value) {
        T v = get(p, null);
        return v != null && v.equals(value);
    }

    public int forEach(BiFunction<AoCPoint, T, Integer> fn) {
        int count = 0;
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                AoCPoint p = new AoCPoint(n, m);
                T value = buffer[m][n];
                count += fn.apply(p, value);
            }
        }
        return count;
    }

    public AoCPoint searchFor(T value) {
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                if (buffer[m][n] == value) {
                    AoCPoint p = new AoCPoint(n, m);
                    return p;
                }
            }
        }
        return null;
    }

    public void set(AoCPoint p, T value) {
        set(p.x, p.y, value);
    }

    public void set(int x, int y, T value) {
        buffer[y][x] = value;
    }

    public long clear(T newValue) {
        long count = 0;
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                buffer[m][n] = newValue;
                count++;
            }
        }
        return count;
    }

    public long clear(T oldValue, T newValue) {
        long count = 0;
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                if (Objects.equals(buffer[m][n], oldValue)) {
                    buffer[m][n] = newValue;
                    count++;
                }
            }
        }
        return count;
    }

    public void dumpBoard(String cellFormat) {
        dumpBoard(cellFormat, null);
    }

    public void dumpBoard(String cellFormat, Function<Cell, T> transformer) {
        System.out.println("---");
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                T v = buffer[m][n];
                if (transformer != null) {
                    v = transformer.apply(new Cell(n, m, v));
                }
                System.out.printf(cellFormat, v);
            }
            System.out.println();
        }
        System.out.println("---");
    }

    /**
     *
     */
    public class Cell {

        public final AoCPoint p;
        public final int n;
        public final int m;
        public final T v;

        public Cell(int n, int m, T v) {
            this.p = new AoCPoint(n, m);
            this.n = n;
            this.m = m;
            this.v = v;
        }
    }
}
