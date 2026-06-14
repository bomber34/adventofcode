package spells;

import characters.AbstractCharacter;

public class Drain  extends InstantSpell {
    public static final ESpells TYPE = ESpells.DRAIN;

    private final boolean _shouldHeal;

    public Drain(boolean shouldHeal) {
        _shouldHeal = shouldHeal;
    }

    @Override
    public ESpells getType() {
        return TYPE;
    }

    @Override
    public void apply(AbstractCharacter c) {
        if (_shouldHeal) {
            c.heal(2);
        } else {
            c.receiveDamage(2, 0);
        }
    }

}
