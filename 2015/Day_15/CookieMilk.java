private static final int MAX_TABLE_SPOONS = 100;
private static final int MAX_CALORIES = 500;
private static final Pattern PROPERTIES_REGEX = Pattern.compile("(?<propName>[a-z]+)\\s(?<value>-?[0-9])");

void main() {
    HashMap<String, RIngredient> ingredients = getInput();
    int maxScore = getMaxPoints(ingredients);
    IO.println(String.format("Max Score is %d", maxScore));
    int maxScoreWithCalorieLimit = getMaxPoints(ingredients, MAX_CALORIES);
    IO.println(String.format("Max Score with calorie limit of %d is %d", MAX_CALORIES, maxScoreWithCalorieLimit));
}

private int getMaxPoints(HashMap<String, RIngredient> ingredients) {
    return getMaxPoints(ingredients, -1);
}

private int getMaxPoints(HashMap<String, RIngredient> ingredients, final int calorieLimit) {
    int maxPoints = 0;
    String[] order = ingredients.keySet().toArray(String[]::new);
    int orderLen = order.length;

    ComboIterator it = new ComboIterator(ingredients.size(), MAX_TABLE_SPOONS);
    do {
        int[] combo = it.getCurrentList();
        int capacity = 0;
        int durability = 0;
        int flavor = 0;
        int texture = 0;
        int calories = 0;
        for (int index = 0; index < orderLen; index++) {
            RIngredient ingredient = ingredients.get(order[index]);

            capacity += combo[index] * ingredient.capacity;
            durability += combo[index] * ingredient.durability;
            flavor += combo[index] * ingredient.flavor;
            texture += combo[index] * ingredient.texture;
            calories += combo[index] * ingredient.calories;
        }
        int score = Math.max(0, capacity) * Math.max(0, durability) * Math.max(0, flavor) * Math.max(0, texture);
        if (score > maxPoints && (calorieLimit < 1 || calorieLimit == calories)) {
            maxPoints = score;
        }
    } while(it.next());

    return maxPoints;
}

private HashMap<String, RIngredient> getInput() {
    File inputFile = new File("input.txt");
    HashMap<String, RIngredient> ingredients = new HashMap<>();
    try (Scanner reader = new Scanner(inputFile)) {
        while (reader.hasNextLine()) {
            String line = reader.nextLine();

            String name = line.substring(0, line.indexOf(":"));
            HashMap<String, Integer> propMap = new HashMap<>();
            Matcher match = PROPERTIES_REGEX.matcher(line);
            while (match.find()) {
                String propName = match.group("propName");
                Integer propValue = Integer.parseInt(match.group("value"));
                propMap.put(propName, propValue);
            }
            int capacity = propMap.get("capacity");
            int durability = propMap.get("durability");
            int flavor = propMap.get("flavor");
            int texture = propMap.get("texture");
            int calories = propMap.get("calories");
            ingredients.put(name, new RIngredient(name, capacity, durability, flavor, texture, calories));
        }
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }

    return ingredients;
}

record RIngredient(String name, int capacity, int durability, int flavor, int texture, int calories) {

}