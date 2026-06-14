import characters.AbstractCharacter;
import characters.Wizard;
import spells.ESpells;

import java.util.List;

public class Fight {

    private final AbstractCharacter _player;
    private final AbstractCharacter _enemy;
    private int _turnNumber;

    public Fight(AbstractCharacter char1, AbstractCharacter char2) {
        _player = char1;
        _enemy = char2;
        _turnNumber = 1;
    }

    public Fight(Fight toCopy) {
        _turnNumber = toCopy._turnNumber;
        _player = AbstractCharacter.copy(toCopy._player);
        _enemy = AbstractCharacter.copy(toCopy._enemy);
    }

    public int getTotalManaSpent() {
        return ((Wizard) _player).getTotalManaSpent();
    }

    public boolean isPlayerTurn() {
        return _turnNumber % 2 == 1;
    }

    public boolean isGameOver() {
        return hasPlayerLost() || hasPlayerWon();
    }

    public boolean hasPlayerWon() {
        return _enemy.isDefeated();
    }

    public boolean hasPlayerLost() {
        return _player.isDefeated();
    }

    public List<ESpells> getPlayerOptions() {
        return ((Wizard) _player).getAvailableSpells(_enemy);
    }

    private void increaseTurnNumber() {
        _turnNumber++;
    }

    public void playerTurn(ESpells nextMove) {
        applyLastingSpells();
        if (isGameOver()) {
            return;
        }
        ((Wizard) _player).castSpell(nextMove, _enemy);
        increaseTurnNumber();
    }

    public void enemyTurn() {
        applyLastingSpells();
        if (isGameOver()) {
            return;
        }
        _player.receiveDamage(_enemy.getDamage(), _player.getArmor());
        increaseTurnNumber();
    }

    private void applyLastingSpells() {
        _player.getActiveSpells().forEachRemaining(spell -> spell.apply(_player));
        _enemy.getActiveSpells().forEachRemaining(spell -> spell.apply(_enemy));
    }
}
