package adventofcode.y2023;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Day 5: If You Give A Seed A Fertilizer
 * https://adventofcode.com/2023/day/5
 */
public class Problem05 extends AoCProblem<Long, Problem05> {

    public static void main(String[] args) throws Exception {
        new Problem05().loadResourceAndSolve(false);
    }

    record TranslationRecord(long sourceIndex, long len, long destIndex) {}

    List<Long> seeds;
    Map<String, TreeMap<Long, TranslationRecord>> recordsMap = new HashMap<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        // seeds: 79 14 55 13
        seeds = input.before("\n\n").firstLineEx().getListOfLong("seeds: ([0-9 ]+)", "\\s+");
        String mapId = null;
        for (LineEx line : input.after("\n\n").iterateLineExs()) {
            if (line.isEmpty()) continue;
            if (Character.isDigit(line.charAt(0))) {
                if (mapId == null) throw new IllegalStateException();
                TreeMap<Long, TranslationRecord> r = recordsMap.computeIfAbsent(mapId, k -> new TreeMap<>());
                // 0 15 37
                List<Long> values = line.splitToLong("\\s+");
                r.put(values.get(1), new TranslationRecord(values.get(1), values.get(2), values.get(0)));
            } else {
                // new map, e.g. soil-to-fertilizer map:
                mapId = line.before(" map:").toString();
            }
        }
    }

    /**
     * ...What is the lowest location number that corresponds
     * to any of the initial seed numbers?
     */
    @Override
    public Long solvePartOne() throws Exception {
        long minLocation = seeds.stream()
            .mapToLong(this::seedToLocation)
            .min().getAsLong();
        return minLocation;
    }

    long seedToLocation(long seed) {
        long n = translateId("seed-to-soil", seed);
        n = translateId("soil-to-fertilizer", n);
        n = translateId("fertilizer-to-water", n);
        n = translateId("water-to-light", n);
        n = translateId("light-to-temperature", n);
        n = translateId("temperature-to-humidity", n);
        n = translateId("humidity-to-location", n);
        return n;
    }

    private long translateId(String mapId, long n) {
        var r = recordsMap.get(mapId).floorEntry(n);
        if (r == null) return n;
        var mapper = r.getValue();
        if (n >= mapper.sourceIndex + mapper.len) return n;
        return n - mapper.sourceIndex + mapper.destIndex;
    }

    /**
     * ...Consider all of the initial seed numbers listed in the ranges
     * on the first line of the almanac.
     * What is the lowest location number that corresponds to any of
     * the initial seed numbers?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        List<long[]> ranges = IntStream.range(0, seeds.size() / 2)
            .mapToObj(i -> new long[]{seeds.get(i * 2), seeds.get(i * 2) + seeds.get(i * 2 + 1) - 1})
            .map(this::seedRangeToLocationRanges)
            .flatMap(l -> l.stream())
            .toList();

        long lowestLocation = ranges.stream()
            .mapToLong(r -> r[0])
            .min().getAsLong();

        return lowestLocation;
    }

    /**
     * Range is represented ad a `long[]` with
     * start value at long[0] and end value at long[1].
     */
    List<long[]> seedRangeToLocationRanges(long[] range) {
        List<long[]> r = translateRange("seed-to-soil", List.of(range));
        r = translateRange("soil-to-fertilizer", r);
        r = translateRange("fertilizer-to-water", r);
        r = translateRange("water-to-light", r);
        r = translateRange("light-to-temperature", r);
        r = translateRange("temperature-to-humidity", r);
        r = translateRange("humidity-to-location", r);
        return r;
    }

    private List<long[]> translateRange(String mapId, List<long[]> ranges) {
        List<long[]> tranges = new ArrayList<>();
        for (long[] range : ranges) {
            while (range[0] <= range[1]) {
                // log("translateRange: %11d %11d %s%n", range[0], range[1] - range[0] + 1, mapId);
                long[] trange = new long[2]; // range translated
                TranslationRecord r1 = getTranslationRecord(mapId, range[0]);
                trange[0] = r1.destIndex + range[0] - r1.sourceIndex;
                if (range[1] < r1.sourceIndex + r1.len) {
                    trange[1] = trange[0] + range[1] - range[0];
                    tranges.add(trange);
                } else {
                    trange[1] = r1.destIndex + r1.len - 1;
                    tranges.add(trange);
                }
                range[0] += trange[1] - trange[0] + 1;
            }
        }
        return tranges;
    }

    private TranslationRecord getTranslationRecord(String mapId, long n) {
        var e1 = recordsMap.get(mapId).floorEntry(n);
        if (e1 == null) {
            var e2 = recordsMap.get(mapId).ceilingEntry(n);
            if (e2 == null) return new TranslationRecord(0, Long.MAX_VALUE, 0);
            return new TranslationRecord(0, e2.getValue().sourceIndex, 0);
        } else {
            var r1 = e1.getValue();
            var hv = r1.sourceIndex + r1.len;
            if (n >= hv) {
                var e2 = recordsMap.get(mapId).ceilingEntry(n);
                if (e2 == null) return new TranslationRecord(hv, Long.MAX_VALUE - hv, hv);
                return new TranslationRecord(hv, e2.getValue().sourceIndex - hv, hv);
            } else {
                return r1;
            }
        }
    }

}
