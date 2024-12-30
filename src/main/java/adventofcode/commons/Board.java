package adventofcode.commons;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 */
public final class Board<T> {

    /**
     *
     */
    public static <T> Board from(String data) {
        Character[][] charMatrix = data.lines()
            .map(str -> str.chars()
                .mapToObj(c -> (char) c)
                .toArray(Character[]::new))
            .toArray(Character[][]::new);
        return new Board(charMatrix);
    }

    /**
     *
     */
    public static <T> Board from(Map<Point, T> points, Class<T> type) {
        int M0 = Integer.MAX_VALUE, M1 = Integer.MIN_VALUE;
        int N0 = Integer.MAX_VALUE, N1 = Integer.MIN_VALUE;
        for (Point p : points.keySet()) {
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
                data[m][n] = points.get(new Point(N0 + n, M0 + m));
            }
        }
        return new Board(data);
    }

    public T[][] buffer; // create a safe method to replace
    public final int N; // rows
    public final int M; // cols

    public Board(Class<T> clazz, int n, int m) {
        N = n; // rows
        M = m; // cols
        buffer = (T[][]) Array.newInstance(clazz, M, N);
    }

    public Board(T[][] data) {
        N = data[0].length;
        M = data.length;
        buffer = cloneBuffer(data); // create a clone to not to alter original data
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Board<?> board = (Board<?>) o;
        return Objects.deepEquals(buffer, board.buffer);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(buffer);
    }

    public int N() {
        return N;
    }

    public int M() {
        return M;
    }

    @Override
    public Board<T> clone() {
        return new Board<>(cloneBuffer(buffer));
    }

    public Rect getRect(int offset) {
        Rect rect = Rect.of(N - 1, M - 1);
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

    public boolean isValidCell(Point p) {
        return p.x >= 0 && p.x < N && p.y >= 0 && p.y < M;
    }

    public Cell cell(Point p) {
        if (isValidCell(p)) {
            return new Cell(p.x, p.y, buffer[p.y][p.x]);
        } else {
            return null;
        }
    }

    public Cell cell(Point p, T defaultValue) {
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

    public T get(Point p, T defaultValue) {
        return get(p.x, p.y, defaultValue);
    }

    public T get(Point p) {
        return get(p.x, p.y, null);
    }

    public T get(Point p, int xOffset, int yOffset, T defaultValue) {
        return get(p.x + xOffset, p.y + yOffset, defaultValue);
    }

    public T get(Point p, int xOffset, int yOffset) {
        return get(p, xOffset, yOffset, null);
    }

    public T get(Point p, Vector v, T defaultValue) {
        return get(p, v.x, v.y, defaultValue);
    }

    public T getOrBlank(Point p) {
        return get(p, (T) (Character) ' ');
    }

    public T getOrBlank(Point p, int xOffset, int yOffset) {
        return get(p, xOffset, yOffset, (T) (Character) ' ');
    }

    public T getOrBlank(Point p, Vector v) {
        return get(p, v.x, v.y, (T) (Character) ' ');
    }

    public boolean check(Point p, T value) {
        T v = get(p, null);
        return v != null && v.equals(value);
    }

    public int forEach(BiFunction<Point, T, Integer> fn) {
        int result = 0;
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                Point p = new Point(n, m);
                T value = buffer[m][n];
                result += fn.apply(p, value);
            }
        }
        return result;
    }

    public int forEach(Vector direction, BiFunction<Point, T, Integer> fn) {
        int m0, m1, dm, n0, n1, dn;
        if (direction == Vector.SOUTH) {
            // start from up to bottom (remember matrix coords are upside-down)
            dm = 1;
            m0 = 0;
            m1 = M;
            dn = 1;
            n0 = 0;
            n1 = N;
        } else if (direction == Vector.EAST) {
            // start right to left
            dm = 1;
            m0 = 0;
            m1 = M;
            dn = -1;
            n0 = N - 1;
            n1 = -1;
        } else if (direction == Vector.NORTH) {
            // start from bottom to up (remember matrix coords are upside-down)
            dm = -1;
            m0 = M - 1;
            m1 = -1;
            dn = 1;
            n0 = 0;
            n1 = N;
        } else if (direction == Vector.WEST) {
            // start from left to right
            dm = 1;
            m0 = 0;
            m1 = M;
            dn = 1;
            n0 = 0;
            n1 = N;
        } else {
            throw new IllegalArgumentException("Supports only AoCVector NORTH, EAST, SOUTH, WEST");
        }
        int result = 0;
        for (int m = m0; m != m1; m += dm) {
            for (int n = n0; n != n1; n += dn) {
                Point p = new Point(n, m);
                T value = buffer[m][n];
                result += fn.apply(p, value);
            }
        }
        return result;
    }

    public Point searchFor(T value) {
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                if (value.equals(buffer[m][n])) {
                    Point p = new Point(n, m);
                    return p;
                }
            }
        }
        return null;
    }

    public Iterable<Cell> cells() {
        return () -> new Iterator<>() {
            int m = 0;
            int n = 0;
            Cell nextCell;

            @Override
            public boolean hasNext() {
                if (nextCell == null && m < M) {
                    nextCell = new Cell(n, m, buffer[m][n]);
                    n++;
                    if (n >= N) {
                        m++;
                        n = 0;
                    }
                }
                return nextCell != null;
            }

            @Override
            public Cell next() {
                if (nextCell == null) hasNext();
                if (nextCell == null) throw new NoSuchElementException();
                Cell cell = nextCell;
                nextCell = null;
                return cell;
            }
        };
    }

    public List<Cell> listAll(T value) {
        List<Cell> list = new ArrayList<>();
        searchAll(value).forEach(c -> list.add(c));
        return list;
    }

    public Iterable<Cell> searchAll(T value) {
        return () -> new Iterator<>() {
            int m = 0;
            int n = 0;
            Cell nextCell;

            @Override
            public boolean hasNext() {
                while (nextCell == null && m < M) {
                    Cell cell = new Cell(n, m, buffer[m][n]);
                    T c = buffer[m][n++];
                    if (n >= N) {
                        m++;
                        n = 0;
                    }
                    if (value.equals(c)) nextCell = cell;
                }
                return nextCell != null;
            }

            @Override
            public Cell next() {
                if (nextCell == null) hasNext();
                if (nextCell == null) throw new NoSuchElementException();
                Cell cell = nextCell;
                nextCell = null;
                return cell;
            }
        };
    }

    public Set<Point> pointSet() {
        Set<Point> set = new HashSet<>(N * M);
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                set.add(Point.of(n, m));
            }
        }
        return set;
    }

    public Set<Cell> cellSet() {
        Set<Cell> set = new HashSet<>(N * M);
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                set.add(new Cell(n, m, buffer[m][n]));
            }
        }
        return set;
    }

    public Iterable<Dimension<T>> rows() {
        return () -> new Iterator<>() {
            int m = 0;
            Row next;

            @Override
            public boolean hasNext() {
                if (next == null && m < M) {
                    next = new Row(m++);
                }
                return next != null;
            }

            @Override
            public Row next() {
                if (next == null) hasNext();
                if (next == null) throw new NoSuchElementException();
                Row row = next;
                next = null;
                return row;
            }
        };
    }

    public Iterable<Dimension<T>> cols() {
        return () -> new Iterator<>() {
            int n = 0;
            Col next;

            @Override
            public boolean hasNext() {
                if (next == null && n < N) {
                    next = new Col(n++);
                }
                return next != null;
            }

            @Override
            public Col next() {
                if (next == null) hasNext();
                if (next == null) throw new NoSuchElementException();
                Col col = next;
                next = null;
                return col;
            }
        };
    }

    public List<Point> neighbors(Point p0, int distance, Predicate<T> filter) {
        List<Point> points = new ArrayList<>(distance * distance);
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

    public void set(Point p, T value) {
        set(p.x, p.y, value);
    }

    public void swap(Point from, Point to) {
        T tmp = get(to);
        set(to, get(from));
        set(from, tmp);
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

    public void dumpBoard(String cellFormat, Function<Cell, ?> transformer) {
        dumpBoard("", cellFormat, transformer);
    }

    public void dumpBoard(String title, String cellFormat, Function<Cell, ?> transformer) {
        System.out.println();
        System.out.println("--- " + title);
        for (int m = 0; m < M; ++m) {
            for (int n = 0; n < N; ++n) {
                Object v = buffer[m][n];
                if (transformer != null) {
                    v = transformer.apply(new Cell(n, m, (T) v));
                }
                System.out.printf(cellFormat, v);
            }
            System.out.println();
        }
        System.out.println("--- " + title);
        System.out.println();
    }

    public static <T> void dumpBoard(String title, Map<Point, T> points, BiFunction<Point, T, String> transformer) {
        int M0 = Integer.MAX_VALUE, M1 = Integer.MIN_VALUE;
        int N0 = Integer.MAX_VALUE, N1 = Integer.MIN_VALUE;
        for (Point p : points.keySet()) {
            if (p.y < M0) M0 = p.y;
            if (p.y > M1) M1 = p.y;
            if (p.x < N0) N0 = p.x;
            if (p.x > N1) N1 = p.x;
        }
        dumpBoard(title, N0, M0, N1 + 1, M1 + 1, points::get, transformer);
    }

    public static <T> void dumpBoard(String title, Rect rect, Map<Point, T> points, BiFunction<Point, T, String> transformer) {
        dumpBoard(title, rect.p1.x, rect.p1.y, rect.p2.x + 1, rect.p2.y + 1, points::get, transformer);
    }

    public static <T> void dumpBoard(String title, int N, int M, Map<Point, T> points, BiFunction<Point, T, String> transformer) {
        dumpBoard(title, 0, 0, N, M, points::get, transformer);
    }

    public static void dumpBoard(String title, int N, int M, Set<Point> points) {
        dumpBoard(title, N, M, points, null);
    }

    public static void dumpBoard(String title, int N, int M, Set<Point> points, BiFunction<Point, Character, String> transformer) {
        dumpBoard(title, 0, 0, N, M, p -> points.contains(p) ? 'X' : null, transformer);
    }

    private static <T> void dumpBoard(String title, int N0, int M0, int N1, int M1, Function<Point, T> points, BiFunction<Point, T, String> transformer) {
        System.out.println();
        System.out.println("--- %s".formatted(title));
        for (int m = M0; m < M1; ++m) {
            for (int n = N0; n < N1; ++n) {
                Point p = new Point(n, m);
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

        public final Point p;
        public final int n;
        public final int m;
        public final T v;
        public final T defaultValue;

        public Cell(int n, int m, T v) {
            this(n, m, v, null);
        }

        public Cell(int n, int m, T v, T defaultValue) {
            this.p = Point.of(n, m);
            this.n = n;
            this.m = m;
            this.v = v;
            this.defaultValue = defaultValue;
        }

        @Override
        public String toString() {
            return "B[" + n + "," + m + "]:" + v;
        }


        public char getChar(Vector dir) {
            return (char) Board.this.get(p, dir, defaultValue);
        }

        public int getInt(Vector dir) {
            return (int) Board.this.get(p, dir, defaultValue);
        }

        public Point p() {
            return p;
        }

        public int n() {
            return n;
        }

        public int m() {
            return m;
        }

        public T v() {
            return v;
        }

        public T defaultValue() {
            return defaultValue;
        }
    }

    /**
     *
     */
    public interface Dimension<T> extends Iterable<Board<T>.Cell> {

        int forEach(BiFunction<Point, T, Integer> fn);
    }

    /**
     *
     */
    public class Row implements Dimension<T> {

        public final int m;

        private Row(int m) {
            this.m = m;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(N * 2);
            for (int n = 0; n < N; ++n) {
                if (n > 0) sb.append(",");
                sb.append(buffer[m][n]);
            }
            return sb.toString();
        }

        public int m() {
            return m;
        }

        public int forEach(BiFunction<Point, T, Integer> fn) {
            int result = 0;
            for (int n = 0; n < N; ++n) {
                Point p = new Point(n, m);
                T value = buffer[m][n];
                result += fn.apply(p, value);
            }
            return result;
        }

        @Override
        public Iterator<Cell> iterator() {
            return new Iterator<>() {
                int n = 0;
                Cell next;

                @Override
                public boolean hasNext() {
                    if (next == null && n < N) {
                        next = new Cell(n, m, buffer[m][n]);
                        n++;
                    }
                    return next != null;
                }

                @Override
                public Cell next() {
                    if (next == null) hasNext();
                    if (next == null) throw new NoSuchElementException();
                    Cell cell = next;
                    next = null;
                    return cell;
                }
            };
        }
    }

    /**
     *
     */
    public class Col implements Dimension<T> {

        public final int n;

        private Col(int n) {
            this.n = n;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(N * 2);
            for (int m = 0; m < M; ++m) {
                if (m > 0) sb.append(",");
                sb.append(buffer[m][n]);
            }
            return sb.toString();
        }

        public int n() {
            return n;
        }

        public int forEach(BiFunction<Point, T, Integer> fn) {
            int result = 0;
            for (int m = 0; m < M; ++m) {
                Point p = new Point(n, m);
                T value = buffer[m][n];
                result += fn.apply(p, value);
            }
            return result;
        }

        @Override
        public Iterator<Cell> iterator() {
            return new Iterator<>() {
                int m = 0;
                Cell next;

                @Override
                public boolean hasNext() {
                    if (next == null && m < M) {
                        next = new Cell(n, m, buffer[m][n]);
                        m++;
                    }
                    return next != null;
                }

                @Override
                public Cell next() {
                    if (next == null) hasNext();
                    if (next == null) throw new NoSuchElementException();
                    Cell cell = next;
                    next = null;
                    return cell;
                }
            };
        }
    }
}
