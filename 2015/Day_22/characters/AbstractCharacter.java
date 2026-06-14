package characters;

import spells.ISpell;
import spells.LastingSpell;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractCharacter {
    public final String name;
    public final int maxHealth;
    protected int _damage;
    protected int _armor;
    protected int _health;
    protected final Set<ISpell> _receivedSpells;

    public AbstractCharacter(String name, int maxHealth, int damage) {
        this.name = name;
        this.maxHealth = maxHealth;
        this._damage = damage;
        this._health = maxHealth;
        this._armor = 0; // default value
        this._receivedSpells = new HashSet<>();
    }

    public int getArmor() {
        return _armor;
    }

    public AbstractCharacter(AbstractCharacter toCopy) {
        this.name = toCopy.name;
        this.maxHealth = toCopy.maxHealth;
        this._damage = toCopy._damage;
        this._health = toCopy._health;
        _receivedSpells =
    }

    private Set<ISpell> copyReceivedSpells(AbstractCharacter toCopy) {
        HashSet<ISpell> copiedSpells = new HashSet<>();
        for (ISpell spell : toCopy._receivedSpells) {

        }
        return new HashSet<>(toCopy._receivedSpells);
    }

    public void addArmor(int armor) {
        _armor += armor;
    }

    /**
     * Checks if character is unable to fight anymore
     *
     * @return true if character is defeated. By default, checks health
     */
    public boolean isDefeated() {
        return _health <= 0;
    }

    /**
     * Subtracts health from character, min damage is always 1
     *
     * @param receivedDamage int that should be subtracted
     * @param armor int that mitigates the damage
     */
    public void receiveDamage(int receivedDamage, int armor) {
        _health -= Math.max(1, receivedDamage - armor);
    }

    public void heal(int healthPoints) {
        _health += healthPoints;
    }

    public void applyEffect(ISpell spell) {
        spell.apply(this);

        if (spell.isSpellOver()) {
            removeSpell(spell);
        }
    }

    private void removeSpell(ISpell spell) {
        if (spell instanceof LastingSpell) {
            ((LastingSpell) spell).onRemovingSpell(this);
        }
        _receivedSpells.remove(spell);
    }
}
