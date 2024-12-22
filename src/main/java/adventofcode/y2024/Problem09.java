package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.ArrayList;
import java.util.List;

/**
 * Day 9: Disk Fragmenter
 * https://adventofcode.com/2024/day/9
 */
public class Problem09 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem09().loadAndSolve(false);
    }

    private int[] data;

    @Override
    public void processInput(AoCInput input) throws Exception {
        data = input.toIntMatrix()[0];
    }

    /**
     * ...Compact the amphipod's hard drive using the process he requested.
     * What is the resulting filesystem checksum?
     * (Be careful copy/pasting the input for this puzzle; it is a single, very long line.)
     */
    @Override
    public Long solvePartOne() throws Exception {
        List<Long> disk = expand();

        int fileIndex = disk.size();
        int freeIndex = -1;
        while (freeIndex < fileIndex) {
            while (disk.get(--fileIndex) == -1) ; // find next file from the right to left
            while (disk.get(++freeIndex) != -1) ; // find next free index
            if (fileIndex <= freeIndex) break;
            disk.set(freeIndex, disk.get(fileIndex));
            disk.set(fileIndex, -1L);
        }

        long result = checksum(disk);
        return result;
    }

    private static long checksum(List<Long> disk) {
        long result = 0;
        for (int i = 0; i < disk.size(); i++) {
            if (disk.get(i) < 0) continue;
            result += disk.get(i) * i;
        }
        return result;
    }

    private List<Long> expand() {
        List<Long> result = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i]; j++) {
                if (i % 2 == 0) {
                    result.add((long) i / 2);
                } else {
                    result.add(-1L); // -1 is free space
                }
            }
        }
        return result;
    }

    // usefull only with short samples
    private static void dump(List<Long> expanded) {
        for (long n : expanded) {
            if (n == -1) System.out.print(".");
            else System.out.print(n);
        }
        System.out.println();
    }

    /**
     * ...This time, attempt to move whole files to the leftmost span of
     * free space blocks that could fit the file...
     * ...Start over, now compacting the amphipod's hard drive using this
     * new method instead. What is the resulting filesystem checksum?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        List<Long> disk = expand();

        int fileIndex = disk.size();
        end:
        while (fileIndex > 0) {
            // next file from right to left
            while (disk.get(--fileIndex) == -1) ;
            long fileId = disk.get(fileIndex);
            int fileSize = 1;
            while (fileIndex > 0 && disk.get(fileIndex - 1) == fileId) {
                fileSize++;
                fileIndex--;
            }

            // find first free space of right size
            int freeIndex = 0;
            int freeSize = 0;
            while (freeSize < fileSize) {
                while (disk.get(++freeIndex) != -1) ;
                if (freeIndex >= fileIndex) continue end; // eval next file
                freeSize = 1;
                int freeIndexEnd = freeIndex;
                while (disk.get(freeIndexEnd + 1) == -1) {
                    freeSize++;
                    freeIndexEnd++;
                }
            }

            // move file
            for (int i = 0; i < fileSize; ++i) {
                disk.set(freeIndex + i, disk.get(fileIndex + i));
                disk.set(fileIndex + i, -1L);
            }
        }

        long result = checksum(disk);
        return result;
    }
}
