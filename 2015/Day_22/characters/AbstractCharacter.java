package characters;

public class AbstractCharacter {
    public final String name;
    public final int maxHeath;
    protected int _damage;
    protected int _health;

    public AbstractCharacter(String name, int maxHealth, int damage) {
        this.name = name;
        this.maxHeath = maxHealth;
        this._damage = damage;
        this._health = maxHealth;
    }

    public boolean isDefeated() {
        return _health <= 0;
    }

    // TODO: apply effects, deal daage?
}
