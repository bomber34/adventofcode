import java.awt.*;

private static final int GRID_SIZE = 1000;
private static final String TURN_OFF_TEXT = "turn off";
private static final String TURN_ON_TEXT = "turn on";
private static final String TOGGLE_TEXT = "toggle";

private static final Pattern COORDINATES_REGEX = Pattern.compile("[0-9]{1,3},[0-9]{1,3}");

enum ECommand {
    TURN_OFF,
    TOGGLE,
    TURN_ON
}

record RInstruction(ECommand command, Point start, Point end) {
    public RInstruction {
        Objects.requireNonNull(command);
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
    }
}

void main() {
    String[] instructions = getInput();
    RInstruction[] listOfInstructions = parsECommands(instructions);
    boolean[][] lightMatrix = toggleLights(listOfInstructions);
    int result = countLights(lightMatrix); // part 1
    IO.println(String.format("After following all instructions %d lights are turned on", result));
    int[][] lightLevelMatrix = toggleLightLevels(listOfInstructions);
    result = countLightLevels(lightLevelMatrix);
    IO.println(String.format("After following all instructions the light level is %d ", result));
}

private static String[] getInput() {
    File inputFile = new File("input.txt");
    StringJoiner stringJoiner = new StringJoiner("\n");
    try (Scanner reader = new Scanner(inputFile)) {
        while (reader.hasNextLine()) {
            stringJoiner.add(reader.nextLine());
        }
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }
    return stringJoiner.toString().split("\n");
}

private static int countLights(boolean[][] lightMatrix) {
    int lights = 0;
    for (int row = 0; row < GRID_SIZE; row++) {
        for (int col = 0; col < GRID_SIZE; col++) {
            if (lightMatrix[row][col]) {
                lights++;
            }
        }
    }
    return lights;
}

private static int countLightLevels(int[][] lightMatrix) {
    int totalLightLevels = 0;
    for (int row = 0; row < GRID_SIZE; row++) {
        for (int col = 0; col < GRID_SIZE; col++) {
            totalLightLevels += lightMatrix[row][col];
        }
    }
    return totalLightLevels;
}

private static RInstruction[] parsECommands(String[] input) {
    RInstruction[] instructions = new RInstruction[input.length];
    int index = 0;
    for (String entry : input) {
        ECommand command = getCommand(entry);
        Matcher coordinates = COORDINATES_REGEX.matcher(entry);
        Point start = getPoint(coordinates);
        Point end = getPoint(coordinates);
        instructions[index] = new RInstruction(command, start, end);
        index++;
    }
    return instructions;
}

private static ECommand getCommand(String entry) {
    ECommand command;
    if (entry.contains(TURN_OFF_TEXT)) {
        command = ECommand.TURN_OFF;
    } else if (entry.contains(TURN_ON_TEXT)) {
        command = ECommand.TURN_ON;
    } else if (entry.contains(TOGGLE_TEXT)) {
        command = ECommand.TOGGLE;
    } else {
        throw new IllegalArgumentException("Entry contains no valid command");
    }
    return command;
}

private static Point getPoint(Matcher coordinateMatcher) {
    String coordinates = "";
    if (coordinateMatcher.find()) {
        coordinates = coordinateMatcher.group();
    } else {
        throw new IllegalStateException("No Coordinates were matched");
    }

    Integer[] startCoordinates = Arrays.stream(coordinates.split(","))
            .map(Integer::valueOf).toArray(Integer[]::new);
    return new Point(startCoordinates[0], startCoordinates[1]);
}

private static boolean[][] toggleLights(RInstruction[] instructions) {
    // in Java initial value of boolean is false ... unlike C++ lol
    boolean[][] lightMatrix = new boolean[GRID_SIZE][GRID_SIZE];

    for (RInstruction instruction : instructions) {
        for (int row = instruction.start.y; row <= instruction.end.y; row++) {
            for (int col = instruction.start.x; col <= instruction.end.x; col++) {
                switch (instruction.command) {
                    case TURN_ON -> lightMatrix[row][col] = true;
                    case TURN_OFF -> lightMatrix[row][col] = false;
                    case TOGGLE -> lightMatrix[row][col] = !lightMatrix[row][col];
                    default -> throw new IllegalStateException("Instruction is not supported");
                }
            }
        }
    }
    return lightMatrix;
}

private static int[][] toggleLightLevels(RInstruction[] instructions) {
    // in Java initial value of int is 0 ... unlike C++ lol
    int[][] lightMatrix = new int[GRID_SIZE][GRID_SIZE];

    for (RInstruction instruction : instructions) {
        for (int row = instruction.start.y; row <= instruction.end.y; row++) {
            for (int col = instruction.start.x; col <= instruction.end.x; col++) {
                switch (instruction.command) {
                    case TURN_ON -> lightMatrix[row][col] += 1;
                    case TURN_OFF -> lightMatrix[row][col] = Math.max(0, lightMatrix[row][col]-1);
                    case TOGGLE -> lightMatrix[row][col]+= 2;
                    default -> throw new IllegalStateException("Instruction is not supported");
                }
            }
        }
    }
    return lightMatrix;
}