package adventofcode.commons;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class DirectedPoint {

    public static DirectedPoint of(Point p, Vector d) {
        return new DirectedPoint(p, d);
    }

    public enum Direction {
        FRONT,
        BACK,
        RIGHT,
        LEFT
    }

    public final Point p;

    public final Vector d;

    public DirectedPoint(Point p, Vector d) {
        this.p = p;
        this.d = d;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DirectedPoint that = (DirectedPoint) o;
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

    public DirectedPoint moveFront() {
        return new DirectedPoint(p.translate(d), d);
    }

    public DirectedPoint moveBack() {
        return new DirectedPoint(p.translate(d.rotate180()), d.rotate180());
    }

    public DirectedPoint moveLeft() {
        return new DirectedPoint(p.translate(d.rotate90L()), d.rotate90L());
    }

    public DirectedPoint moveRight() {
        return new DirectedPoint(p.translate(d.rotate90R()), d.rotate90R());
    }

    public DirectedPoint move(Direction d) {
        return switch (d) {
            case FRONT -> this.moveFront();
            case LEFT -> this.moveLeft();
            case RIGHT -> this.moveRight();
            case BACK -> this.moveBack();
        };
    }

    public Collection<DirectedPoint> move(Direction... directions) {
        return Arrays.stream(directions)
                     .map(this::move)
                     .toList();
    }
}
