/*
 * Another brute force solution where we find all valid groups to sum,
 * then we validate that out of these groups we only keep the ones with the lowest amount of elements.
 * Afterward, we verify that we can find valid remaining groups so all groups have the same sum.
 * Last step, map all these valid first groups to their products and return the minimum value.
 *
 * Maybe there is a more efficient approach but the groups of 3 take around 15 seconds to solve on my machine ...
 */
void measureTime(Runnable functionToExecute) {
    long startTime = System.currentTimeMillis();
    functionToExecute.run();
    long endTime = System.currentTimeMillis();
    IO.println(String.format("It took %d ms to run function", endTime - startTime));
}

void main() {
    List<Long> input = Collections.unmodifiableList(getInput());
    final long sum = input.stream().reduce(Long::sum).orElseThrow();
    measureTime(() -> lowestFirstGroupProduct(input, 3, sum)); // 15 seconds of execution time on my system
    measureTime(() -> lowestFirstGroupProduct(input, 4, sum)); // 1 second of execution time on my system
}

private void lowestFirstGroupProduct(List<Long> input, long numberOfGroups, long sum) {
    if (sum % numberOfGroups != 0) {
        throw new IllegalArgumentException(String.format("List has a sum that cannot be divided into groups of %d", numberOfGroups));
    }
    final long groupSum = sum / numberOfGroups;

    List<ArrayList<Long>> firstGroups = new GroupDivider(input, groupSum).findGroup();
    firstGroups.sort(Comparator.comparingInt(ArrayList::size));
    int minSize = firstGroups.getFirst().size();
    firstGroups = firstGroups.stream()
            .filter(l -> l.size() == minSize)
            .filter(l -> isValidGroup(numberOfGroups, groupSum, input, l))
            .toList();
    findLowestProduct(firstGroups);
}

private void findLowestProduct(List<ArrayList<Long>> firstGroups) {
    List<Long> products = firstGroups.stream()
            .map(l -> l.stream().reduce(1L, (a, b) -> a * b))
            .toList();

    long result = products.stream()
            .filter(s -> s > 0) // 10723906903
            .min(Comparator.naturalOrder())
            .orElseThrow();
    IO.println(String.format("Lowest QE is %d", result));
}

private boolean isValidGroup(long numberOfGroups, long sum, List<Long> input, ArrayList<Long> group) {
    if (numberOfGroups < 1) {
        return false;
    }
    if (numberOfGroups == 1) {
        return group.stream().reduce(Long::sum).orElseThrow() == sum;
    }
    List<Long> remaining = input.stream().filter(e -> !group.contains(e)).toList();
    GroupDivider div = new GroupDivider(remaining, sum);
    List<ArrayList<Long>> nextGroups = div.findGroup();
    for (ArrayList<Long> nextGroup : nextGroups) {
        if (isValidGroup(numberOfGroups-1, sum, remaining, nextGroup)) {
            return true;
        }
    }
    return false;
}

private List<Long> getInput() {
    File inputFile = new File("input.txt");
    try (Scanner reader = new Scanner(inputFile)) {

        ArrayList<Long> numbers = new ArrayList<>();
        while (reader.hasNextLine()) {
            numbers.add(Long.parseLong(reader.nextLine()));
        }
        Collections.sort(numbers);
        return numbers.reversed();
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }
}