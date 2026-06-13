private static final Pattern MAP_REGEX = Pattern.compile("(?<key>[A-Za-z]+)\\s=>\\s(?<value>[A-Za-z]+)");

private void measureTime(String name, Runnable call) {
    long startTime = System.currentTimeMillis();
    call.run();
    long endTime = System.currentTimeMillis();
    IO.println(String.format("%s: it took %d ms to find the solution", name, endTime - startTime));
}

void main() {
    // Replacement r = getExample("HOH");
    Replacement r = getInput();
    measureTime("partOne", () -> partOne(r));
    IO.println("");
    measureTime("partTwo", () -> partTwo(r));
}

void partOne(Replacement r) {
    r.calculateAllReplacements();
    IO.println(String.format("Found %d replacements", r.getNumberOfPossibleReplacements()));
}

void partTwo(Replacement r) {
    ReverseReplacer revR = new ReverseReplacer(r.getMap(), r.getText());
    int result = revR.calculateReplacementChainLength("e");
    IO.println(String.format("Shortest chain length with %d replacements", result));
}

@SuppressWarnings("unused")
private Replacement getExample(String exampleString) {
    HashMap<String, ArrayList<String>> map = new HashMap<>();
    ArrayList<String> optionsForH = new ArrayList<>(List.of(new String[]{"HO", "OH"}));
    ArrayList<String> optionsForO = new ArrayList<>(List.of(new String[]{"HH"}));
    map.put("H", optionsForH);
    map.put("O", optionsForO);

    return new Replacement(map, exampleString);
}

private Replacement getInput() {
    File inputFile = new File("input.txt");
    HashMap<String, ArrayList<String>> map = new HashMap<>();
    Replacement input;
    try (Scanner reader = new Scanner(inputFile)) {

        ArrayList<String> lines = new ArrayList<>();
        while (reader.hasNextLine()) {
            lines.add(reader.nextLine());
        }

        for (int i = 0; i < lines.size()-1; i++) {
            String line = lines.get(i);
            Matcher x = MAP_REGEX.matcher(line);
            while (x.find()) {
                String key = x.group("key");
                String value = x.group("value");

                map.putIfAbsent(key, new ArrayList<>());
                map.get(key).add(value);
            }
        }
        input = new Replacement(map, lines.getLast());
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }

    return input;
}
