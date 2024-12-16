package adventofcode.commons;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AoCBoard<T> {

    public T[][] buffer; // create a safe method to replace
    public final int N; // rows
    public final int M; // cols

    public AoCBoard(Class<T> clazz, int n, int m) {
        N = n; // rows
        M = m; // cols
        buffer = (T[][]) Array.newInstance(clazz, M, N);
    }

    public AoCBoard(T[][] data) {
        N = data[0].length;
        M = data.length;
        buffer = cloneBuffer(data); // create a clone to not to alter original data
    }

    @Override
    public AoCBoard<T> clone() throws CloneNotSupportedException {
        return new AoCBoard<>(cloneBuffer(buffer));
    }

    private T[][] cloneBuffer(T[][] data) {
        T[][] clone = data.clone();
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null) {
                clone[i] = data[i].clone();
            }
        }
        return clone;
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
        return p.x >= 0 && p.x < N && p.y >= 0 && p.y < M;
    }

    public Cell cell(AoCPoint p) {
        if (isValidCell(p)) {
            return new Cell(p.x, p.y, buffer[p.y][p.x]);
        } else {
            return null;
        }
    }

    public Cell cell(AoCPoint p, T defaultValue) {
        if (isValidCell(p)) {
            return new Cell(p.x, p.y, buffer[p.y][p.x], defaultValue);
        } else {
            return new Cell(p.x, p.y, defaultValue, defaultValue);
        }
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
        int result = 0;
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                AoCPoint p = new AoCPoint(n, m);
                T value = buffer[m][n];
                result += fn.apply(p, value);
            }
        }
        return result;
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
        dumpBoard("", cellFormat, null);
    }

    public void dumpBoard(String title, String cellFormat) {
        dumpBoard(title, cellFormat, null);
    }

    public void dumpBoard(String cellFormat, Function<Cell, T> transformer) {
        dumpBoard("", cellFormat, transformer);
    }

    public void dumpBoard(String title, String cellFormat, Function<Cell, T> transformer) {
        System.out.println();
        System.out.println("--- " + title);
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

    public static <T> void dumpBoard(String title, int N, int M, Map<AoCPoint, T> points, BiFunction<AoCPoint, T, String> transformer) {
        System.out.println();
        System.out.println("--- %s".formatted(title));
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                AoCPoint p = new AoCPoint(n, m);
                T v = points.get(p);
                String vs;
                if (transformer != null) {
                    vs = transformer.apply(p, v);
                } else if (v != null) {
                    vs = v.toString();
                } else {
                    vs = " ";
                }
                System.out.print(vs);
            }
            System.out.println();
        }
        System.out.println("---");
    }

    public static void dumpBoard(String title, int N, int M, Set<AoCPoint> points, BiFunction<AoCPoint, Character, String> transformer) {
        System.out.println();
        System.out.println("--- %s".formatted(title));
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                AoCPoint p = new AoCPoint(n, m);
                Character v = points.contains(p) ? 'X' : null;
                String vs;
                if (transformer != null) {
                    vs = transformer.apply(p, v);
                } else if (v != null) {
                    vs = v.toString();
                } else {
                    vs = " ";
                }
                System.out.print(vs);
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
        public final T defaultValue;

        public Cell(int n, int m, T v) {
            this(n, m, v, null);
        }

        public Cell(int n, int m, T v, T defaultValue) {
            this.p = new AoCPoint(n, m);
            this.n = n;
            this.m = m;
            this.v = v;
            this.defaultValue = defaultValue;
        }

        public char getChar(AoCVector dir) {
            return (char) AoCBoard.this.get(p, dir, defaultValue);
        }

        public int getInt(AoCVector dir) {
            return (int) AoCBoard.this.get(p, dir, defaultValue);
        }
    }
}
