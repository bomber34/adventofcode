package spells;

import characters.AbstractCharacter;

public interface ILastingSpell extends ISpell {
    void onRemovingSpell(AbstractCharacter c);
}
