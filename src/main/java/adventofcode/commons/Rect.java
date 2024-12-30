package adventofcode.commons;

import java.util.Objects;

/**
 *
 */
public final class Rect {

    public static final Rect of(int x2, int y2) {
        return of(Point.of(x2, y2));
    }

    public static final Rect of(int x1, int y1, int x2, int y2) {
        return new Rect(Point.of(x1, y1), Point.of(x2, y2));
    }

    public static final Rect of(Point p2) {
        return of(Point.of(0, 0), p2);
    }

    public static final Rect of(Point p1, Point p2) {
        return new Rect(p1, p2);
    }

    public final Point p1;
    public final Point p2;

    public Rect(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point p1() {
        return p1;
    }

    public Point p2() {
        return p2;
    }

    @Override
    public String toString() {
        return "R{" + p1.x + "," + p1.y + ":" + p2.x + "," + p2.y + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Rect rect = (Rect) o;
        return Objects.equals(p1, rect.p1) && Objects.equals(p2, rect.p2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p1, p2);
    }

    public boolean contains(Point p) {
        if (p.x < p1.x || p.x > p2.x || p.y < p1.y || p.y > p2.y) return false;
        return true;
    }

    public Rect expand(int offset) {
        return Rect.of(p1.x - offset, p1.y - offset, p2.x + offset, p2.y + offset);
    }

    public Rect sortVertices() {
        if (p1.compareTo(p2) > 0)
            return new Rect(p2, p1);
        return this;
    }
}
