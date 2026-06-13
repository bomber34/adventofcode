package characters;

import spells.ISpell;

import java.util.List;

public class Wizard extends AbstractCharacter {

    final List<ISpell> _spells;
    private int _armor;
    private int _mana;
    private int _totalManaSpent;

    public Wizard(String name, int health, int mana, List<ISpell> spells) {
        super(name, health, 0);
        _armor = 0;
        _spells = spells;
        _mana = mana;
        _totalManaSpent = 0;
    }


}
