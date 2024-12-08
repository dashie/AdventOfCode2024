package adventofcode.commons;

import java.util.List;
import java.util.Objects;

public class AoCVector {

    public static final AoCVector NORTH = new AoCVector(0, 1);
    public static final AoCVector EAST = new AoCVector(1, 0);
    public static final AoCVector SOUTH = new AoCVector(0, -1);
    public static final AoCVector WEST = new AoCVector(-1, 0);

    public static final AoCVector NE = new AoCVector(1, 1);
    public static final AoCVector NW = new AoCVector(-1, 1);
    public static final AoCVector SE = new AoCVector(1, -1);
    public static final AoCVector SW = new AoCVector(-1, -1);

    public static final List<AoCVector> DIRECTIONS = List.of(NORTH, EAST, SOUTH, WEST);
    public static final List<AoCVector> DIRECTIONS_EXT = List.of(NORTH, NE, EAST, SE, SOUTH, SW, WEST, NW);

    public final int x;
    public final int y;
    public final int z;

    public AoCVector(int x, int y) {
        this(x, y, 0);
    }

    public AoCVector(int x, int y, int z) {
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
        AoCVector intPoint = (AoCVector) o;
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

    public static AoCVector valueOf(String x, String y) {
        return valueOf(x, y, null);
    }

    public static AoCVector valueOf(String x, String y, String z) {
        return new AoCVector(parseInt(x), parseInt(y), parseInt(z));
    }

    public int manhattam() {
        return Math.abs(x) + Math.abs(y);
    }

    public AoCVector absolute() {
        return new AoCVector(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public AoCVector rotate90R() {
        return new AoCVector(y, -x, z);
    }

    public AoCVector rotate180() {
        return new AoCVector(-x, -y, z);
    }

    public AoCVector rotate90L() {
        return new AoCVector(-y, x, z);
    }

    public AoCVector mul(int n) {
        return new AoCVector(x * n, y * n, z * n);
    }

    public AoCVector signs() {
        return new AoCVector(
            x > 0 ? 1 : x < 0 ? -1 : 0,
            y > 0 ? 1 : y < 0 ? -1 : 0,
            z > 0 ? 1 : z < 0 ? -1 : 0);
    }

    public static AoCVector[] newArray(int size, int x, int y, int z) {
        AoCVector[] a = new AoCVector[size];
        for (int i = 0; i < a.length; ++i) {
            a[i] = new AoCVector(x, y, z);
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
