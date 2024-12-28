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
        AoCRect r1 = AoCRect.of(9, 6, 7, 8);
        AoCRect r2 = AoCRect.of(7, 8, 9, 6);
        assertNotEquals(r1, r2);
        assertEquals(r1.sortVertices(), r2.sortVertices());
    }
}
