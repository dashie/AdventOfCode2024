package adventofcode.commons;

import java.util.Objects;

public class AOCVector {

    public static final AOCVector NORTH = new AOCVector(0, 1);
    public static final AOCVector EAST = new AOCVector(1, 0);
    public static final AOCVector SOUTH = new AOCVector(0, -1);
    public static final AOCVector WEST = new AOCVector(-1, 0);

    public final int x;
    public final int y;
    public final int z;

    public AOCVector(int x, int y) {
        this(x, y, 0);
    }

    public AOCVector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int manhattam() {
        return Math.abs(x) + Math.abs(y);
    }

    public AOCVector absolute() {
        return new AOCVector(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public AOCVector rotate90R() {
        return new AOCVector(y, -x, z);
    }

    public AOCVector rotate180() {
        return new AOCVector(-x, -y, z);
    }

    public AOCVector rotate90L() {
        return new AOCVector(-y, x, z);
    }

    public AOCVector signs() {
        return new AOCVector(
            x > 0 ? 1 : x < 0 ? -1 : 0,
            y > 0 ? 1 : y < 0 ? -1 : 0,
            z > 0 ? 1 : z < 0 ? -1 : 0);
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AOCVector intPoint = (AOCVector) o;
        return x == intPoint.x && y == intPoint.y && z == intPoint.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "V{" + x + "," + y + "," + z + "}";
    }

    public static AOCVector valueOf(String x, String y) {
        return valueOf(x, y, null);
    }

    public static AOCVector valueOf(String x, String y, String z) {
        return new AOCVector(parseInt(x), parseInt(y), parseInt(z));
    }

    public static AOCVector[] newArray(int size, int x, int y, int z) {
        AOCVector[] a = new AOCVector[size];
        for (int i = 0; i < a.length; ++i) {
            a[i] = new AOCVector(x, y, z);
        }
        return a;
    }

    private static int parseInt(String s) {
        if (s == null
            || s.trim()
                .isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(s);
        }
    }
}
