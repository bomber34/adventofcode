private static final String INPUT_PART_ONE = "vzbxkghb";
private static final char ASCII_INT_A = 'a';
private static final char ASCII_INT_Z = 'z';
private static final char[] ILLEGAL_CHARS = {'i', 'l', 'o'};

void main() {
    String result = findNextPassword(INPUT_PART_ONE);
    IO.println(String.format("Santa's next Password is: %s", result));

    result = findNextPassword(result);
    IO.println(String.format("Santa's next Password after the last one is: %s", result));
}

private static String findNextPassword(String input) {
    char[] chars = input.toCharArray();

    do {
        increaseWord(chars, true, chars.length-1);
    } while(!isValidPassword(chars));

    return new String(chars);
}

private static char[] increaseWord(char[] word, boolean increase, int index) {
    if (!increase) {
        return word;
    } else if (index < 0) {
        throw new IllegalStateException("Cannot increment word anymore");
    }

    if (word[index] == ASCII_INT_Z) {
        word[index] = ASCII_INT_A;
        return increaseWord(word, true, index-1);
    } else {
        word[index]++;
        return increaseWord(word, false, index-1);
    }
}

/**
 * Rules to generate a new and S A F E (lol) password
 * - Must contain only lowercase ASCII alphabetic characters a-z
 * - Must have a run of three consecutive letters (abc, bcd, xyz)
 * - no i, l or o
 * - at least two different letter pair without overlap -> aa is okay but aaa is not but aabb is okay
 */
private static boolean isValidPassword(char[] chars) {
    final int len = chars.length;
    final int maxRunIndex = len - 2;
    final int maxPairIndex = len - 1;
    boolean hasRunOfThree = false;
    ArrayList<RPairIndex> pairs = new ArrayList<>();
    for (int i = 0; i < len; i++) {
        if (isIllegalChar(chars[i])) {
            return false;
        }

        if (!hasRunOfThree && i < maxRunIndex) {
            hasRunOfThree = chars[i] + 1 == chars[i+1] && chars[i] +2 == chars[i+2];
        }
        if (pairs.size() < 2
                && i < maxPairIndex
                && chars[i] == chars[i+1]
                && isPairInRange(pairs, chars[i], i)) {
            pairs.add(new RPairIndex(chars[i], i));
        }
    }
    return pairs.size() > 1 && hasRunOfThree;
}

private static boolean isPairInRange(ArrayList<RPairIndex> pairs, char c, int i) {
    for (RPairIndex pair : pairs) {
        if (pair.character == c && pair.index == i-1) {
            return false;
        }
    }
    return true;
}

private static boolean isIllegalChar(char c) {
    for (char illegalChar : ILLEGAL_CHARS) {
        if (c == illegalChar) {
            return true;
        }
    }
    return false;
}

record RPairIndex(char character, int index) {

}