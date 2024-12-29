package adventofcode.commons;

import adventofcode.y2023.Problem14;

import java.util.*;

/**
 *
 */
public class SequenceAnalyzer<T> {

    private final int minLength;
    private final int maxLength;
    private final ArrayDeque<T> window;
    private final Map<Frame, List<T>> sequences = new HashMap<>();

    public SequenceAnalyzer(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.window = new ArrayDeque<>(maxLength * 2 + 1);
    }

    public List<T> analyze(T value) {
        window.add(value);
        if (window.size() < minLength) return null;
        while (window.size() > maxLength) window.removeFirst();

        // search for sequence matches
        for (int l = minLength; l <= maxLength; ++l) {
            ArrayList<T> buffer = new ArrayList<>(window);
            List<T> sub = buffer.subList(maxLength - l, maxLength);
            // Frame frame =
        }

        return null;
    }

    private class Frame {

        final long i;
        final int length;

        private Frame(long i, int length) {
            this.i = i;
            this.length = length;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Frame frame = (Frame) o;
            return i == frame.i && length == frame.length;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, length);
        }
    }
}
