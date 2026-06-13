package spells;

public interface ISpell {
    void activate();
    boolean isActivated();
    void apply(Character c);
}
