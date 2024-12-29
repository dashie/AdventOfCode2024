package adventofcode.commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 *
 */
public class AoCRectTest {

    @Test
    public void sortVerticesTest() {
        Rect r1 = Rect.of(9, 6, 7, 8);
        Rect r2 = Rect.of(7, 8, 9, 6);
        assertNotEquals(r1, r2);
        assertEquals(r1.sortVertices(), r2.sortVertices());
    }
}
