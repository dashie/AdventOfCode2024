package adventofcode.commons;

import java.util.Objects;

/**
 *
 */
public final class AoCRect {

    public static final AoCRect of(int x2, int y2) {
        return of(AoCPoint.of(x2, y2));
    }

    public static final AoCRect of(int x1, int y1, int x2, int y2) {
        return new AoCRect(AoCPoint.of(x1, y1), AoCPoint.of(x2, y2));
    }

    public static final AoCRect of(AoCPoint p2) {
        return of(AoCPoint.of(0, 0), p2);
    }

    public static final AoCRect of(AoCPoint p1, AoCPoint p2) {
        return new AoCRect(p1, p2);
    }

    public final AoCPoint p1;
    public final AoCPoint p2;

    public AoCRect(AoCPoint p1, AoCPoint p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public String toString() {
        return "R{" + p1.x + "," + p1.y + "," + p2.x + "," + p2.y + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AoCRect rect = (AoCRect) o;
        return Objects.equals(p1, rect.p1) && Objects.equals(p2, rect.p2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p1, p2);
    }

    public boolean contains(AoCPoint p) {
        if (p.x < p1.x || p.x > p2.x || p.y < p1.y || p.y > p2.y) return false;
        return true;
    }

    public AoCRect expand(int offset) {
        return AoCRect.of(p1.x - offset, p1.y - offset, p2.x + offset, p2.y + offset);
    }
}
