package tests;

import battle.Fight;
import characters.Boss;
import characters.Wizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * If this wasn't just a "solve the task" kind of deal, I would probably put in more effort into making the game really playable
 * by adding an interface for players or for automatic players.
 */
class WizardRpg {

    void main() {
        Boss boss = getInput();
        Boss part2 = new Boss(boss);
        partOne(boss);
        partTwo(part2);
    }

    void partOne(Boss boss) {
        Wizard player = new Wizard("Player", 50, 500);

        int lowestManaAmount = Fight.getLowestAmountOfManaSpent(new Fight(player, boss, false));
        IO.println(String.format("Lowest Amount of used mana to win is %d", lowestManaAmount));
    }

    void partTwo(Boss boss) {
        Wizard player = new Wizard("Player", 50, 500);

        int lowestManaAmount = Fight.getLowestAmountOfManaSpent(new Fight(player, boss, true));
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
}