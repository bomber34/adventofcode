package spells;

import characters.AbstractCharacter;

public class Shield extends LastingSpell {
    public static final ESpells TYPE = ESpells.SHIELD;
    private static final int ADD_ARMOR_AMOUNT = 7;
    private int _remainingArmor;

    public Shield(SpellDuration duration) {
        super(duration);
        _remainingArmor = ADD_ARMOR_AMOUNT;
    }

    public Shield(Shield toCopy) {
        super(toCopy._duration.copy());
        _remainingArmor = toCopy._remainingArmor;
    }

    @Override
    public ESpells getType() {
        return TYPE;
    }

    @Override
    public void apply(AbstractCharacter c) {
        super.apply(c);
        c.addArmor(_remainingArmor);
        _remainingArmor = 0;
    }

    @Override
    public void onRemovingSpell(AbstractCharacter c) {
        super.onRemovingSpell(c);
        c.addArmor(-ADD_ARMOR_AMOUNT);
    }
}
