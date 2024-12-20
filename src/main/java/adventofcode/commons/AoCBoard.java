package adventofcode.commons;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 */
public final class AoCBoard<T> {

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

    public static <T> AoCBoard from(Map<AoCPoint, T> points, Class<T> type) {
        int M0 = Integer.MAX_VALUE, M1 = Integer.MIN_VALUE;
        int N0 = Integer.MAX_VALUE, N1 = Integer.MIN_VALUE;
        for (AoCPoint p : points.keySet()) {
            if (p.y < M0) M0 = p.y;
            if (p.y > M1) M1 = p.y;
            if (p.x < N0) N0 = p.x;
            if (p.x > N1) N1 = p.x;
        }
        int M = M1 - M0 + 1;
        int N = N1 - N0 + 1;

        T[][] data = (T[][]) Array.newInstance(type, M, N);
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                data[m][n] = points.get(new AoCPoint(N0 + n, M0 + m));
            }
        }
        return new AoCBoard(data);
    }

    @Override
    public AoCBoard<T> clone() throws CloneNotSupportedException {
        return new AoCBoard<>(cloneBuffer(buffer));
    }

    public AoCRect getRect(int offset) {
        AoCRect rect = AoCRect.of(N - 1, M - 1);
        return rect.expand(-1);
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


    public List<AoCPoint> neighbors(AoCPoint p0, int distance, Predicate<T> filter) {
        List<AoCPoint> points = new ArrayList<>(distance * distance);
        for (int dy = -distance; dy <= distance; ++dy) {
            int dx0 = distance - Math.abs(dy);
            for (int dx = -dx0; dx <= dx0; ++dx) {
                if (dy != 0 || dx != 0) {
                    var p = p0.translate(dx, dy);
                    if (isValidCell(p) && filter.test(get(p))) {
                        points.add(p0.translate(dx, dy));
                    }
                }
            }
        }
        return points;
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

    public static <T> void dumpBoard(String title, Map<AoCPoint, T> points, BiFunction<AoCPoint, T, String> transformer) {
        int M0 = Integer.MAX_VALUE, M1 = Integer.MIN_VALUE;
        int N0 = Integer.MAX_VALUE, N1 = Integer.MIN_VALUE;
        for (AoCPoint p : points.keySet()) {
            if (p.y < M0) M0 = p.y;
            if (p.y > M1) M1 = p.y;
            if (p.x < N0) N0 = p.x;
            if (p.x > N1) N1 = p.x;
        }
        dumpBoard(title, N0, M0, N1 + 1, M1 + 1, points::get, transformer);
    }

    public static <T> void dumpBoard(String title, AoCRect rect, Map<AoCPoint, T> points, BiFunction<AoCPoint, T, String> transformer) {
        dumpBoard(title, rect.p1.x, rect.p1.y, rect.p2.x + 1, rect.p2.y + 1, points::get, transformer);
    }

    public static <T> void dumpBoard(String title, int N, int M, Map<AoCPoint, T> points, BiFunction<AoCPoint, T, String> transformer) {
        dumpBoard(title, 0, 0, N, M, points::get, transformer);
    }

    public static void dumpBoard(String title, int N, int M, Set<AoCPoint> points) {
        dumpBoard(title, N, M, points, null);
    }

    public static void dumpBoard(String title, int N, int M, Set<AoCPoint> points, BiFunction<AoCPoint, Character, String> transformer) {
        dumpBoard(title, 0, 0, N, M, p -> points.contains(p) ? 'X' : null, transformer);
    }

    private static <T> void dumpBoard(String title, int N0, int M0, int N1, int M1, Function<AoCPoint, T> points, BiFunction<AoCPoint, T, String> transformer) {
        System.out.println();
        System.out.println("--- %s".formatted(title));
        for (int m = M0; m < M1; ++m) {
            for (int n = N0; n < N1; ++n) {
                AoCPoint p = new AoCPoint(n, m);
                T v = points.apply(p);
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
