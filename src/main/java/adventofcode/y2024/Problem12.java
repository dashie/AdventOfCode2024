package adventofcode.y2024;

import adventofcode.commons.*;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Function;

import static adventofcode.commons.AoCVector.*;

/**
 * Day 12: Garden Groups
 * https://adventofcode.com/2024/day/12
 */
public class Problem12 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem12().solve(false);
    }

    AoCBoard<Character> board;

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toCharBoard();
    }

    class Region {

        int area;
        int perimeter;
        int sides;
    }

    /**
     * ...Due to "modern" business practices, the price of fence required for a
     * region is found by multiplying that region's area by its perimeter.
     * The total price of fencing all regions on a map is found by adding
     * together the price of fence for every region on the map...
     * ...What is the total price of fencing all regions on your map?
     */
    @Override
    public Long partOne() throws Exception {
        return evalGarden(r -> r.perimeter * r.area);
    }

    public long evalGarden(Function<Region, Integer> fn) {
        long result = 0L;
        Set<AoCPoint> visited = new HashSet<>();
        result += board.forEach((p, v) -> {
            if (visited.contains(p)) return 0;
            Region region = evalRegion(p, visited);
            return fn.apply(region);
        });
        return result;
    }

    public Region evalRegion(AoCPoint p0, Set<AoCPoint> visited) {
        char id = board.get(p0);
        Region region = new Region();
        Deque<AoCPoint> stack = new LinkedList<>();
        stack.add(p0);
        while (!stack.isEmpty()) {
            AoCPoint p = stack.pop();
            if (visited.contains(p)) continue;
            visited.add(p);
            region.area += 1;
            // eval perimeter
            AoCBoard.Cell c = board.cell(p, ' ');
            for (AoCVector d : DIRECTIONS) {
                if (c.getChar(d) == id)
                    stack.add(p.translate(d));
                else
                    region.perimeter += 1;
            }
            // open angles (for sides evaluation)
            if (c.getChar(NORTH) != id && c.getChar(EAST) != id) region.sides += 1;
            if (c.getChar(NORTH) != id && c.getChar(WEST) != id) region.sides += 1;
            if (c.getChar(SOUTH) != id && c.getChar(EAST) != id) region.sides += 1;
            if (c.getChar(SOUTH) != id && c.getChar(WEST) != id) region.sides += 1;
            // closed angles (for sides evaluation)
            if (c.getChar(NORTH) == id && c.getChar(EAST) == id && c.getChar(NE) != id) region.sides += 1;
            if (c.getChar(NORTH) == id && c.getChar(WEST) == id && c.getChar(NW) != id) region.sides += 1;
            if (c.getChar(SOUTH) == id && c.getChar(EAST) == id && c.getChar(SE) != id) region.sides += 1;
            if (c.getChar(SOUTH) == id && c.getChar(WEST) == id && c.getChar(SW) != id) region.sides += 1;
        }
        return region;
    }

    /**
     * ...Under the bulk discount, instead of using the perimeter to calculate
     * the price, you need to use the number of sides each region has...
     * ...What is the new total price of fencing all regions on your map?
     */
    @Override
    public Long partTwo() throws Exception {
        return evalGarden(r -> r.sides * r.area);
    }
}
