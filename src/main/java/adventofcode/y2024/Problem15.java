package adventofcode.y2024;

import adventofcode.commons.*;

import java.util.Arrays;

import static adventofcode.commons.Vector.charArrowToMatrixDirection;

/**
 * Day 15: Warehouse Woes
 * https://adventofcode.com/2024/day/15
 */
public class Problem15 extends AoCProblem<Long, Problem15> {

    public static void main(String[] args) throws Exception {
        new Problem15().loadResourceAndSolve(false);
    }

    Character[][] boardData;
    int[] movements;

    @Override
    public void processInput(AoCInput input) throws Exception {
        boardData = input.toCharMatrixUntilEmptyLine();
        movements = input.split("\n\n").getLast().chars().filter(c -> c != '\n').toArray();
    }

    /**
     * ...The lanternfish would like to know the sum of all
     * boxes' GPS coordinates after the robot finishes moving...
     * ...Predict the motion of the robot and boxes in the warehouse.
     * After the robot is finished moving, what is the sum of
     * all boxes' GPS coordinates?
     */
    @Override
    public Long solvePartOne() throws Exception {
        Board<Character> board = new Board<>(boardData);
        Point p = board.searchFor('@');
        for (Integer movement : movements) {
            Vector d = charArrowToMatrixDirection(movement);
            if (pushSimple(p, d, board)) p = p.translate(d);
        }
        return evalScore(board, 'O');
    }

    private long evalScore(Board<Character> board, Character boxChar) {
        long result = board.forEach((p, v) -> v == boxChar ? 100 * p.y + p.x : 0);
        return result;
    }

    private boolean pushSimple(Point p, Vector d, Board<Character> board) {
        Character c = board.get(p);
        if (c == '.') return true;
        if (c == '#') return false;
        Point nextPoint = p.translate(d);
        if (!pushSimple(nextPoint, d, board)) return false;
        board.set(nextPoint, board.get(p));
        board.set(p, '.');
        return true;
    }

    /**
     * ...Predict the motion of the robot and boxes in this new, scaled-up warehouse.
     * What is the sum of all boxes' final GPS coordinates?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        Character[][] boardDataExpanded = expandBoardData();
        Board<Character> board = new Board<>(boardDataExpanded);
        Point p = board.searchFor('@');
        for (Integer movement : movements) {
            Vector d = charArrowToMatrixDirection(movement);
            if (checkAndPushComplex(p, d, board)) p = p.translate(d);
        }
        return evalScore(board, '[');
    }

    private Character[][] expandBoardData() {
        return Arrays
            .stream(boardData)
            .map(line -> {
                Character[] largeLine = new Character[line.length * 2];
                for (int i = 0; i < line.length; ++i) {
                    Character c = line[i];
                    switch (c) {
                        case '#' -> {
                            largeLine[i * 2] = c;
                            largeLine[i * 2 + 1] = c;
                        }
                        case 'O' -> {
                            largeLine[i * 2] = '[';
                            largeLine[i * 2 + 1] = ']';
                        }
                        case '@' -> {
                            largeLine[i * 2] = '@';
                            largeLine[i * 2 + 1] = '.';
                        }
                        case '.' -> {
                            largeLine[i * 2] = '.';
                            largeLine[i * 2 + 1] = '.';
                        }
                        default -> throw new IllegalStateException();
                    }
                }
                return largeLine;
            })
            .toArray(Character[][]::new);
    }

    private boolean checkAndPushComplex(Point p, Vector d, Board<Character> board) {
        if (checkComplex(p, d, board)) {
            pushComplex(p, d, board);
            return true;
        }
        return false;
    }

    private boolean checkComplex(Point p, Vector d, Board<Character> board) {
        Character c = board.get(p);
        if (c == '.') return true;
        if (c == '#') return false;
        Point next = p.translate(d);
        if (d.isEast() || d.isWest()) {
            // left/right movement does not change in the complex scenario
            if (!checkComplex(next, d, board)) return false;
        } else {
            if (c == '[') {
                if (!checkComplex(next, d, board) || !checkComplex(next.east(), d, board))
                    return false;
            } else if (c == ']') {
                if (!checkComplex(next, d, board) || !checkComplex(next.west(), d, board))
                    return false;
            } else if (c == '@') {
                if (!checkComplex(next, d, board)) return false;
            }
        }
        return true;
    }

    private void pushComplex(Point p, Vector d, Board<Character> board) {
        Character c = board.get(p);
        if (c == '.') return;
        if (c == '#') return;
        Point next = p.translate(d);
        if (d.isEast() || d.isWest()) {
            // left/right movement does not change in the complex scenario
            pushComplex(next, d, board);
            board.set(next, board.get(p));
            board.set(p, '.');
        } else {
            if (c == '[') {
                pushComplex(next, d, board);
                pushComplex(next.east(), d, board);
                board.set(next, '[');
                board.set(next.east(), ']');
                board.set(p, '.');
                board.set(p.east(), '.');
            } else if (c == ']') {
                pushComplex(next, d, board);
                pushComplex(next.west(), d, board);
                board.set(next, ']');
                board.set(next.west(), '[');
                board.set(p, '.');
                board.set(p.west(), '.');
            } else if (c == '@') {
                pushComplex(next, d, board);
                board.set(next, board.get(p));
                board.set(p, '.');
            }
        }
    }
}
