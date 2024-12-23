package adventofcode.y2024;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.max;

/**
 * Day 23: LAN Party
 * https://adventofcode.com/2024/day/23
 */
public class Problem23 extends AoCProblem<String> {

    public static void main(String[] args) throws Exception {
        new Problem23().loadResourceAndSolve(false);
    }

    Map<String, Set<String>> graph = new HashMap<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        input.iterateLineExs().forEach(l -> {
            String from = l.before("-").toString();
            String to = l.after("-").toString();
            Set<String> fromLinks = graph.computeIfAbsent(from, k -> new HashSet<>());
            fromLinks.add(to);
            Set<String> toLinks = graph.computeIfAbsent(to, k -> new HashSet<>());
            toLinks.add(from);
        });
    }

    /**
     * ...Find all the sets of three inter-connected computers.
     * How many contain at least one computer with a name that starts with t?
     */
    @Override
    public String solvePartOne() throws Exception {
        Set<String> groups = new HashSet<>();
        for (var c : graph.keySet()) {
            groups.addAll(findGroupsOf3WithHistorian(c));
        }
        long count = groups.size();
        return Long.toString(count);
    }

    private Set<String> findGroupsOf3WithHistorian(String from) {
        Set<String> groups = new HashSet<>();
        List<String> tolist = graph.get(from).stream().toList();
        if (tolist.size() > 1) {
            for (int i = 0; i < tolist.size() - 1; ++i) {
                for (int j = i + 1; j < tolist.size(); ++j) {
                    String to1 = tolist.get(i);
                    String to2 = tolist.get(j);
                    if (!to1.startsWith("t") && !to2.startsWith("t") && !from.startsWith("t")) continue;
                    if (!graph.get(to1).contains(to2)) continue;
                    String group = groupToPassword(List.of(from, to1, to2));
                    groups.add(group);
                }
            }
        }
        return groups;
    }

    private static String groupToPassword(Collection<String> computers) {
        List<String> list = new ArrayList<>(computers);
        list.sort(String::compareTo);
        return list.stream().collect(Collectors.joining(","));
    }

    /**
     * ...Since it doesn't seem like any employees are around, you figure they
     * must all be at the LAN party. If that's true, the LAN party will be the
     * largest set of computers that are all connected to each other.
     * ...What is the password to get into the LAN party?
     */
    @Override
    public String solvePartTwo() throws Exception {
        Set<String> bestGroup = findBestGroup(Collections.EMPTY_SET, graph.keySet(), Collections.EMPTY_SET, 0);
        return groupToPassword(bestGroup);
    }

    private Set<String> findBestGroup(Set<String> group, Set<String> candidateSet, Set<String> visitedSet, int bestGroupSize) {
        if (group.size() + candidateSet.size() <= bestGroupSize) return group;
        if (candidateSet.isEmpty()) return group;

        Set<String> bestGroup = group;
        Set<String> newVisitedSet = new HashSet<>(visitedSet);
        for (var c : candidateSet) {
            if (!newVisitedSet.add(c)) continue;

            Set<String> linkSet = graph.get(c);
            if (!linkSet.containsAll(group)) continue;

            Set<String> newGroup = new HashSet<>(group);
            newGroup.add(c);
            Set<String> newCandidateSet = new HashSet<>(candidateSet);
            newCandidateSet.remove(c);
            Set<String> tmpGroup = findBestGroup(newGroup, newCandidateSet, newVisitedSet, max(bestGroup.size(), bestGroupSize));
            if (tmpGroup.size() > bestGroup.size()) {
                bestGroup = tmpGroup;
            }
        }
        return bestGroup;
    }
}
