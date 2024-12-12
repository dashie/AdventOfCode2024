package adventofcode.y2015;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Day 22: Wizard Simulator 20XX
 * https://adventofcode.com/2015/day/22
 */
public class Problem22 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem22().solve(false);
    }

    private int bossHp0 = 0;
    private int bossDamage0 = 0;
    private int playerHp0 = 0;
    private int playerMana0 = 0;
    private Game bestResult;

    @Override
    public void processInput(AoCInput input) throws Exception {
        Iterator<LineEx> i = input.iterateLineExs().iterator();
        bossHp0 = i.next().getInt("\\d+");
        bossDamage0 = i.next().getInt("\\d+");
        if (i.hasNext()) {
            playerHp0 = i.next().getInt("\\d+");
            playerMana0 = i.next().getInt("\\d+");
        } else {
            playerHp0 = 50;
            playerMana0 = 500;
        }
    }

    /**
     *
     */
    @Override
    public Long partOne() throws Exception {
        bestResult = new Game();
        bestResult.spentMana = Integer.MAX_VALUE;
        Game g = new Game();
        g.bossHp = bossHp0;
        g.bossDamage = bossDamage0;
        g.hp = playerHp0;
        g.mana = playerMana0;
        playNext2Turns(g, false);
        // bestResult.log.forEach(System.out::println);
        return (long) bestResult.spentMana;
    }

    class Game implements Cloneable {

        int turn = 0;
        int bossHp = 0;
        int bossDamage;
        int hp = 0;
        int mana = 0;
        int armour = 0;
        int shieldTurns = 0;
        int poisonTurns = 0;
        int rechargeTurns = 0;
        int spentMana = 0;
        List<String> log = new ArrayList<>();

        public int spendMana(int n) {
            mana -= n;
            spentMana += n;
            return spentMana;
        }

        public void applyEffects() {
            if (poisonTurns > 0) {
                bossHp -= 3;
                poisonTurns--;
            }
            if (rechargeTurns > 0) {
                mana += 101;
                rechargeTurns--;
            }
            if (shieldTurns > 0) {
                shieldTurns--;
                if (shieldTurns == 0) {
                    armour -= 7;
                }
            }
        }

        public Game clone() {
            try {
                Game game = (Game) super.clone();
                game.log = new ArrayList<>(log);
                return game;
            } catch (CloneNotSupportedException ex) {
                throw new UnsupportedOperationException(ex);
            }
        }
    }

//    Magic Missile costs 53 mana. It instantly does 4 damage.
//    Drain costs 73 mana. It instantly does 2 damage and heals you for 2 hit points.
//    Shield costs 113 mana. It starts an effect that lasts for 6 turns. While it is active, your armor is increased by 7.
//    Poison costs 173 mana. It starts an effect that lasts for 6 turns. At the start of each turn while it is active, it deals the boss 3 damage.
//    Recharge costs 229 mana. It starts an effect that lasts for 5 turns. At the start of each turn while it is active, it gives you 101 new mana.

    private interface Action extends Function<Game, Boolean> {}

    private boolean castMagicMissile(Game g) {
        if (g.mana < 53) return false;
        g.log.add("        - castMagicMissile");
        g.spendMana(53);
        g.bossHp -= 4;
        return true;
    }

    private boolean castDrain(Game g) {
        if (g.mana < 73) return false;
        g.log.add("        - castDrain");
        g.spendMana(73);
        g.bossHp -= 2;
        g.hp += 2;
        return true;
    }

    private boolean castShield(Game g) {
        if (g.mana < 113 || g.shieldTurns > 0) return false;
        g.log.add("        - castShield");
        g.spendMana(113);
        g.shieldTurns = 6;
        g.armour += 7;
        return true;
    }

    private boolean castPoison(Game g) { // damage 3 per round
        if (g.mana < 173 || g.poisonTurns > 0) return false;
        g.log.add("        - castPoison");
        g.spendMana(173);
        g.poisonTurns = 6;
        return true;
    }

    private boolean castRecharge(Game g) { // damage 3 per round
        if (g.mana < 229 || g.rechargeTurns > 0) return false;
        g.log.add("        - castRecharge");
        g.spendMana(229);
        g.rechargeTurns = 5;
        return true;
    }

    private final Action[] ACTIONS = new Action[]{
        this::castShield,
        this::castRecharge,
        this::castPoison,
        this::castDrain,
        this::castMagicMissile,
    };

    private void playNext2Turns(Game g0, boolean hardMode) {
        if (bestResult.spentMana <= g0.spentMana) return; // discard option

        // player turn
        g0.turn++;
        g0.log.add("player: boss hp=%s | player hp=%s mana=%s armor=%s".formatted(g0.bossHp, g0.hp, g0.mana, g0.armour));

        if (hardMode) {
            g0.hp--;
            if (g0.hp <= 0)
                return; // die
        }

        g0.applyEffects();
        if (g0.bossHp <= 0) {
            if (g0.spentMana < bestResult.spentMana) bestResult = g0; // new best option
            return;
        }

        for (Action cast : ACTIONS) {
            Game g = g0.clone();

            if (!cast.apply(g)) continue; // cast spell
            if (g.mana < 0) continue; // invalid option
            if (g.bossHp <= 0) {
                if (g.spentMana < bestResult.spentMana) bestResult = g; // new best option
                continue;
            }

            // boss turn
            g.turn++;
            g.log.add("boss:   boss hp=%s | player hp=%s mana=%s armor=%s".formatted(g.bossHp, g.hp, g.mana, g.armour));
            g.applyEffects();
            if (g.bossHp <= 0) {
                if (g.spentMana < bestResult.spentMana) bestResult = g; // new best option
                continue;
            }

            int damage = Math.max(1, bossDamage0 - g.armour);
            g.log.add("        - boss attack %s".formatted(damage));
            g.hp -= damage;
            if (g.hp <= 0) continue; // die

            // next turn
            playNext2Turns(g, hardMode);
        }
    }

    @Override
    public Long partTwo() throws Exception {
        bestResult = new Game();
        bestResult.spentMana = Integer.MAX_VALUE;
        Game g = new Game();
        g.bossHp = bossHp0;
        g.bossDamage = bossDamage0;
        g.hp = playerHp0;
        g.mana = playerMana0;
        playNext2Turns(g, true);
        // bestResult.log.forEach(System.out::println);
        return (long) bestResult.spentMana;
    }

}
