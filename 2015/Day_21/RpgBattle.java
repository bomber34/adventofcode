private static final RItem NOTHING = new RItem("nothing", 0, 0, 0);

private static final List<RItem> WEAPONS = List.of(
        new RItem("Dagger", 8, 4, 0),
        new RItem("Shortsword", 10, 5, 0),
        new RItem("Warhammer", 25, 6, 0),
        new RItem("Longsword", 40, 7, 0),
        new RItem("Greataxe", 74, 8, 0)
);

private static final List<RItem> ARMOR = List.of(
        NOTHING,
        new RItem("Leather", 13, 0, 1),
        new RItem("Chainmail", 31, 0, 2),
        new RItem("Splintmail", 53, 0, 3),
        new RItem("Bandedmail", 75, 0, 4),
        new RItem("Platemail", 102, 0, 5)
);

private static final List<RItem> RINGS = List.of(
        NOTHING,
        NOTHING,
        new RItem("Dmg1", 25, 1, 0),
        new RItem("Dmg2", 50, 2, 0),
        new RItem("Dmg3", 100, 3, 0),
        new RItem("Def1", 20, 0, 1),
        new RItem("Def2", 40, 0, 2),
        new RItem("Def3", 80, 0, 3)
);

static class ItemSelector {

    private int weaponIndex = 0;
    private int armorIndex = 0;
    private int ringOneIndex = 0;
    private int ringTwoIndex = 1;

    public RItem getStats() {
        RItem weapon = WEAPONS.get(weaponIndex);
        RItem armor = ARMOR.get(armorIndex);
        RItem ringOne = RINGS.get(ringOneIndex);
        RItem ringTwo = RINGS.get(ringTwoIndex);

        int totalCost = weapon.cost() + armor.cost() + ringOne.cost() + ringTwo.cost();
        int totalDmg = weapon.damage() + armor.damage() + ringOne.damage() + ringTwo.damage();
        int totalDef = weapon.armor() + armor.armor() + ringOne.armor() + ringTwo.armor();
        return new RItem("Stats", totalCost, totalDmg, totalDef);
    }

    public void next() {
        weaponIndex = (weaponIndex + 1) % WEAPONS.size();

        if (weaponIndex == 0) {
            armorIndex = (armorIndex + 1) % ARMOR.size();

            if (armorIndex == 0) {
                ringTwoIndex = (ringTwoIndex + 1) % RINGS.size();
                if (ringTwoIndex == 0) {
                    ringOneIndex = (ringOneIndex + 1) % RINGS.size();
                    ringTwoIndex = ringOneIndex + 1;
                }
            }
        }
    }

    public boolean hasNext() {
        return weaponIndex != (WEAPONS.size()-1)
                || armorIndex != (ARMOR.size()-1)
                || ringOneIndex != (RINGS.size()-2)
                || ringTwoIndex != (RINGS.size()-1);
    }
}

/**
 * You must buy exactly one weapon; no dual-wielding.
 * Armor is optional, but you can't use more than one.
 * You can buy 0-2 rings
 */
void main() {
    final Character boss = getInput();
    partOne(boss);
    partTwo(boss);
}

// Least amount of gold and still win
void partOne(final Character boss) {
    RItem bestItem = new RItem("", Integer.MAX_VALUE, 0, 0);
    ItemSelector it = new ItemSelector();
    while (it.hasNext()) {
        RItem stats = it.getStats();
        Character player = new Character("Player", 100, stats.damage(), stats.armor());
        if (winsAgainstBoss(player, boss) && stats.cost() < bestItem.cost()) {
            bestItem = stats;
        }
        boss.fullHeal();
        it.next();
    }

    IO.println(String.format("Lowest cost to defeat boss is %d with %d dmg and %d def", bestItem.cost(), bestItem.damage(), bestItem.armor()));
}

// most amount of gold and still lose
void partTwo(final Character boss) {
    RItem worstItem = new RItem("", Integer.MIN_VALUE, 0, 0);
    ItemSelector it = new ItemSelector();
    while (it.hasNext()) {
        RItem stats = it.getStats();
        Character player = new Character("Player", 100, stats.damage(), stats.armor());
        if (!winsAgainstBoss(player, boss) && stats.cost() > worstItem.cost()) {
            worstItem = stats;
        }
        boss.fullHeal();
        it.next();
    }

    IO.println(String.format("Highest cost to lose against boss is %d with %d dmg and %d def", worstItem.cost(), worstItem.damage(), worstItem.armor()));
}

boolean winsAgainstBoss(Character player, Character boss) {
    boolean isPlayerTurn = true;
    while(!player.isDefeated() && !boss.isDefeated()) {
        if (isPlayerTurn) {
            boss.receiveDamage(player);
        } else {
            player.receiveDamage(boss);
        }
        isPlayerTurn = !isPlayerTurn;
    }
    return boss.isDefeated();
}

private Character getInput() {
    File inputFile = new File("input.txt");
    HashMap<String, Integer> map = new HashMap<>();
    Pattern pattern = Pattern.compile("(?<prop>.+):\\s(?<value>[0-9]+)");
    try (Scanner reader = new Scanner(inputFile)) {
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String key = matcher.group("prop");
                Integer value = Integer.parseInt(matcher.group("value"));
                map.put(key, value);
            }
        }
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }

    return new Character("Boss", map.get("Hit Points"), map.get("Damage"), map.get("Armor"));
}