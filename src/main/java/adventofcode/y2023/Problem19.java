package adventofcode.y2023;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;
import adventofcode.commons.MatcherEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Day 19: Aplenty
 * https://adventofcode.com/2023/day/19
 */
public class Problem19 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem19().solve(false);
    }

    record Part(int x, int m, int a, int s) {

        public int cat(char c) {
            return switch (c) {
                case 'x' -> x;
                case 'm' -> m;
                case 'a' -> a;
                case 's' -> s;
                default -> throw new IllegalStateException();
            };
        }

        public long rate() {
            return x + m + a + s;
        }
    }

    record Rule(char cat, char op, int value, String destination) {}

    record Workflow(String name, List<Rule> rules) {}

    private List<Part> parts = new ArrayList<>();

    private Map<String, Workflow> workflows = new HashMap<>();

    @Override
    public void processInput(AoCInput input) throws Exception {

        // load workflows
        for (LineEx line : input.before("\n\n").iterateLineExs()) {
            // px{a<2006:qkq,m>2090:A,rfg}
            String wfid = line.before("\\{").toString();
            List<Rule> rules = new ArrayList<>();
            line = line.after("\\{").before("\\}");
            List<LineEx> rulestrs = line.split(",");
            rulestrs.forEach(r -> {
                MatcherEx m = r.match("(?:([a-z]+)([<>])([0-9]+):)?([a-z]+)", Pattern.CASE_INSENSITIVE);
                rules.add(new Rule(m.getChar(1, '.'), m.getChar(2, '.'), m.getInt(3, 0), m.get(4)));
            });
            workflows.put(wfid, new Workflow(wfid, rules));
        }

        // load parts
        for (LineEx line : input.after("\n\n").iterateLineExs()) {
            // {x=674,m=504,a=708,s=190}
            MatcherEx m = line.match("\\{x=([0-9]+),m=([0-9]+),a=([0-9]+),s=([0-9]+)\\}");
            Part part = new Part(
                m.getInt(1),
                m.getInt(2),
                m.getInt(3),
                m.getInt(4)
            );
            parts.add(part);
        }
    }

    /**
     * ...Sort through all of the parts you've been given; what do you get
     * if you add together all of the rating numbers for all of the parts
     * that ultimately get accepted?
     */
    @Override
    public Long partOne() throws Exception {
        return parts.stream()
                    .filter(this::isAccepted)
                    .mapToLong(Part::rate)
                    .sum();
    }

    private boolean isAccepted(Part part) {
        Workflow wf = workflows.get("in");
        while (wf != null) {
            for (Rule rule : wf.rules) {
                if (rule.cat != '.') {
                    if (rule.op == '>' && part.cat(rule.cat) <= rule.value) continue;
                    if (rule.op == '<' && part.cat(rule.cat) >= rule.value) continue;
                }
                if ("R".equals(rule.destination)) return false;
                else if ("A".equals(rule.destination)) return true;
                else {
                    wf = workflows.get(rule.destination);
                    break;
                }
            }
        }
        throw new IllegalStateException();
    }

    /**
     * ...Consider only your list of workflows; the list of part ratings that the Elves
     * wanted you to sort is no longer relevant.
     * How many distinct combinations of ratings will be accepted by the Elves' workflows?
     */
    @Override
    public Long partTwo() throws Exception {
        Workflow wf = workflows.get("in");
        return countAcceptedCombinations(wf, new PartCriteria(
            new Range(1, 4000),
            new Range(1, 4000),
            new Range(1, 4000),
            new Range(1, 4000)
        ));
    }

    record Range(long min, long max) {

        public long size() {
            return max > min ? max - min + 1 : 0;
        }
    }

    record PartCriteria(Range x, Range m, Range a, Range s) {

        public long combinations() {
            return x.size() * m.size() * a.size() * s.size();
        }

        public PartCriteria setMax(char c, int value) {
            return switch (c) {
                case 'x' -> new PartCriteria(new Range(x.min, min(x.max, value)), m, a, s);
                case 'm' -> new PartCriteria(x, new Range(m.min, min(m.max, value)), a, s);
                case 'a' -> new PartCriteria(x, m, new Range(a.min, min(a.max, value)), s);
                case 's' -> new PartCriteria(x, m, a, new Range(s.min, min(s.max, value)));
                default -> throw new IllegalStateException();
            };
        }

        public PartCriteria setMin(char c, int value) {
            return switch (c) {
                case 'x' -> new PartCriteria(new Range(max(x.min, value), x.max), m, a, s);
                case 'm' -> new PartCriteria(x, new Range(max(m.min, value), m.max), a, s);
                case 'a' -> new PartCriteria(x, m, new Range(max(a.min, value), a.max), s);
                case 's' -> new PartCriteria(x, m, a, new Range(max(s.min, value), s.max));
                default -> throw new IllegalStateException();
            };
        }
    }

    private long countAcceptedCombinations(Workflow wf, PartCriteria p) {
        long n = 0;
        for (Rule rule : wf.rules) {
            PartCriteria pThatMatchRule = p;
            if (rule.cat != '.') {
                if (rule.op == '>') {
                    pThatMatchRule = pThatMatchRule.setMin(rule.cat, rule.value + 1);
                    p = p.setMax(rule.cat, rule.value); // constraint p to values that does not match
                } else if (rule.op == '<') {
                    pThatMatchRule = pThatMatchRule.setMax(rule.cat, rule.value - 1);
                    p = p.setMin(rule.cat, rule.value); // constraint p to values that does not match
                } else throw new IllegalStateException();
            }
            if ("A".equals(rule.destination)) {
                n += pThatMatchRule.combinations();
            } else if ("R".equals(rule.destination)) {
                continue;
            } else {
                n += countAcceptedCombinations(workflows.get(rule.destination), pThatMatchRule);
            }
        }
        return n;
    }
}
