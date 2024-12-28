package adventofcode.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public final class AoCPoint implements Comparable<AoCPoint> {

    private static final Pattern POINT_PATTERN = Pattern.compile("([+-]?[0-9]+)[, ]([+-]?[0-9]+)");

    public static AoCPoint parsePoint(String str) {
        Matcher m = POINT_PATTERN.matcher(str);
        if (!m.find()) throw new IllegalArgumentException(str);
        return of(m.group(1), m.group(2), null);
    }

    public static AoCPoint of(int x, int y) {
        return new AoCPoint(x, y);
    }

    public static AoCPoint of(String x, String y) {
        return of(x, y, null);
    }

    public static AoCPoint of(String x, String y, String z) {
        return new AoCPoint(
            parseInt(x),
            parseInt(y),
            parseInt(z));
    }

    public final int x;
    public final int y;
    public final int z;

    public AoCPoint(int x, int y) {
        this(x, y, 0);
    }

    public AoCPoint(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AoCPoint aocPoint = (AoCPoint) o;
        return x == aocPoint.x && y == aocPoint.y && z == aocPoint.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public int compareTo(AoCPoint o) {
        // distance from O
        var d1 = distance(0, 0);
        var d2 = o.distance(0, 0);
        var cmp = d1.compareTo(d2);
        if (cmp == 0) {
            cmp = Integer.compare(y, o.y);
            if (cmp == 0) {
                cmp = Integer.compare(x, o.x);
            }
        }
        return cmp;
    }

    public int[] toArray() {
        return new int[]{x, y, z};
    }

    @Override
    public String toString() {
        return "P{" + x + "," + y + "," + z + "}";
    }

    /**
     * Relative distance from p
     */
    public AoCVector distance(AoCPoint p) {
        return new AoCVector(x - p.x, y - p.y, z - p.z);
    }

    /**
     * Relative distance from x1, y1
     */
    public AoCVector distance(int x1, int y1) {
        return new AoCVector(x - x1, y - y1, z - 0);
    }

    public AoCPoint translate(AoCVector v) {
        return new AoCPoint(x + v.x, y + v.y);
    }

    public AoCPoint translate(int dx, int dy) {
        return new AoCPoint(x + dx, y + dy);
    }

    public boolean isIn(AoCRect r) {
        return r.contains(this);
    }

    public boolean isOut(AoCRect r) {
        return !r.contains(this);
    }

    public AoCPoint north() {
        return this.translate(AoCVector.NORTH);
    }

    public AoCPoint south() {
        return this.translate(AoCVector.SOUTH);
    }

    public AoCPoint east() {
        return this.translate(AoCVector.EAST);
    }

    public AoCPoint west() {
        return this.translate(AoCVector.WEST);
    }

    public List<AoCPoint> neighbors() {
        return Arrays.asList(north(), east(), south(), west());
    }

    public List<AoCPoint> neighbors(int distance) {
        List<AoCPoint> points = new ArrayList<>(distance * distance);
        for (int dy = -distance; dy <= distance; ++dy) {
            int dx0 = distance - Math.abs(dy);
            for (int dx = -dx0; dx <= dx0; ++dx) {
                if (dy != 0 || dx != 0) points.add(AoCPoint.of(x + dx, y + dy));
            }
        }
        return points;
    }

    public AoCPoint module(int modX, int modY) {
        int x1 = x % modX;
        if (x1 < 0)
            x1 += modX;
        int y1 = y % modY;
        if (y1 < 0)
            y1 += modY;
        return new AoCPoint(x1, y1);
    }

    /**
     * Follow all points from p0 to pEnd (p0 + direction)
     * using the Bresenham algorithm.
     */
    public List<AoCPoint> follow(AoCVector d) {
        List<AoCPoint> points = new ArrayList<>();
        AoCPoint end = this.translate(d);
        AoCVector distance = end.distance(this).absolute();

        int dx = distance.x;
        int dy = distance.y;
        int sx = x < end.x ? 1 : -1; // x direction
        int sy = y < end.y ? 1 : -1; // y direction
        int err = dx - dy; // initial error

        AoCPoint p = this;
        while (true) {
            int x1 = p.x;
            int y1 = p.y;
            if (x1 == end.x && y1 == end.y) break; // end point

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
            p = new AoCPoint(x1, y1);
            points.add(p); // add current point
        }
        return points;
    }

    public static AoCPoint[] newArray(int size, int x, int y, int z) {
        AoCPoint[] a = new AoCPoint[size];
        for (int i = 0; i < a.length; ++i) {
            a[i] = new AoCPoint(x, y, z);
        }
        return a;
    }

    private static int parseInt(String s) {
        if (s == null || s.trim().isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(s);
        }
    }
}
