package spells;

import characters.AbstractCharacter;

public class Poison extends LastingSpell {
    public static final ESpells TYPE = ESpells.POISON;

    private static final int POISON_DAMAGE = 3;
    public Poison(SpellDuration duration) {
        super(duration);
    }

    public Poison(Poison toCopy) {
        super(toCopy._duration.copy());
    }

    @Override
    public ESpells getType() {
        return TYPE;
    }

    @Override
    public void apply(AbstractCharacter c) {
        super.apply(c);
        c.receiveDamage(POISON_DAMAGE, 0);
    }
}
