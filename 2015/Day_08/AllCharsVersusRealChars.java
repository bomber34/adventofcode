private static final char BACKSLASH = '\\';
private static final char QUOTE = '\"';
private static final char HEXADECIMAL_SIGN = 'x';
private static final int BASE_HEXADECIMAL = 16;

void main() {
    String[] lines = getInput();
    int sumAllCharsOfInput = Arrays.stream(lines)
            .map(String::length)
            .reduce(0, Integer::sum);

    part1(sumAllCharsOfInput, lines);
    part2(sumAllCharsOfInput, lines);
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

private static void part1(int sumAllCharsOfInput, String[] lines) {

    ArrayList<String> unescapedLines = new ArrayList<>(lines.length);
    for (String line : lines) {
        unescapedLines.add(decodeString(line));
    }
    int unescapedChars = unescapedLines.stream().map(String::length).reduce(0, Integer::sum);
    int result = sumAllCharsOfInput - unescapedChars;

    IO.println(String.format("There are %d in total, %d escaped chars. Therefore the diff is %d", sumAllCharsOfInput, unescapedChars, result));
}

private static void part2(int sumAllCharsOfInput, String[] lines) {

    ArrayList<String> encodedLines = new ArrayList<>(lines.length);
    for (String line : lines) {
        encodedLines.add(encodeString(line));
    }
    int numberEncodedChars = encodedLines.stream().map(String::length).reduce(0, Integer::sum);
    int result = numberEncodedChars - sumAllCharsOfInput;

    IO.println(String.format("There are %d in total, %d unescaped chars. Therefore the diff is %d", sumAllCharsOfInput, numberEncodedChars, result));
}

/**
 * For every character that was escaped, we use the intended character and remove the quotes
 *
 * @param text that should be unescaped
 * @return String displaying the intended version of the original
 */
private static String decodeString(String text) {
    final int lastIndex = text.length() - 1;
    StringBuilder strBuilder = new StringBuilder();
    // Starting at index 1, because every line starts with a quote
    for (int charIndex = 1; charIndex < lastIndex; charIndex++) {
        char character = text.charAt(charIndex);
        if (character != BACKSLASH) {
            strBuilder.append(character);
            continue;
        }
        charIndex++;
        character = text.charAt(charIndex);
        if (character != HEXADECIMAL_SIGN) {
            strBuilder.append(character);
        } else {
            charIndex++;
            char firstHexadecimalSymbol = text.charAt(charIndex);
            charIndex++;
            char secondHexadecimalSymbol = text.charAt(charIndex);
            String hexString = new String(new char[]{firstHexadecimalSymbol, secondHexadecimalSymbol});
            int hexVal = Integer.parseInt(hexString, BASE_HEXADECIMAL);
            strBuilder.append((char) hexVal);
        }
    }
    return strBuilder.toString();
}

/**
 * For every character that can be escaped, we add another backslash, and we wrap the line again with quotes
 *
 * @param text that should be escaped again
 * @return String displaying another layer of escaped characters
 */
private static String encodeString(String text) {
    final int lastIndex = text.length();
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append(QUOTE);
    for (int charIndex = 0; charIndex < lastIndex; charIndex++) {
        char character = text.charAt(charIndex);
        if (character == BACKSLASH || character == QUOTE) {
            strBuilder.append(BACKSLASH);
        }
        strBuilder.append(character);
    }
    strBuilder.append(QUOTE);
    return strBuilder.toString();
}