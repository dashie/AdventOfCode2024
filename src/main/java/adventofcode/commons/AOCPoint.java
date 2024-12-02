package adventofcode.commons;

import java.util.Objects;

public class AOCPoint {

    public int x;
    public int y;
    public int z;

    public AOCPoint() {
    }

    public AOCPoint(int x, int y) {
        this(x, y, 0);
    }

    public AOCPoint(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public AOCPoint clone() {
        return new AOCPoint(x, y, z);
    }

    public AOCVector distanceVector(AOCPoint p) {
        return new AOCVector(x - p.x, y - p.y, z - p.z);
    }

    public AOCVector distanceVector(int x1, int y1) {
        return new AOCVector(x - x1, y - y1, z - 0);
    }

    public AOCPoint traslate(AOCVector v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public AOCPoint traslate(int dx, int dy) {
        x += dx;
        y += dy;
        return this;
    }

    public AOCPoint traslateNew(AOCVector v) {
        return new AOCPoint(x + v.x, y + v.y);
    }

    public AOCPoint traslateNew(int dx, int dy) {
        return new AOCPoint(x + dx, y + dy);
    }

    public AOCPoint moduleNew(int modX, int modY) {
        int x1 = x % modX;
        if (x1 < 0)
            x1 += modX;
        int y1 = y % modY;
        if (y1 < 0)
            y1 += modY;
        return new AOCPoint(x1, y1);
    }

    public AOCPoint module(int modX, int modY) {
        int x1 = x % modX;
        if (x1 < 0)
            x1 += modX;
        int y1 = y % modY;
        if (y1 < 0)
            y1 += modY;
        x = x1;
        y = y1;
        return this;
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AOCPoint aocPoint = (AOCPoint) o;
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

    public static AOCPoint valueOf(String x, String y) {
        return valueOf(x, y, null);
    }

    public static AOCPoint valueOf(String x, String y, String z) {
        return new AOCPoint(
                parseInt(x),
                parseInt(y),
                parseInt(z));
    }

    public static AOCPoint[] newArray(int size, int x, int y, int z) {
        AOCPoint[] a = new AOCPoint[size];
        for (int i = 0; i < a.length; ++i) {
            a[i] = new AOCPoint(x, y, z);
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
