package adventofcode.commons;

import java.util.List;
import java.util.Objects;

/**
 *
 */
public final class Vector implements Comparable<Vector> {

    public static Vector of(int x, int y) {
        return new Vector(x, y);
    }

    public static Vector of(String x, String y) {
        return of(x, y, null);
    }

    public static Vector of(String x, String y, String z) {
        return new Vector(parseInt(x), parseInt(y), parseInt(z));
    }

    public static final Vector NORTH = new Vector(0, 1);
    public static final Vector EAST = new Vector(1, 0);
    public static final Vector SOUTH = new Vector(0, -1);
    public static final Vector WEST = new Vector(-1, 0);

    public static final Vector NE = new Vector(1, 1);
    public static final Vector NW = new Vector(-1, 1);
    public static final Vector SE = new Vector(1, -1);
    public static final Vector SW = new Vector(-1, -1);

    public static final List<Vector> DIRECTIONS = List.of(NORTH, EAST, SOUTH, WEST);
    public static final List<Vector> DIRECTIONS_EXT = List.of(NORTH, NE, EAST, SE, SOUTH, SW, WEST, NW);

    public final int x;
    public final int y;
    public final int z;

    public Vector(int x, int y) {
        this(x, y, 0);
    }

    public Vector(int x, int y, int z) {
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
        Vector intPoint = (Vector) o;
        return x == intPoint.x && y == intPoint.y && z == intPoint.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public int compareTo(Vector o) {
        var md1 = manhattam();
        var md2 = o.manhattam();
        return Integer.compare(md1, md2);
    }

    @Override
    public String toString() {
        return "V{" + x + "," + y + "," + z + "}";
    }

    public int manhattam() {
        return Math.abs(x) + Math.abs(y);
    }

    public Vector absolute() {
        return new Vector(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public Vector rotate90R() {
        return new Vector(y, -x, z);
    }

    public Vector rotate180() {
        return new Vector(-x, -y, z);
    }

    public Vector rotate90L() {
        return new Vector(-y, x, z);
    }

    public Vector mul(int n) {
        return new Vector(x * n, y * n, z * n);
    }

    public Vector extend(int n) {
        return new Vector(
            x > 0 ? x + n : x < 0 ? x - n : 0,
            y > 0 ? y + n : y < 0 ? y - n : 0,
            z > 0 ? z + n : z < 0 ? z - n : 0);
    }

    public Vector signs() {
        return new Vector(
            x > 0 ? 1 : x < 0 ? -1 : 0,
            y > 0 ? 1 : y < 0 ? -1 : 0,
            z > 0 ? 1 : z < 0 ? -1 : 0);
    }

    public boolean isNorth() {
        return this.equals(NORTH);
    }

    public boolean isSouth() {
        return this.equals(SOUTH);
    }

    public boolean isEast() {
        return this.equals(EAST);
    }

    public boolean isWest() {
        return this.equals(WEST);
    }

    public boolean is(Vector... dirs) {
        for (var d : dirs) {
            if (d.equals(this)) return true;
        }
        return false;
    }

    public static Vector[] newArray(int size, int x, int y, int z) {
        Vector[] a = new Vector[size];
        for (int i = 0; i < a.length; ++i) {
            a[i] = new Vector(x, y, z);
        }
        return a;
    }

    /**
     * ^, v, >, < to direction
     * North and South are inverted in a matrix.
     */
    public static Vector charArrowToMatrixDirection(int c) {
        return switch (c) {
            case 'v' -> Vector.NORTH;
            case '^' -> Vector.SOUTH;
            case '>' -> Vector.EAST;
            case '<' -> Vector.WEST;
            default -> throw new IllegalStateException();
        };
    }

    /**
     * U, D, R, L to direction
     */
    public static Vector charUDRLToDirection(int c) {
        return switch (Character.toUpperCase(c)) {
            case 'U' -> Vector.NORTH;
            case 'D' -> Vector.SOUTH;
            case 'R' -> Vector.EAST;
            case 'L' -> Vector.WEST;
            default -> throw new IllegalStateException();
        };
    }

    /**
     * U, D, R, L to direction
     */
    public static Vector charUDRLToDirection(String c) {
        return charUDRLToDirection(c.charAt(0));
    }

    private static int parseInt(String s) {
        if (s == null
            || s.trim().isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(s);
        }
    }
}
