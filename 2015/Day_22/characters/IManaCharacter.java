package characters;

import spells.ESpells;

public interface IManaCharacter {

    void castSpell(ESpells spellType, AbstractCharacter to);

    void applyManaChange(int change);

    void addToTotalManaUsage(int mana);
}
