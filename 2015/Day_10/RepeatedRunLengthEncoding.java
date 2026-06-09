private static final int NUM_ITERATIONS = 40;
private static final int PART_TWO_EXTRA_ITERATIONS = 10;

void main() {
    Instant start = Instant.now();
    String runLength = "1113122113";
    for (int i = 0; i < NUM_ITERATIONS; i++) {
        runLength = nextRunLength(runLength);
    }
    // result is too long, but normally I'd print it: IO.println(String.format("It took '%d' ms for 40 iterations of RLE", endTime - startTime));
    IO.println("Length of that monstrosity is " + runLength.length());
    IO.println("-------------- PART 2 --------------");
    for (int i = 0; i < PART_TWO_EXTRA_ITERATIONS; i++) {
        runLength = nextRunLength(runLength);
    }
    // result is too long, but normally I'd print it: IO.println(String.format("After %d extra iterations, we have: '%s'", PART_TWO_EXTRA_ITERATIONS, runLength));
    IO.println("Length of that monstrosity is " + runLength.length());

    Instant end = Instant.now();
    IO.println(String.format("For both parts it took %d ms", Duration.between(start, end).toMillis()));
}

private String nextRunLength(String runLength) {
    if (runLength.isEmpty()) {
        return "";
    }
    // we can expect the length of input to increase roughly by two in each iteration
    StringBuilder nextRunLength = new StringBuilder(runLength.length() * 2);

    char currentChar = runLength.charAt(0);
    int counter = 0;
    for (char c : runLength.toCharArray()) {
        if (c == currentChar) {
            counter++;
        } else {
            nextRunLength.append(counter);
            nextRunLength.append(currentChar);
            currentChar = c;
            counter = 1;
        }
    }
    nextRunLength.append(counter);
    nextRunLength.append(currentChar);
    return  nextRunLength.toString();
}
