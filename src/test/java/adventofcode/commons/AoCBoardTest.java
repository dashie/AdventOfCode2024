package adventofcode.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class AoCBoardTest {

    @Test
    public void iterateCellsTest() {
        Board<Character> board = Board.from("""
            ....
            .##.
            ....
            .#..""");

        int totalCount = 0;
        int dotCount = 0;
        int hashCount = 0;
        for (var cell : board.cells()) {
            totalCount++;
            if (cell.v == '#') hashCount++;
            if (cell.v == '.') dotCount++;
        }
        Assertions.assertEquals(16, totalCount);
        Assertions.assertEquals(13, dotCount);
        Assertions.assertEquals(3, hashCount);
    }

    @Test
    public void searchAllTest() {
        Board<Character> board = Board.from("""
            #...
            .##.
            ....
            .#..""");

        int totalCount = 0;
        int dotCount = 0;
        Set<Point> hashSet = new HashSet<>();
        for (var cell : board.searchAll('#')) {
            totalCount++;
            if (cell.v == '.') dotCount++;
            if (cell.v == '#') hashSet.add(cell.p);
        }
        Assertions.assertEquals(4, totalCount);
        Assertions.assertEquals(0, dotCount);
        Assertions.assertEquals(4, hashSet.size());
        Assertions.assertTrue(hashSet.remove(Point.of(0, 0)));
        Assertions.assertTrue(hashSet.remove(Point.of(1, 1)));
        Assertions.assertTrue(hashSet.remove(Point.of(2, 1)));
        Assertions.assertTrue(hashSet.remove(Point.of(1, 3)));
        Assertions.assertTrue(hashSet.isEmpty());
    }
}
