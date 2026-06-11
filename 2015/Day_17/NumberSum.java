private static final int GOAL_NUMBER = 150;

void main() {
    Integer[] input = getInput();
    //testExamplePartOne();
    solvePartOne(input);
    //testExamplePartTwo();
    solvePartTwo(input);
}

private void solvePartOne(Integer[] input) {
    int result = findCombos(input, 0, GOAL_NUMBER);
    IO.println(String.format("There are %d ways to reach %d with the given input file", result, GOAL_NUMBER));
}

private void solvePartTwo(Integer[] input) {
    long result = findMinimumElementCombos(input, GOAL_NUMBER);
    IO.println(String.format("There are %d ways to reach %d with the minimum amount of elements by the given input", result, GOAL_NUMBER));
}

@SuppressWarnings("unused")
private void testExamplePartOne() {
    Integer[] input = new Integer[] {20, 15, 10, 5, 5};
    int result = findCombos(input, 0, 25);
    IO.println(String.format("There are %d ways to reach %d with the given input file", result, 25));
}

@SuppressWarnings("unused")
private void testExamplePartTwo() {
    Integer[] input = new Integer[] {20, 15, 10, 5, 5};
    long result = findMinimumElementCombos(input, 25);
    IO.println(String.format("There are %d ways to reach %d with the minimum amount of elements by the given input", result, 25));
}

private long findMinimumElementCombos(Integer[] list, int goal) {
    ArrayList<ArrayList<Integer>> results = new ArrayList<>();
    findCombos(list, 0, goal, new ArrayList<>(), results);
    results.sort(Comparator.comparingInt(ArrayList::size));
    int minimumLength = results.getFirst().size();
    return results.stream().filter(p -> p.size() == minimumLength).count();
}
private void findCombos(Integer[] list, int index, int goal, ArrayList<Integer> acc, ArrayList<ArrayList<Integer>> result) {
    if (goal == 0) {
        result.add(acc);
        return;
    }

    if (index < 0 || index >= list.length || goal < 0) {
        return;
    }

    ArrayList<Integer> clone = new ArrayList<>(acc);

    clone.add(list[index]);
    findCombos(list, index+1, goal, acc, result);
    findCombos(list, index+1, goal - list[index], clone, result);
}

private int findCombos(Integer[] list, int index, int goal) {
    if (goal == 0) {
        return 1;
    }

    if (index < 0 || index >= list.length || goal < 0) {
        return 0;
    }

    return findCombos(list, index+1, goal)
        + findCombos(list, index+1, goal - list[index]);
}

private Integer[] getInput() {
    File inputFile = new File("input.txt");
    ArrayList<Integer> numbers = new ArrayList<>();
    try (Scanner reader = new Scanner(inputFile)) {
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            numbers.add(Integer.parseInt(line));
        }
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }


    return numbers.stream().sorted().toArray(Integer[]::new);
}