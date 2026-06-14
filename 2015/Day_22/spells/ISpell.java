package spells;

import characters.AbstractCharacter;

public interface ISpell {
    ESpells getType();
    void apply(AbstractCharacter c);
    boolean isSpellOver();
    boolean isInstantEffect();

    static ISpell copy(ISpell toCopy) {
        return switch (toCopy) {
            case Shield s -> new Shield(s);
            case Recharge r -> new Recharge(r);
            case Poison p -> new Poison(p);
            default -> null;
        };
    }
}
