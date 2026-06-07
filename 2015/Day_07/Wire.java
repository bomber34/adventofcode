private static final HashMap<String, Integer> WIRE_VALUES = new HashMap<>();
private static final int MAX_UNSIGNED_SHORT_VALUE = ((int) Short.MAX_VALUE) * 2 + 1;
private static final int IGNORE_VALUE = -1;

enum EOperations {
    SET,
    NOT,
    AND,
    OR,
    LSHIFT,
    RSHIFT
}

private final static String[] BINARY_OPERATION_STRINGS = {
        EOperations.AND.name(),
        EOperations.OR.name(),
        EOperations.LSHIFT.name(),
        EOperations.RSHIFT.name()
};

record RWireInput(int leftCable, EOperations op, int rightCable) {

}

void main() {
    String[] textCommands = getInput();
    fillHashMap(textCommands);
    int result = WIRE_VALUES.getOrDefault("a", IGNORE_VALUE);
    IO.println(String.format("Part 1 -> Wire a is: %d", result)); // part 1

    // part 2
    fillHashMap(getInputPartTwo(textCommands));
    result = WIRE_VALUES.getOrDefault("a", IGNORE_VALUE);
    IO.println(String.format("PART 2 -> Wire a is: %d", result)); // part 2
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

/*
 * to be honest, the site's instructions were a little bit unclear on that one
 * I thought we restart the run but already have value of b in the map.
 * But instead they wanted us to override the one line that sets a specific value to b with the result from part 1
 */
private static String[] getInputPartTwo(String[] textCommands) {
    int wireA = WIRE_VALUES.getOrDefault("a", IGNORE_VALUE);
    WIRE_VALUES.replaceAll((_, _) -> IGNORE_VALUE);
    String[] modifiedCommands = new String[textCommands.length];
    for (int index = 0; index < textCommands.length; index++) {
        String cmd = textCommands[index];
        if (cmd.equals("14146 -> b")) {
            cmd = String.format("%d -> b", wireA);
        }
        modifiedCommands[index] = cmd;
    }
    return modifiedCommands;
}

private static void fillHashMap(String[] input) {
    ArrayDeque<String> queue = new ArrayDeque<>(Arrays.stream(input).toList());
    while(!queue.isEmpty()) {
        String line = queue.pollFirst();
        String[] tokens = line.split(" ");
        RWireInput wireInput = parseInput(tokens);
        if (wireInput.leftCable >= 0 && wireInput.rightCable >= 0) {
            WIRE_VALUES.put(tokens[tokens.length-1], calculateInput(wireInput));
        } else {
            queue.addLast(line);
        }
    }
}

private static int calculateInput(RWireInput input) {
    int result;
    switch (input.op) {
        case EOperations.SET -> result = input.leftCable;
        case EOperations.NOT -> result = ~input.leftCable & 0x0000FFFF;
        case EOperations.AND -> result = input.leftCable & input.rightCable;
        case EOperations.OR -> result = input.leftCable | input.rightCable;
        case EOperations.LSHIFT -> result = (input.leftCable << input.rightCable) & 0x0000FFFF;
        case EOperations.RSHIFT -> result = (input.leftCable >> input.rightCable) & 0x0000FFFF;
        default -> throw new IllegalStateException("Illegal operation");
    }
    return result;
}

private static RWireInput parseInput(String[] input) {
    int leftValue;
    int rightValue;
    EOperations op;
    if (EOperations.NOT.name().equals(input[0])) {
        leftValue = parseValue(input[1]);
        rightValue = MAX_UNSIGNED_SHORT_VALUE;
        op = EOperations.NOT;
    } else if (isBinaryOp(input[1])){
        leftValue = parseValue(input[0]);
        rightValue = parseValue(input[2]);
        op = EOperations.valueOf(input[1]);
    } else {
        leftValue = parseValue(input[0]);
        rightValue = MAX_UNSIGNED_SHORT_VALUE;
        op = EOperations.SET;
    }
    return new RWireInput(leftValue, op, rightValue);
}

private static boolean isBinaryOp(String op) {
    for (String binaryOp : BINARY_OPERATION_STRINGS) {
        if (binaryOp.equals(op)) {
            return true;
        }
    }
    return false;
}

private static int parseValue(String valueStr) {
    int value = IGNORE_VALUE;
    try {
        value = Integer.parseInt(valueStr);
    } catch (Exception e) {
        if (WIRE_VALUES.containsKey(valueStr)) {
            value = WIRE_VALUES.get(valueStr);
        } else {
            WIRE_VALUES.put(valueStr, IGNORE_VALUE);
        }
    }
    return value;
}