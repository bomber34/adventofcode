void main() {
    String input = getInput();
    int countedFloors = countFloors(input);
    IO.println(String.format("Santa's floor is %d", countedFloors));
}

private static String getInput() {
    return "TEXT"; // TODO: fetch input of santafloor_input
}

/**
 * Counts floors provided by input according to the following rules:
 * - '(' means +1
 * - ')' means -1
 *
 * @param input String containing countable characters according to rules
 * @return int describing which floor Santa has to go to
 */
private static int countFloors(String input) {

    return input.length(); // TODO: Actually count floors
}

