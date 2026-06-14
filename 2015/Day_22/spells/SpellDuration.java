package spells;

public class SpellDuration {

    private int _remainingTurns;

    public SpellDuration(int remainingTurns) {
        _remainingTurns = remainingTurns;
    }

    public void countDown() {
        _remainingTurns = Math.max(0, _remainingTurns-1);
    }

    public boolean isActive() {
        return _remainingTurns > 0;
    }

    public SpellDuration copy() {
        return new SpellDuration(_remainingTurns);
    }
}
