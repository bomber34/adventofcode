private static final int GRID_SIZE = 100;
private static final int ITERATIONS = 100;
private static final char ON = '#';
private static final char OFF = '.';
private static final boolean PRINT_INFO = false;

@SuppressWarnings("CommentedOutCode")
void main() {
    char[][] input = getInput();
    // example(false);
    // example(true);
    part(input, false);
    part(input, true);
}

private void part(char[][] input, boolean hasDefectCorners) {
    char[][] animationState = animateGameOfLight(input, hasDefectCorners);
    int lightsOn = countLightsOn(animationState);
    IO.println(String.format("Number of lights after %d iterations: %d", ITERATIONS, lightsOn));
}

@SuppressWarnings("unused")
private void example(boolean hasDefectCorners) {
    char[][] currentState = new char[][] {
            ".#.#.#".toCharArray(),
            "...##.".toCharArray(),
            "#....#".toCharArray(),
            "..#...".toCharArray(),
            "#.#..#".toCharArray(),
            "####..".toCharArray()
    };
    int i = 0;
    int exampleIterations = 5;

    if (hasDefectCorners) {
        turnOnCorners(currentState);
    }

    do {
        IO.println(String.format("After %d iterations:", i));
        printGrid(currentState);
        currentState = updateState(currentState, hasDefectCorners);
        i++;
    } while (i < exampleIterations);

}

private char[][] animateGameOfLight(char[][] input, boolean hasDefectCorners) {
    char[][] animation = copyState(input);
    if (hasDefectCorners) {
        turnOnCorners(animation);
    }
    for (int i = 0; i < ITERATIONS; i++) {
        if (PRINT_INFO) {
            int lightsOn = countLightsOn(animation);
            IO.println(String.format("In iteration %d there are %d lights on", i, lightsOn));
        }

        animation = updateState(animation, hasDefectCorners);

    }
    return animation;
}

private char[][] copyState(char[][] state) {
    final int rowSize = state.length;
    final int colSize = state[0].length;
    char[][] copy = new char[rowSize][colSize];
    for (int r = 0; r < rowSize; r++) {
        System.arraycopy(state[r], 0, copy[r], 0, colSize);
    }
    return copy;
}

private char[][] updateState(char[][] currentState, boolean hasDefectCorners) {
    char[][] nextState = copyState(currentState);
    final int rowSize = currentState.length;
    final int colSize = currentState[0].length;

    for (int row = 0; row < rowSize; row++) {
        for (int col = 0; col < colSize; col++) {
            int turnedOnNeighbors = countTurnedOnNeighbors(currentState, row, col);
            if (currentState[row][col] == ON) {
                handleTurnedOnLight(nextState, row, col, turnedOnNeighbors);
            } else if (currentState[row][col] == OFF) {
                handleTurnedOffLight(nextState, row, col, turnedOnNeighbors);
            } else {
                throw new IllegalStateException(String.format("Grid has illegal symbol at (%d %d) which is %s", row, col, currentState[row][col]));
            }
        }
    }

    if (hasDefectCorners) {
        turnOnCorners(nextState);
    }

    return nextState;
}

private void turnOnCorners(char[][] state) {
    final int rowStart = 0;
    final int colStart = 0;
    final int rowEnd = state.length-1;
    final int colEnd = state[0].length-1;
    state[rowStart][colStart] = ON;
    state[rowStart][colEnd] = ON;
    state[rowEnd][colStart] = ON;
    state[rowEnd][colEnd] = ON;
}

private void handleTurnedOnLight(char[][] nextState, int row, int col, int turnedOnNeighbors) {
    if (amountNeededToStayOn(turnedOnNeighbors)) {
        nextState[row][col] = ON;
    } else {
        nextState[row][col] = OFF;
    }
}

private void handleTurnedOffLight(char[][] nextState, int row, int col, int turnedOnNeighbors) {
    if (amountNeededToTurnOn(turnedOnNeighbors)) {
        nextState[row][col] = ON;
    } else {
        nextState[row][col] = OFF;
    }
}

private boolean amountNeededToTurnOn(int turnedOnNeighbors) {
    return turnedOnNeighbors == 3;
}

private boolean amountNeededToStayOn(int turnedOnNeighbors) {
    return turnedOnNeighbors >= 2 && turnedOnNeighbors <= 3;
}

private int countTurnedOnNeighbors(char[][] currentState, int row, int col) {
    int counter = 0;

    for (int rIdx = row-1; rIdx <= row+1; rIdx++) {
        for (int cIdx = col-1; cIdx <= col+1; cIdx++) {
            if (rIdx == row && cIdx == col) {
                continue;
            }

            if (isInBound(currentState, rIdx, cIdx) && currentState[rIdx][cIdx] == ON) {
                counter++;
            }
        }
    }

    return counter;
}

private boolean isInBound(char[][] currentState, int row, int col) {
    return row >= 0 && row < currentState.length && col >= 0 && col < currentState[0].length;
}

private int countLightsOn(char[][] animationState) {
    int counter = 0;
    for (int row = 0; row < GRID_SIZE; row++) {
        for (int col = 0; col < GRID_SIZE; col++) {
            if (animationState[row][col] == ON) {
                counter++;
            }
        }
    }
    return counter;
}

private void printGrid(char[][] grid) {
    StringBuilder sb = new StringBuilder((grid.length+1)*grid[0].length);
    final int colSize = grid[0].length;
    for (char[] chars : grid) {
        for (int col = 0; col < colSize; col++) {
            sb.append(chars[col]);
        }
        sb.append(System.lineSeparator());
    }
    IO.println(sb.toString());
}

private char[][] getInput() {
    File inputFile = new File("input.txt");
    char[][] input = new char[GRID_SIZE][GRID_SIZE];

    try (Scanner reader = new Scanner(inputFile)) {
        int lineNumber = 0;
        while (reader.hasNextLine()) {
            char[] line = reader.nextLine().toCharArray();
            System.arraycopy(line, 0, input[lineNumber], 0, GRID_SIZE);
            lineNumber++;
        }
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }

    return input;
}