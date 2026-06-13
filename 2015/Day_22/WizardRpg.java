import characters.Boss;

void main() {
    Boss boss = getInput();
    IO.println("TODO: DO STUFF");
}
// TODO: Actual class structure around chracters
// TODO: Magic System
// TODO: Priority Queue or something to find lowest mana path


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