package adventofcode.y2023;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.Point;
import adventofcode.commons.Rect;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

/**
 * Day 22: Sand Slabs
 * https://adventofcode.com/2023/day/22
 */
public class Problem22 extends AoCProblem<Long, Problem22> {

    public static void main(String[] args) throws Exception {
        new Problem22().loadResourceAndSolve(false);
    }

    List<Rect> bricks0 = new ArrayList<>();
    Map<Integer, List<Point>> brickBlocks0 = new HashMap<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        int brickId = 0;
        for (var l : input.iterateLineExs()) {
            // 1,0,1~1,2,1
            Point p1 = Point.parsePoint(l.before("~").toString());
            Point p2 = Point.parsePoint(l.after("~").toString());
            if (p1.x > p2.x) throw new IllegalArgumentException();
            if (p1.y > p2.y) throw new IllegalArgumentException();
            if (p1.z > p2.z) throw new IllegalArgumentException();
            if (p1.z < 1) throw new IllegalArgumentException();

            Rect r = Rect.of(p1, p2);
            bricks0.add(r);
            brickBlocks0.put(brickId, brickBlocks(r));

            //
            brickId++;
        }
    }

    private static List<Point> brickBlocks(Rect r) {
        List<Point> blocks = new ArrayList<>();
        for (int x = r.p1.x; x <= r.p2.x; ++x) {
            for (int y = r.p1.y; y <= r.p2.y; ++y) {
                for (int z = r.p1.z; z <= r.p2.z; ++z) {
                    Point p = Point.of(x, y, z);
                    blocks.add(p);
                }
            }
        }
        return blocks;
    }

    /**
     * ...Figure how the blocks will settle based on the snapshot.
     * Once they've settled, consider disintegrating a single brick;
     * how many bricks could be safely chosen as the one to get disintegrated?
     */
    @Override
    public Long solvePartOne() throws Exception {
        Map<Integer, List<Point>> brickBlocks = new HashMap<>(brickBlocks0);
        // space point, bricks id (position in the list)
        Map<Point, Integer> xyzSpace = new HashMap<>();
        for (var b : brickBlocks0.entrySet()) {
            b.getValue().forEach(p -> xyzSpace.put(p, b.getKey()));
        }

        //
        settleBricks(brickBlocks, xyzSpace);
        long result = countDisintegrableBricks(brickBlocks, xyzSpace);
        return result;
    }

    private void settleBricks(Map<Integer, List<Point>> brickBlocks, Map<Point, Integer> xyzSpace) {
        boolean tryToSettle = true;
        while (tryToSettle) {
            tryToSettle = false;

            // sort by z (from 1 to -> N)
            List<Map.Entry<Integer, List<Point>>> zbricks = brickBlocks.entrySet().stream()
                .sorted(comparingInt(e -> e.getValue().getFirst().z))
                .toList();

            // while space below is empty (and z >= 1) I can make the brick fall
            for (var b : zbricks) {
                int z = b.getValue().getFirst().z;
                if (z <= 1) continue;

                List<Point> blocksBelow = b.getValue().stream()
                    .map(p -> p.translate(0, 0, -1))
                    .toList();

                Set<Integer> bricksBelow = blocksBelow.stream()
                    .map(xyzSpace::get)
                    .filter(Objects::nonNull)
                    .filter(id -> !b.getKey().equals(id)) // remove itself in the case of standing bricks
                    .collect(Collectors.toSet());

                if (bricksBelow.isEmpty()) { // no brick below
                    b.getValue().forEach(p -> xyzSpace.remove(p));
                    blocksBelow.forEach(p -> xyzSpace.put(p, b.getKey()));
                    brickBlocks.put(b.getKey(), blocksBelow);
                    tryToSettle = true;
                }
            }
        }
    }

    private long countDisintegrableBricks(Map<Integer, List<Point>> brickBlocks, Map<Point, Integer> xyzSpace) {
        long count = 0;
        for (var id : brickBlocks.keySet()) {
            if (isDisintegrable(id, brickBlocks, xyzSpace))
                count++;
        }
        return count;
    }

    private boolean isDisintegrable(
        int brickId,
        Map<Integer, List<Point>> brickBlocks,
        Map<Point, Integer> xyzSpace) {

        // bricks can be removed if they do not support other bricks
        // or once removed the bricks they are supporting are supported by
        // other bricks

        Set<Integer> supportedBricks = findSupportedBricks(brickId, brickBlocks, xyzSpace);
        if (supportedBricks.size() == 0)
            return true;

        for (Integer sbid : supportedBricks) {
            Set<Integer> supportingBricks = findSupportingBricks(sbid, brickBlocks, xyzSpace);
            if (supportingBricks.size() == 1) // there is only one brick that support it (and that brick is the brick we are checking)
                return false;
        }

        // no above brick is supported only by this one
        // so brick can be disintegreted
        return true;
    }

    private Set<Integer> findSupportedBricks(int brickId, Map<Integer, List<Point>> brickBlocks, Map<Point, Integer> xyzSpace) {
        List<Point> blocksAbove = brickBlocks.get(brickId).stream()
            .map(p -> p.translate(0, 0, 1))
            .toList();

        Set<Integer> supportedBricks = blocksAbove.stream()
            .map(xyzSpace::get)
            .filter(Objects::nonNull)
            .filter(id -> brickId != id) // remove itself in the case of standing bricks
            .collect(Collectors.toSet());

        return supportedBricks;
    }

    private Set<Integer> findSupportingBricks(Integer brickId, Map<Integer, List<Point>> brickBlocks, Map<Point, Integer> xyzSpace) {
        List<Point> blocksBelow = brickBlocks.get(brickId).stream()
            .map(p -> p.translate(0, 0, -1))
            .toList();

        Set<Integer> bricksBelow = blocksBelow.stream()
            .map(xyzSpace::get)
            .filter(Objects::nonNull)
            .filter(id -> !brickId.equals(id)) // remove itself in the case of standing bricks
            .collect(Collectors.toSet());
        return bricksBelow;
    }

    /**
     * ...For each brick, determine how many other bricks would fall if that
     * brick were disintegrated. What is the sum of the number of other bricks
     * that would fall?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        Map<Integer, List<Point>> brickBlocks = new HashMap<>(brickBlocks0);
        // space point, bricks id (position in the list)
        Map<Point, Integer> xyzSpace = new HashMap<>();
        for (var b : brickBlocks0.entrySet()) {
            b.getValue().forEach(p -> xyzSpace.put(p, b.getKey()));
        }

        //
        settleBricks(brickBlocks, xyzSpace);
        long result = countSupportedBricks(brickBlocks, xyzSpace);
        return result;
    }

    private long countSupportedBricks(Map<Integer, List<Point>> brickBlocks, Map<Point, Integer> xyzSpace) {
        long count = 0;
        for (var b : brickBlocks.entrySet()) {
            int brickId = b.getKey();

            Set<Integer> affectedBricksStack = new HashSet<>();
            affectedBricksStack.add(brickId);
            Deque<Integer> stack = new LinkedList<>();
            stack.add(brickId);
            while (!stack.isEmpty()) {
                int bid = stack.poll();
                Set<Integer> supportedBricks = findSupportedBricks(bid, brickBlocks, xyzSpace);
                for (var supportedBrick : supportedBricks) {
                    if (affectedBricksStack.contains(supportedBrick)) continue;
                    Set<Integer> supportingBricks = findSupportingBricks(supportedBrick, brickBlocks, xyzSpace);
                    supportingBricks.removeAll(affectedBricksStack);
                    if (supportingBricks.isEmpty()) {
                        affectedBricksStack.add(supportedBrick);
                        stack.add(supportedBrick);
                    }
                }
            }

            affectedBricksStack.remove(brickId);
            count += affectedBricksStack.size();
        }
        return count;
    }
}
