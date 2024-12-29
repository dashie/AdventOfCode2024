package adventofcode.y2023;

import adventofcode.commons.*;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static adventofcode.commons.Vector.NORTH;
import static adventofcode.commons.Vector.SOUTH;

/**
 * Day 16: The Floor Will Be Lava
 * https://adventofcode.com/2023/day/16
 */
public class Problem16 extends AoCProblem<Long, Problem16> {

    public static void main(String[] args) throws Exception {
        new Problem16().loadResourceAndSolve(false);
    }

    Board<Character> board;

    @Override
    public void processInput(AoCInput input) throws Exception {
        board = input.toCharBoard();
    }

    /**
     * ...The light isn't energizing enough tiles to produce lava;
     * to debug the contraption, you need to start by analyzing the
     * current situation. With the beam starting in the top-left
     * heading right, how many tiles end up being energized?
     */
    @Override
    public Long solvePartOne() throws Exception {
        Set<Point> energizedTiles = simulateBeam(DirectedPoint.of(0, 0, Vector.EAST));
        return (long) energizedTiles.size();
    }

    public Set<Point> simulateBeam(DirectedPoint dp0) {
        return simulateBeam(dp0, new HashSet<>());
    }

    public Set<Point> simulateBeam(DirectedPoint dp0, Set<DirectedPoint> globalVisited) {
        Set<Point> energizedTiles = new HashSet<>();
        Set<DirectedPoint> visited = new HashSet<>();
        Deque<DirectedPoint> stack = new LinkedList<>();
        stack.add(dp0);
        while (!stack.isEmpty()) {
            var dp = stack.pollFirst();
            var c = board.get(dp.p, ' ');
            if (c == ' ') continue; // out of board
            if (!visited.add(dp)) continue;
            globalVisited.add(dp);
            energizedTiles.add(dp.p);

            if (c == '.') {
                stack.add(dp.moveFront());
            } else if (c == '/') {
                if (dp.d.is(NORTH, SOUTH)) {
                    stack.add(dp.moveLeft());
                } else {
                    stack.add(dp.moveRight());
                }
            } else if (c == '\\') {
                if (dp.d.is(NORTH, SOUTH)) {
                    stack.add(dp.moveRight());
                } else {
                    stack.add(dp.moveLeft());
                }
            } else if (c == '|') {
                if (dp.d.is(NORTH, SOUTH)) {
                    stack.add(dp.moveFront());
                } else {
                    stack.add(dp.moveRight());
                    stack.add(dp.moveLeft());
                }
            } else if (c == '-') {
                if (dp.d.is(NORTH, SOUTH)) {
                    stack.add(dp.moveRight());
                    stack.add(dp.moveLeft());
                } else {
                    stack.add(dp.moveFront());
                }
            } else {
                throw new IllegalStateException();
            }
        }
        return energizedTiles;
    }

    /**
     * ...Find the initial beam configuration that energizes the
     * largest number of tiles; how many tiles are energized in
     * that configuration?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        Set<DirectedPoint> globalVisited = new HashSet<>();
        long result = 0;
        for (var c : board.cells()) {
            if (c.p.x != 0 && c.p.x != board.N - 1 && c.p.y != 0 && c.p.y != board.M - 1) continue;
            for (var d : Vector.DIRECTIONS) {
                if (!board.isValidCell(c.p.translate(d))) continue;
                DirectedPoint dp = DirectedPoint.of(c.p, d);
                if (globalVisited.contains(dp)) continue;
                Set<Point> energizedTiles = simulateBeam(dp, globalVisited);
                if (energizedTiles.size() > result) {
                    // log("%-20s : %d%n", dp, energizedTmp.size());
                    result = energizedTiles.size();
                }
            }
        }
        return result;
    }
}
