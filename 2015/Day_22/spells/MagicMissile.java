package spells;

import characters.AbstractCharacter;

public class MagicMissile extends InstantSpell {
    public static final ESpells TYPE = ESpells.MAGIC_MISSILE;

    @Override
    public ESpells getType() {
        return TYPE;
    }

    @Override
    public void apply(AbstractCharacter c) {
        c.receiveDamage(4, 0);
    }
}
