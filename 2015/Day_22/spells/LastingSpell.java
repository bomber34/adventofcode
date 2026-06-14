package spells;

import characters.AbstractCharacter;

abstract public class LastingSpell implements ILastingSpell {

    protected final SpellDuration _duration;

    public LastingSpell(SpellDuration duration) {
        _duration = duration;
    }

    @Override
    public void apply(AbstractCharacter c) {
        _duration.countDown();
    }

    @Override
    public boolean isSpellOver() {
        return !_duration.isActive();
    }

    @Override
    public boolean isInstantEffect() {
        return false;
    }

    @Override
    public void onRemovingSpell(AbstractCharacter c) {
        // do nothing
    };
}
