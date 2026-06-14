package spells;

abstract public class InstantSpell implements ISpell {

    @Override
    public boolean isSpellOver() {
        return true;
    }

    @Override
    public boolean isInstantEffect() {
        return true;
    }
}
