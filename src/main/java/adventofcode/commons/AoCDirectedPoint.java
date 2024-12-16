package adventofcode.commons;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class AoCDirectedPoint {

    public static AoCDirectedPoint of(AoCPoint p, AoCVector d) {
        return new AoCDirectedPoint(p, d);
    }

    public enum Direction {
        FRONT,
        BACK,
        RIGHT,
        LEFT
    }

    public final AoCPoint p;

    public final AoCVector d;

    public AoCDirectedPoint(AoCPoint p, AoCVector d) {
        this.p = p;
        this.d = d;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AoCDirectedPoint that = (AoCDirectedPoint) o;
        return Objects.equals(p, that.p) && Objects.equals(d, that.d);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p, d);
    }

    @Override
    public String toString() {
        return "P{" + p.x + "," + p.y + "," + p.z + "}->{" + d.x + "," + d.y + "," + d.z + "}";
    }

    public AoCDirectedPoint moveFront() {
        return new AoCDirectedPoint(p.translate(d), d);
    }

    public AoCDirectedPoint moveBack() {
        return new AoCDirectedPoint(p.translate(d.rotate180()), d.rotate180());
    }

    public AoCDirectedPoint moveLeft() {
        return new AoCDirectedPoint(p.translate(d.rotate90L()), d.rotate90L());
    }

    public AoCDirectedPoint moveRight() {
        return new AoCDirectedPoint(p.translate(d.rotate90R()), d.rotate90R());
    }

    public AoCDirectedPoint move(Direction d) {
        return switch (d) {
            case FRONT -> this.moveFront();
            case LEFT -> this.moveLeft();
            case RIGHT -> this.moveRight();
            case BACK -> this.moveBack();
        };
    }

    public Collection<AoCDirectedPoint> move(Direction... directions) {
        return Arrays.stream(directions)
                     .map(this::move)
                     .toList();
    }
}
