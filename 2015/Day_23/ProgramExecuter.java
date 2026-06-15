private static final boolean IS_DEBUG = false;

void main() {
    List<Instruction> input = Collections.unmodifiableList(getInput());
    partOne(input);
    partTwo(input);
}

private void partOne(List<Instruction> instructions) {
    HashMap<String, Integer> varMap = new HashMap<>();
    varMap.put("a", 0);
    varMap.put("b", 0);
    executeProgram(instructions, varMap);

    IO.println(String.format("Value of b after running program is %d", varMap.get("b")));
}

private void partTwo(List<Instruction> instructions) {
    HashMap<String, Integer> varMap = new HashMap<>();
    varMap.put("a", 1);
    varMap.put("b", 0);
    executeProgram(instructions, varMap);

    IO.println(String.format("Value of b after running program is %d", varMap.get("b")));
}

private void executeProgram(List<Instruction> instructions, HashMap<String, Integer> varMap) {
    final int terminationAddress = instructions.size();
    int currentAddress = 0;
    while (currentAddress < terminationAddress) {
        Instruction currentInstruction = instructions.get(currentAddress);
        int nextAddress = currentInstruction.nextProgramAddress(varMap, currentAddress);
        if (IS_DEBUG) {
            IO.println(String.format("After performing %s the instruction address changed from %d to %d", currentInstruction, currentAddress, nextAddress));
        }
        currentAddress = nextAddress;
    }
}

private List<Instruction> getInput() {
    File inputFile = new File("input.txt");
    List<Instruction> instructions = new ArrayList<>();
    try (Scanner reader = new Scanner(inputFile)) {
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            int indexOfFirstSpace = line.indexOf(" ");
            String instr = line.substring(0, indexOfFirstSpace);
            String rest = line.substring(indexOfFirstSpace + 1);
            Object[] args = getTokens(rest);
            instructions.add(new Instruction(EInstruction.valueOf(instr.toUpperCase()), args[0], args[1]));
        }
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }

    return instructions;
}

private Object[] getTokens(String args) {
    String[] tokens = args.split(",");
    Object[] varArgs = new Object[2];
    int index = 0;
    for (String token : tokens) {
        try {
            varArgs[index] = Integer.parseInt(token.trim());
        } catch (Exception e) {
            varArgs[index] = token.trim();
        }
        index++;
    }

    if (varArgs[1] == null) {
        varArgs[1] = 0;
    }

    return varArgs;
}
