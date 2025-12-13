package adventofcode.y2025;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.MemoizationCache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Day 11: Reactor
 * https://adventofcode.com/2025/day/11
 */
public class Problem11 extends AoCProblem<Long, Problem11> {

    public static void main(String[] args) throws Exception {
        new Problem11().loadResourceAndSolve(false);
    }

    private Map<String, Set<String>> graph = new HashMap<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        input.lineExs()
            .forEach(line -> {
                var node = line.before(": ").toString();
                var outs = line.after(": ").splitToString(" ");
                graph.put(node, new HashSet<>(outs));
            });
    }

    /**
     * ...How many different paths lead from you to out?
     */
    @Override
    public Long solvePartOne() throws Exception {
        return findAllPaths("you", "out");
    }

    private MemoizationCache<Long> findAllPathsCache = new MemoizationCache<>();

    private long findAllPaths(String from, String to) {
        return findAllPathsCache.key(from, to).andCompute(() -> {

            if (from.equals(to))
                return 1L;

            Set<String> links = graph.get(from);
            if (links == null || links.isEmpty())
                return 0L;

            long paths = 0;
            for (var link : links) {
                paths += findAllPaths(link, to);
            }
            return paths;
        });
    }

    /**
     * ...Find all of the paths that lead from svr to out.
     * How many of those paths visit both dac and fft?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        long paths_svr_fft = findAllPaths("svr", "fft");
        long paths_svr_dac = findAllPaths("svr", "dac");
        long paths_fft_dac = findAllPaths("fft", "dac");
        long paths_dac_fft = findAllPaths("dac", "fft");
        long paths_fft_out = findAllPaths("fft", "out");
        long paths_dac_out = findAllPaths("dac", "out");
        return paths_svr_fft * paths_fft_dac * paths_dac_out
            + paths_svr_dac * paths_dac_fft * paths_fft_out;
    }
}
