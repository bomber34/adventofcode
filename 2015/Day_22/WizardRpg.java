import characters.Boss;
import characters.Wizard;
import spells.ESpells;

void main() {
    Boss boss = getInput();
    partOne(boss);
}

void partOne(Boss boss) {
    // 727 and 886 are too low
    Wizard player = new Wizard("Player", 50, 500);
    PriorityQueue<Fight> queue = new PriorityQueue<>(Comparator.comparingInt(Fight::getTotalManaSpent));
    queue.add(new Fight(player, boss));
    int lowestManaAmount = Integer.MAX_VALUE;
    while (!queue.isEmpty()) {
        Fight f = queue.poll();

        if (f.isGameOver()) {
            if (f.hasPlayerWon()) {
                lowestManaAmount = f.getTotalManaSpent();
                break;
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
    IO.println(String.format("Lowest Amount of used mana to win is %d", lowestManaAmount));
}

private Boss getInput() {
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

    return new Boss("Boss",
            map.getOrDefault("Hit Points", 0),
            map.getOrDefault("Damage", 0));
}