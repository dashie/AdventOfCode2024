package adventofcode.y2023;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static adventofcode.y2023.Problem07.HandType.*;

/**
 * Day 7: Camel Cards
 * https://adventofcode.com/2023/day/7
 */
public class Problem07 extends AoCProblem<Long, Problem07> {

    public static void main(String[] args) throws Exception {
        new Problem07().loadResourceAndSolve(false);
    }

    record Hand(String cards, long bid) {}

    List<Hand> hands;

    @Override
    public void processInput(AoCInput input) throws Exception {
        // 32T3K 765
        hands = input.pattern("([A-Z0-9]{5}) ([0-9]+)")
            .map(m -> new Hand(m.get(1), m.getInt(2)))
            .toList();
    }


    /**
     * ...Now, you can determine the total winnings of this set
     * of hands by adding up the result of multiplying each hand's
     * bid with its rank (765 * 1 + 220 * 2 + 28 * 3 + 684 * 4 + 483 * 5).
     * So the total winnings in this example are 6440.
     *
     * Find the rank of every hand in your set.
     * What are the total winnings?
     */
    @Override
    public Long solvePartOne() throws Exception {
        return evalTotalWinnings(Problem07::evalTypeSimple, c -> switch (c) {
            case 'A' -> 14;
            case 'K' -> 13;
            case 'Q' -> 12;
            case 'J' -> 11;
            case 'T' -> 10;
            case '9' -> 9;
            case '8' -> 8;
            case '7' -> 7;
            case '6' -> 6;
            case '5' -> 5;
            case '4' -> 4;
            case '3' -> 3;
            case '2' -> 2;
            default -> throw new IllegalStateException();
        });
    }

    private long evalTotalWinnings(Function<String, HandType> typeMapper, Function<Character, Integer> valueMapper) {
        // log("%nevalTotalWinnings%n");
        List<TypedHand> ohands = hands.stream()
            .map(h -> new TypedHand(h, typeMapper.apply(h.cards)))
            .sorted((a, b) -> {
                int cmp = a.type.compareTo(b.type);
                if (cmp != 0) return cmp;
                for (int i = 0; i < 5; ++i) {
                    int acv = valueMapper.apply(a.hand.cards.charAt(i));
                    int bcv = valueMapper.apply(b.hand.cards.charAt(i));
                    cmp = Integer.compare(acv, bcv);
                    if (cmp != 0) return cmp;
                }
                return 0;
            })
            .toList();

        long result = 0;
        for (int i = 0; i < ohands.size(); ++i) {
            TypedHand th = ohands.get(i);
            // log("%s %14s %5d %d%n", th.hand.cards, th.type, th.hand.bid, th.hand.bid * (i + 1));
            result += th.hand.bid * (i + 1);
        }
        return result;
    }

    enum HandType {
        HIGH_CARD,     // -    A23A4
        ONE_PAIR,      // 2    A23A4
        TWO_PAIR,      // 2+2  23432
        THREE_OF_KIND, // 3    23332
        FULL_HOUSE,    // 3+2  23332
        FOUR_OF_KIND,  // 4    AA8AA
        FIVE_OF_KIND;  // 5    AAAAA
    }

    record TypedHand(Hand hand, HandType type) {}

    public static HandType evalTypeSimple(String cards) {
        Map<Character, Integer> map = new HashMap<>();
        HandType type = HIGH_CARD;
        for (int i = 0; i < cards.length(); ++i) {
            int n = map.compute(cards.charAt(i), (k, v) -> v == null ? 1 : v + 1);
            if (n == 5) {
                type = FIVE_OF_KIND;
            } else if (n == 4) {
                type = FOUR_OF_KIND;
            } else if (n == 3) {
                if (type == ONE_PAIR) {
                    type = THREE_OF_KIND;
                } else if (type == TWO_PAIR) {
                    type = FULL_HOUSE;
                } else throw new IllegalStateException();
            } else if (n == 2) {
                if (type == HIGH_CARD) {
                    type = ONE_PAIR;
                } else if (type == ONE_PAIR) {
                    type = TWO_PAIR;
                } else if (type == THREE_OF_KIND) {
                    type = FULL_HOUSE;
                } else throw new IllegalStateException();
            }
        }
        return type;
    }

    /**
     * ...Using the new joker rule, find the rank of every hand in your set.
     * What are the new total winnings?
     */
    @Override
    public Long solvePartTwo() throws Exception {
        return evalTotalWinnings(Problem07::evalTypeWithJoker, c -> switch (c) {
            case 'A' -> 14;
            case 'K' -> 13;
            case 'Q' -> 12;
            case 'T' -> 11;
            case '9' -> 10;
            case '8' -> 9;
            case '7' -> 8;
            case '6' -> 7;
            case '5' -> 6;
            case '4' -> 5;
            case '3' -> 4;
            case '2' -> 3;
            case 'J' -> 2;
            default -> throw new IllegalStateException();
        });
    }

    public static HandType evalTypeWithJoker(String cards) {
        HandType type = evalTypeSimple(cards);
        long jokers = cards.chars().filter(c -> c == 'J').count();
        if (jokers == 4) {
            return FIVE_OF_KIND;
        } else if (jokers == 3) {
            if (type == ONE_PAIR || type == FULL_HOUSE) {
                return FIVE_OF_KIND;
            } else {
                return FOUR_OF_KIND;
            }
        } else if (jokers == 2) {
            if (type == THREE_OF_KIND || type == FULL_HOUSE) {
                return FIVE_OF_KIND;
            } else if (type == TWO_PAIR) {
                return FOUR_OF_KIND;
            } else if (type == ONE_PAIR) {
                return THREE_OF_KIND;
            }
        } else if (jokers == 1) {
            if (type == FOUR_OF_KIND) {
                return FIVE_OF_KIND;
            } else if (type == THREE_OF_KIND) {
                return FOUR_OF_KIND;
            } else if (type == TWO_PAIR) {
                return FULL_HOUSE;
            } else if (type == ONE_PAIR) {
                return THREE_OF_KIND;
            } else {
                return ONE_PAIR;
            }
        }
        return type;
    }
}
