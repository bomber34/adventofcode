
void main() {
    ArrayList<Integer[]> listOfDimensions = getInput();
    int presentPaperAmount = calculateTotalWrapperAmount(listOfDimensions); // part 1
    IO.println("Wrapper paper amount in square feet needed: " + presentPaperAmount);
    int ribbonAmount = calculateTotalRibbonAmount(listOfDimensions); // part 2
    IO.println("Ribbon amount in feet needed: " + ribbonAmount);
}

private static ArrayList<Integer[]> getInput() {
    ArrayList<Integer[]> listOfDimensions = new ArrayList<>();

    File inputFile = new File("input.txt");
    try (Scanner reader = new Scanner(inputFile)) {
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            Integer[] dimensions = Arrays.stream(line.split("x"))
                    .map(Integer::valueOf)
                    .toArray(Integer[]::new);
            if (dimensions.length != 3) {
                throw new IllegalArgumentException("Line: '" + line + "' does not have 3 dimensions");
            }
            listOfDimensions.add(dimensions);
        }
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }
    return listOfDimensions;
}

private static int calculateTotalWrapperAmount(ArrayList<Integer[]> listOfDimensions) {
    return listOfDimensions.stream()
            .map((dimensions -> calculateWrapperAmount(dimensions)))
            .reduce(0, Integer::sum);
}

private static void validatePresentDimensions(Integer[] dimensions) {
    if (dimensions.length != 3) {
        throw new IllegalArgumentException("Dimensions are not 3 but " + dimensions.length);
    }
}

/**
 * Rule for amount: multiply all dimensions with each other i.e. length * width + length * height + width * height
 * Multiply by 2 and add the smallest pair to the total.
 * Example: 2x3x4 -> 2 * (2 * 3 + 2 * 4 + 3 * 4) + (2 * 3) = 58
 *
 * @param dimensions in length, width and height
 * @return amount of wrapping paper needed to wrap the present
 */
private static int calculateWrapperAmount(Integer[] dimensions) {
    validatePresentDimensions(dimensions);
    int lengthWidth = dimensions[0] * dimensions[1];
    int lengthHeight = dimensions[0] * dimensions[2];
    int widthHeight = dimensions[1] * dimensions[2];
    int smallestSide = Math.min(lengthWidth, Math.min(lengthHeight, widthHeight));

    return 2 * (lengthWidth + lengthHeight + widthHeight) + smallestSide;
}

private static int calculateTotalRibbonAmount(ArrayList<Integer[]> listOfDimensions) {
    return listOfDimensions.stream()
            .map((dimensions -> calculateRibbonAmount(dimensions)))
            .reduce(0, Integer::sum);
}

/**
 * Ribbon amount is calculated by these rules:
 * Smallest perimeter of any one face + ribbon needed for the bow tie
 * Example with 2x3x4 dimensions:
 * (2 * 2 + 2 * 3) + (2 * 3 * 4)
 * @param dimensions in length, width and height
 * @return amount of ribbon needed to wrap the bow tie
 */
private static int calculateRibbonAmount(Integer[] dimensions) {
    validatePresentDimensions(dimensions);
    int lengthWidth = dimensions[0] + dimensions[1];
    int lengthHeight = dimensions[0] + dimensions[2];
    int widthHeight = dimensions[1] + dimensions[2];
    int smallestSide = Math.min(lengthWidth, Math.min(lengthHeight, widthHeight));
    int cubicVolume = Arrays.stream(dimensions).reduce(1, (acc, dim) -> acc * dim);

    return 2 * smallestSide + cubicVolume;
}