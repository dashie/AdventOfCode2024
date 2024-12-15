package adventofcode.y2024;

import adventofcode.commons.*;

import java.util.Arrays;

import static adventofcode.commons.AoCVector.charToDirection;

/**
 * Day 15: Warehouse Woes
 * https://adventofcode.com/2024/day/15
 */
public class Problem15 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem15().solve(false);
    }

    Character[][] boardData;
    int[] movements;

    @Override
    public void processInput(AoCInput input) throws Exception {
        boardData = input.toCharMatrixEmptyLine();
        movements = input.toSingleString().chars().filter(c -> c != '\n').toArray();
    }

    /**
     * ...The lanternfish would like to know the sum of all
     * boxes' GPS coordinates after the robot finishes moving...
     * ...Predict the motion of the robot and boxes in the warehouse.
     * After the robot is finished moving, what is the sum of
     * all boxes' GPS coordinates?
     */
    @Override
    public Long partOne() throws Exception {
        AoCBoard<Character> board = new AoCBoard<>(boardData);
        AoCPoint p0 = board.searchFor('@');
        AoCPoint p = p0;
        for (Integer movement : movements) {
            AoCVector d = charToDirection(movement);
            if (moveBlockSimple(p, d, board)) p = p.translate(d);
        }
        return evalScore(board, 'O');
    }

    private long evalScore(AoCBoard<Character> board, Character boxChar) {
        long result = board.forEach((p, v) -> v == boxChar ? 100 * p.y + p.x : 0);
        return result;
    }

    private boolean moveBlockSimple(AoCPoint p, AoCVector d, AoCBoard<Character> board) {
        Character c = board.get(p);
        if (c == '.') return true;
        if (c == '#') return false;
        AoCPoint nextPoint = p.translate(d);
        if (!moveBlockSimple(nextPoint, d, board)) return false;
        board.set(nextPoint, board.get(p));
        board.set(p, '.');
        return true;
    }

    /**
     * ...Predict the motion of the robot and boxes in this new, scaled-up warehouse.
     * What is the sum of all boxes' final GPS coordinates?
     */
    @Override
    public Long partTwo() throws Exception {
        Character[][] boardDataExpanded = expandBoardData();
        AoCBoard<Character> board = new AoCBoard<>(boardDataExpanded);
        AoCPoint p0 = board.searchFor('@');
        AoCPoint p = p0;
        for (Integer movement : movements) {
            AoCVector d = charToDirection(movement);
            if (checkAndMoveBlockComplex(p, d, board)) p = p.translate(d);
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

    private boolean checkAndMoveBlockComplex(AoCPoint p, AoCVector d, AoCBoard<Character> board) {
        if (checkMoveComplex(p, d, board)) {
            moveBlockComplex(p, d, board);
            return true;
        }
        return false;
    }

    private boolean checkMoveComplex(AoCPoint p, AoCVector d, AoCBoard<Character> board) {
        Character c = board.get(p);
        if (c == '.') return true;
        if (c == '#') return false;
        AoCPoint next = p.translate(d);
        if (d.isEast() || d.isWest()) {
            // left/right movement does not change in the complex scenario
            if (!checkMoveComplex(next, d, board)) return false;
        } else {
            if (c == '[') {
                if (!checkMoveComplex(next, d, board) || !checkMoveComplex(next.east(), d, board))
                    return false;
            } else if (c == ']') {
                if (!checkMoveComplex(next, d, board) || !checkMoveComplex(next.west(), d, board))
                    return false;
            } else if (c == '@') {
                if (!checkMoveComplex(next, d, board)) return false;
            }
        }
        return true;
    }

    private void moveBlockComplex(AoCPoint p, AoCVector d, AoCBoard<Character> board) {
        Character c = board.get(p);
        if (c == '.') return;
        if (c == '#') return;
        AoCPoint next = p.translate(d);
        if (d.isEast() || d.isWest()) {
            moveBlockComplex(next, d, board);
            board.set(next, board.get(p));
            board.set(p, '.');
        } else {
            if (c == '[') {
                moveBlockComplex(next, d, board);
                moveBlockComplex(next.east(), d, board);
                board.set(next, '[');
                board.set(next.east(), ']');
                board.set(p, '.');
                board.set(p.east(), '.');
            } else if (c == ']') {
                moveBlockComplex(next, d, board);
                moveBlockComplex(next.west(), d, board);
                board.set(next, ']');
                board.set(next.west(), '[');
                board.set(p, '.');
                board.set(p.west(), '.');
            } else if (c == '@') {
                moveBlockComplex(next, d, board);
                board.set(next, board.get(p));
                board.set(p, '.');
            }
        }
    }
}
