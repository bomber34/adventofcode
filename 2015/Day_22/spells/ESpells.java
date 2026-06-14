package spells;

public enum ESpells {
    MAGIC_MISSILE("Magic Missile", 53),
    DRAIN("Drain", 73),
    SHIELD("Shield", 113),
    POISON("Poison",173),
    RECHARGE("Recharge", 229);

    public final String Name;
    public final int Cost;
    ESpells(String name, int cost) {
        Name = name;
        Cost = cost;
    }
}
