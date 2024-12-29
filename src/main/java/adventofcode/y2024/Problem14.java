package adventofcode.y2024;

import adventofcode.commons.*;
import adventofcode.commons.Vector;

import java.util.*;

/**
 * Day 14: Restroom Redoubt
 * https://adventofcode.com/2024/day/14
 */
public class Problem14 extends AoCProblem<Long, Problem14> {

    public static void main(String[] args) throws Exception {
        new Problem14().loadResourceAndSolve(false);
    }

    class Robot {

        Point p0;
        Vector v;
        Point p;
    }

    private List<Robot> robots = new ArrayList<>();
    int areaX = 0;
    int areaY = 0;

    @Override
    public void processInput(AoCInput input) throws Exception {
        input.iterateLineExs()
             .forEach(line -> {
                 MatcherEx m = line.match("p=([0-9]+),([0-9]+) v=(-?[0-9]+),(-?[0-9]+)");
                 Robot robot = new Robot();
                 robot.p0 = new Point(m.getInt(1), m.getInt(2));
                 robot.v = new Vector(m.getInt(3), m.getInt(4));
                 robots.add(robot);
             });

        if (isUsingSampleResource()) {
            areaX = 11;
            areaY = 7;
        } else {
            areaX = 101;
            areaY = 103;
        }
    }

    /**
     * ...Predict the motion of the robots in your list within a space which
     * is 101 tiles wide and 103 tiles tall.
     * What will the safety factor be after exactly 100 seconds
     * have elapsed?
     */
    @Override
    public Long solvePartOne() throws Exception {
        moveRobots(100);
        return (long) evalSafetyFactor();
    }

    private Map<Point, Integer> moveRobots(int time) {
        Map<Point, Integer> points = new HashMap<>();
        for (Robot robot : robots) {
            int x = (robot.p0.x + robot.v.x * time) % areaX;
            if (x < 0) x += areaX;
            int y = (robot.p0.y + robot.v.y * time) % areaY;
            if (y < 0) y += areaY;
            robot.p = new Point(x, y);
            points.compute(robot.p, (k, v) -> v == null ? 1 : v + 1);
        }
        return points;
    }

    public int evalSafetyFactor() {
        int[] quadrants = new int[4];
        for (Robot robot : robots) {
            int middleX = areaX / 2;
            int middleY = areaY / 2;
            if (robot.p.x > middleX) {
                if (robot.p.y > middleY) {
                    quadrants[3]++; // SE
                } else if (robot.p.y < middleY) {
                    quadrants[1]++; // NE
                }
            } else if (robot.p.x < middleX) {
                if (robot.p.y > middleY) {
                    quadrants[2]++; // SW
                } else if (robot.p.y < middleY) {
                    quadrants[0]++; // NW
                }
            }
        }
        return Arrays.stream(quadrants).reduce(1, (a, b) -> a * b);
    }

    /**
     * ...What is the fewest number of seconds that must elapse for the
     * robots to display the Easter egg?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        int cycleLength = areaX * areaY;
        double easterEggSpreadFactor = 1.0;
        double easterEggTime = -1;
        Map<Point, Integer> easterEggPoints = null;

        for (int time = 1; time < cycleLength; ++time) {
            Map<Point, Integer> points = moveRobots(time);
            // if there is a shape the spread factor should be low
            double spreadFactor = evalSpreadFactor(points);
            if (spreadFactor < easterEggSpreadFactor) {
                easterEggSpreadFactor = spreadFactor;
                easterEggTime = time;
                easterEggPoints = points;
            }
        }

        Board.dumpBoard("time=%s spread factor=%s".formatted(easterEggTime, easterEggSpreadFactor),
            areaX, areaY, easterEggPoints,
            (p, v) -> (v == null) ? " " : "*");
        return (long) easterEggTime;
    }

    public double evalSpreadFactor(Map<Point, Integer> points) {
        // every robots scores 16 if all adjacent len are free
        //              scores  4 if only one adjacent len is filled
        //              scores  2 if only two adjacents len is filled
        //              scores  0 if more then two adjacent len is filled
        double max = robots.size() * 16.0;
        int score = 0;
        for (Robot robot : robots) {
            int count = 0;
            for (Vector d : Vector.DIRECTIONS_EXT) {
                Point p = robot.p.translate(d);
                if (points.containsKey(p)) count++;
            }
            switch (count) {
                case 0 -> score += 16;
                case 1 -> score += 2;
                case 2 -> score += 1;
            }
        }
        return score / max;
    }
}
