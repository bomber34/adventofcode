package spells;

import characters.AbstractCharacter;

abstract public class LastingSpell implements ISpell {

    protected int _remainingTurns;

    public LastingSpell(int remainingTurns) {
        _remainingTurns = remainingTurns;
    }

    @Override
    public void apply(AbstractCharacter c) {
        _remainingTurns--;
    }

    @Override
    public boolean isSpellOver() {
        return false;
    }

    @Override
    public boolean isInstantEffect() {
        return false;
    }
}
