package spells;

import characters.AbstractCharacter;
import characters.IManaCharacter;

public class Recharge extends LastingSpell {
    public static final ESpells TYPE = ESpells.RECHARGE;
    private static final int RECHARGE_MANA_AMOUNT = 101;

    public Recharge(SpellDuration duration) {
        super(duration);
    }

    public Recharge(Recharge toCopy) {
        super(toCopy._duration.copy());
    }

    @Override
    public ESpells getType() {
        return TYPE;
    }

    @Override
    public void apply(AbstractCharacter c) {
        super.apply(c);
        if (c instanceof IManaCharacter) {
            ((IManaCharacter) c).applyManaChange(RECHARGE_MANA_AMOUNT);
        }
    }
}
