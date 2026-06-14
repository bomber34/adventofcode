package characters;

import spells.*;

import java.util.*;

public class Wizard extends AbstractCharacter implements IManaCharacter {
    // Wizard can cast any spells, but if that changes in the future,I am set :)
    private static final ESpells[] CASTABLE_SPELLS = ESpells.values();
    private static final ESpells CHEAPEST_SPELL = Arrays.stream(CASTABLE_SPELLS)
            .min(Comparator.comparingInt(a -> a.Cost))
            .orElseThrow();
    private int _mana;
    private int _totalManaSpent;

    public Wizard(String name, int health, int mana) {
        super(name, health, 0);
        _mana = mana;
        _totalManaSpent = 0;
    }

    public Wizard(Wizard toCopy) {
        super(toCopy);
        _armor = toCopy._armor;
        _mana = toCopy._mana;
        _totalManaSpent = toCopy._totalManaSpent;
    }

    public List<ESpells> getAvailableSpells(AbstractCharacter enemy) {
        ArrayList<ESpells> availableSpells = new ArrayList<>();
        for (ESpells spell : CASTABLE_SPELLS) {
            if (spell.Cost <= _mana
                && !this.isUnderEffectOfSpell(spell)
                && !enemy.isUnderEffectOfSpell(spell)) {
                availableSpells.add(spell);
            }
        }
        return availableSpells;
    }

    @Override
    public void castSpell(ESpells spellType, AbstractCharacter to) {
        applyManaChange(-spellType.Cost);
        addToTotalManaUsage(spellType.Cost);

        switch (spellType) {
            case MAGIC_MISSILE -> new MagicMissile().apply(to);
            case DRAIN -> {
                new Drain(false).apply(to);
                new Drain(true).apply(this);
            }
            case RECHARGE -> this.addSpell(new Recharge(new SpellDuration(spellType.Duration)));
            case POISON -> to.addSpell(new Poison(new SpellDuration(spellType.Duration)));
            case SHIELD -> this.addSpell(new Shield(new SpellDuration(spellType.Duration)));
        }
    }

    @Override
    public void applyManaChange(int change) {
        _mana += change;
    }

    @Override
    public void addToTotalManaUsage(int mana) {
        _totalManaSpent += mana;
    }

    /**
     * Checks if character is unable to fight anymore
     *
     * @return true if character is defeated. By default, checks health
     */
    @Override
     public boolean isDefeated() {
        return super.isDefeated() || _mana < CHEAPEST_SPELL.Cost;
    }

    public int getTotalManaSpent() {
        return _totalManaSpent;
    }
}
