void main() {
    String input = getInput();
    int countedFloors = countFloors(input);
    IO.println(String.format("Santa's floor is %d", countedFloors));
}

private static String getInput() {
    StringBuilder input = new StringBuilder();
    File inputFile = new File("santafloor_input.txt");
    try (Scanner reader = new Scanner(inputFile)) {
        while (reader.hasNextLine()) {
            input.append(reader.nextLine());
        }
    } catch (FileNotFoundException e) {
        //noinspection CallToPrintStackTrace because of simplicity sake in regards to task
        e.printStackTrace();
    }
    return input.toString();
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
    int count = 0;
    for (char c : input.toCharArray()) {
        if (c == '(') {
            count++;
        } else if (c == ')') {
            count--;
        }
    }
    return count;
}

