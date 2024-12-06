package adventofcode.y2024;

import adventofcode.commons.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * Day 6: Guard Gallivant
 * https://adventofcode.com/2024/day/6
 */
public class Problem06 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem06().solve(false);
    }

    private AoCBoard<Character> board;
    private Map<String, Set<String>> visited = new HashMap<>(); // visit flag with directions

    @Override
    public void processInput(AoCInput input) throws Exception {
        this.board = input.toCharBoard();
    }

    /**
     * ...Predict the path of the guard. How many distinct positions will the guard
     * visit before leaving the mapped area?
     */
    @Override
    protected Long partOne() throws Exception {
        AoCVector d = AoCVector.SOUTH;
        AoCPoint p = board.searchFor('^');
        return countCells(p, d);
    }

    private long countCells(AoCPoint p, AoCVector d) {
        visited.clear();
        updateVisited(p, d);
        for (; ; ) {
            p = moveStraight(p, d);
            if (board.getSafe(p.traslateNew(d)) == null) {
                break;
            }
            d = d.rotate90L();
        }
        return (long) visited.size();
    }

    private AoCPoint moveStraight(AoCPoint p, AoCVector d) {
        AoCPoint p1 = p.traslateNew(d);
        while (board.getSafe(p1) != null && board.getSafe(p1) != '#') {
            p = p1;
            updateVisited(p, d);
            p1 = p.traslateNew(d);
        }
        return p;
    }

    private void updateVisited(AoCPoint p, AoCVector d) {
        visited.computeIfAbsent(p.toString(), (k) -> new HashSet<>())
               .add(d.toString());
    }

    /**
     * ...You need to get the guard stuck in a loop by adding a single new obstruction.
     * How many different positions could you choose for this obstruction?
     */
    @Override
    protected Long partTwo() throws Exception {
        final AoCVector d0 = AoCVector.SOUTH;
        final AoCPoint p0 = board.searchFor('^');
        long loopsCount = board.forEach((p, c) -> {
            if (c == '#' || c == '^') return 0;
            board.set(p, '#');
            try {
                return findLoops(p0, d0) ? 1 : 0;
            } finally {
                board.set(p, '.');
            }
        });
        return loopsCount;
    }

    private boolean findLoops(AoCPoint p, AoCVector d) {
        visited.clear();
        updateVisited(p, d);
        for (; ; ) {
            p = moveStraight(p, d);
            if (board.getSafe(p.traslateNew(d)) == null) {
                break;
            }
            d = d.rotate90L();
            AoCPoint p1 = p.traslateNew(d);
            if (visited.getOrDefault(p1.toString(), emptySet()).contains(d.toString()))
                return true;
        }
        return false;
    }

}
