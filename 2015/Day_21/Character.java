public class Character {

    public final String name;
    public final int attack;
    public final int defense;
    public final int maxHealth;
    private int _health;

    public Character(String name, int health, int attack, int defense) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.maxHealth = health;
        _health = health;
    }

    void fullHeal() {
        _health = maxHealth;
    }

    /**
     * Deals damage from attacker to this character
     *
     * @param other that attacks this character
     * @return int damage received
     */
    public int receiveDamage(Character other) {
        int damage = Math.max(1, other.attack - this.defense);
        _health -= damage;
        return damage;
    }

    public boolean isDefeated() {
        return _health <= 0;
    }
}
