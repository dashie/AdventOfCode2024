package adventofcode.commons;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AOCBoard<T> {

    public T[][] buffer; // create a safe method to replace
    public final int N;
    public final int M;

    public AOCBoard(Class<T> clazz, int n, int m) {
        N = n;
        M = m;
        buffer = (T[][]) Array.newInstance(clazz, M, N);
    }

    public AOCBoard(T[][] data) {
        N = data[0].length;
        M = data.length;
        buffer = data;
    }

    @Override
    public AOCBoard<T> clone() throws CloneNotSupportedException {
        return new AOCBoard<>(buffer.clone());
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

    public boolean isValidCell(AOCPoint p) {
        return p.x >= 0 && p.x < N
            && p.y >= 0 && p.y < M;
    }

    public T get(AOCPoint p) {
        return get(p.x, p.y);
    }

    public T get(int x, int y) {
        return buffer[y][x];
    }

    public T getWithDefault(int x, int y, T defaultValue) {
        T v = get(x, y);
        return v != null ? v : defaultValue;
    }

    public T getSafe(AOCPoint p) {
        try {
            return get(p.x, p.y);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public T getSafe(AOCPoint p, int xOffset, int yOffset) {
        try {
            return get(p.x + xOffset, p.y + yOffset);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public T getSafeWithDeafult(AOCPoint p, T defaultValue) {
        try {
            return getWithDefault(p.x, p.y, defaultValue);
        } catch (IndexOutOfBoundsException ex) {
            return defaultValue;
        }
    }

    public T getSafeWithDeafult(AOCPoint p, int xOffset, int yOffset, T defaultValue) {
        try {
            return getWithDefault(p.x + xOffset, p.y + yOffset, defaultValue);
        } catch (IndexOutOfBoundsException ex) {
            return defaultValue;
        }
    }

    public int forEach(BiFunction<AOCPoint, T, Boolean> fn) {
        int count = 0;
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                AOCPoint p = new AOCPoint(n, m);
                T value = buffer[m][n];
                if (fn.apply(p, value)) {
                    count++;
                }
            }
        }
        return count;
    }

    public AOCPoint searchFor(T value) {
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                if (buffer[m][n] == value) {
                    AOCPoint p = new AOCPoint(n, m);
                    return p;
                }
            }
        }
        return null;
    }

    public void set(AOCPoint p, T value) {
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

        public final int n;
        public final int m;
        public final T v;

        public Cell(int n, int m, T v) {
            this.n = n;
            this.m = m;
            this.v = v;
        }
    }
}
