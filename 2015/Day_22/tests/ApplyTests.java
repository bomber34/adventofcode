package tests;

import characters.Boss;
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

    void main() {
        testLastTurnSpells();
    }
}
