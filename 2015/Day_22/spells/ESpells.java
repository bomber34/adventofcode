package spells;

public enum ESpells {
    MAGIC_MISSILE("Magic Missile", 53, 0),
    DRAIN("Drain", 73, 0),
    SHIELD("Shield", 113, 6),
    POISON("Poison",173, 6),
    RECHARGE("Recharge", 229, 5);

    public final String Name;
    public final int Cost;
    public final int Duration;
    ESpells(String name, int cost, int duration) {
        Name = name;
        Cost = cost;
        Duration = duration;
    }
}
