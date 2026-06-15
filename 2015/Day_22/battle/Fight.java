package battle;

import characters.AbstractCharacter;
import characters.Wizard;
import spells.ESpells;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Fight {

    private final AbstractCharacter _player;
    private final AbstractCharacter _enemy;
    private final boolean _isHardMode;
    private int _turnNumber;

    public Fight(AbstractCharacter char1, AbstractCharacter char2, boolean isHardMode) {
        _player = char1;
        _enemy = char2;
        _turnNumber = 1;
        _isHardMode = isHardMode;
    }

    public Fight(Fight toCopy) {
        _turnNumber = toCopy._turnNumber;
        _player = AbstractCharacter.copy(toCopy._player);
        _enemy = AbstractCharacter.copy(toCopy._enemy);
        _isHardMode = toCopy._isHardMode;
    }

    public static int getLowestAmountOfManaSpent(Fight initialFight) {
        Fight winningFight = runFight(initialFight);
        return winningFight != null ? winningFight.getTotalManaSpent() : Integer.MAX_VALUE;
    }

    private static Fight runFight(Fight initialFight) {
        PriorityQueue<Fight> queue = new PriorityQueue<>(Comparator.comparingInt(Fight::getTotalManaSpent));
        queue.add(initialFight);
        while (!queue.isEmpty()) {
            Fight f = queue.poll();
            f.applyHardMode();
            f.applyLastingSpells();

            if (f.isGameOver()) {
                if (f.hasPlayerWon()) {
                    return f;
                } else if (f.hasPlayerLost()) {
                    continue;
                }
            }

            if (f.isPlayerTurn()) {
                for (ESpells option : f.getPlayerOptions()) {
                    Fight copy = new Fight(f);
                    copy.playerTurn(option);
                    queue.add(copy);
                }
            } else {
                f.enemyTurn();
                queue.add(f);
            }
        }
        return null;
    }

    private void applyHardMode() {
        if (_isHardMode) {
            _player.receiveDamage(1, 0);
        }
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
        if (isGameOver()) {
            return;
        }
        ((Wizard) _player).castSpell(nextMove, _enemy);
        increaseTurnNumber();
    }

    public void enemyTurn() {
        if (isGameOver()) {
            return;
        }
        _player.receiveDamage(_enemy.getDamage(), _player.getArmor());
        increaseTurnNumber();
    }

    public void applyLastingSpells() {
        _player.applyLastingSpells();
        _enemy.applyLastingSpells();
    }
}
