package tests;

import battle.Fight;
import characters.Boss;
import characters.Wizard;
import spells.ISpell;
import spells.Shield;
import spells.SpellDuration;

public class ApplyTests {

    static class TestBoss extends Boss {

        public TestBoss(String name, int health, int damage) {
            super(name, health, damage);
        }

        public void addSpell(ISpell spell) {
            super.addSpell(spell);
        }

        public boolean isUnderSpell(ISpell spell) {
            return super.isUnderEffectOfSpell(spell.getType());
        }
    }

    void testLastTurnSpells() {
        TestBoss boss = new TestBoss("TestBoss", 100, 1);
        SpellDuration duration = new SpellDuration(2);
        Shield shield = new Shield(duration);
        boss.addSpell(shield);
        boss.applyLastingSpells();
        assert boss.getArmor() != 0;
        assert duration.isActive();
        assert !shield.isSpellOver();
        assert boss.isUnderSpell(shield);
        boss.applyLastingSpells();
        assert boss.getArmor() == 0;
        assert !duration.isActive();
        assert shield.isSpellOver();
        assert !boss.isUnderSpell(shield);
        IO.println("SUCCESS");
    }

    void testExample() {
        // Example 1
        Boss boss = new Boss("TestBoss", 13, 8);
        Wizard wizard = new Wizard("TestWizard", 10, 250);
        /*
         * Poison           --> 173
         * Magic Missile    -->  53
         * ------------------------
         * 226?
         */
        int result = Fight.getLowestAmountOfManaSpent(new Fight(wizard, boss, false));
        assert result == 226;

        // Example 2
        boss = new Boss("TestBoss", 14, 8);
        wizard = new Wizard("TestWizard", 10, 250);
        /*
         * Recharge         --> 229
         * Shield           --> 113
         * Drain            --> 073
         * Poison           --> 173
         * Magic Missile    --> 053
         * -------------------------
         *                      641
         */
        result = Fight.getLowestAmountOfManaSpent(new Fight(wizard, boss, false));
        assert result == 641;
    }

    void main() {
        testLastTurnSpells();
        testExample();
    }
}
