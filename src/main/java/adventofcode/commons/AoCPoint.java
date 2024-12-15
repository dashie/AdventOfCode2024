package adventofcode.commons;

import java.util.Objects;

public class AoCPoint {

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

    public int[] toArray() {
        return new int[]{x, y, z};
    }

    @Override
    public String toString() {
        return "P{" + x + "," + y + "," + z + "}";
    }

    public static AoCPoint valueOf(String x, String y) {
        return valueOf(x, y, null);
    }

    public static AoCPoint valueOf(String x, String y, String z) {
        return new AoCPoint(
            parseInt(x),
            parseInt(y),
            parseInt(z));
    }

    public AoCVector distance(AoCPoint p) {
        return new AoCVector(x - p.x, y - p.y, z - p.z);
    }

    public AoCVector distance(int x1, int y1) {
        return new AoCVector(x - x1, y - y1, z - 0);
    }

    public AoCPoint translate(AoCVector v) {
        return new AoCPoint(x + v.x, y + v.y);
    }

    public AoCPoint translate(int dx, int dy) {
        return new AoCPoint(x + dx, y + dy);
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

    public AoCPoint module(int modX, int modY) {
        int x1 = x % modX;
        if (x1 < 0)
            x1 += modX;
        int y1 = y % modY;
        if (y1 < 0)
            y1 += modY;
        return new AoCPoint(x1, y1);
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
