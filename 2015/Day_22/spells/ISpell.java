package spells;

import characters.AbstractCharacter;

public interface ISpell {
    ESpells getType();
    void apply(AbstractCharacter c);
    boolean isSpellOver();
    boolean isInstantEffect();
}
