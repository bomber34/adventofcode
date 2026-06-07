private final static String[] NAUGHTY_SUBSTRINGS_PART_ONE = {"ab", "cd", "pq", "xy"};
private final static Character[] VOWELS = {'a', 'e', 'i', 'o', 'u'};

void main() {
    String[] names = getInput();
    int result = countNiceEntriesFromList(names); // part 1
    IO.println(String.format("List contains %d nice entries", result));

    result = countTrueNiceEntriesFromList(names); // part 2
    IO.println(String.format("List contains %d truly nice entries", result));
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

private static int countNiceEntriesFromList(String[] names) {
    int niceEntries = 0;
    for (String entry : names) {
        if (isNice(entry)) {
            niceEntries++;
        }
    }
    return niceEntries;
}

private static boolean isNice(String entry) {
    boolean hasAtLeastThreeVowels = false;
    boolean hasAtLeastOneDouble = false;
    boolean hasNoNaughtySubString = true;
    int vowelCount = 0;
    int nameLength = entry.length();
    int indexOfLastLetter = nameLength - 1;
    for (int charIndex = 0; charIndex < nameLength; charIndex++) {
        char currentLetter = entry.charAt(charIndex);

        if (!hasAtLeastThreeVowels) {
            vowelCount = isVowel(currentLetter) ? vowelCount + 1 : vowelCount;
            hasAtLeastThreeVowels = vowelCount >= 3;
        }

        if (!hasAtLeastOneDouble && charIndex < indexOfLastLetter) {
            hasAtLeastOneDouble = currentLetter == entry.charAt(charIndex+1);
        }

        if (hasNoNaughtySubString && charIndex < indexOfLastLetter) {
            String candidate = entry.substring(charIndex, charIndex+2);
            hasNoNaughtySubString = !isNaughtyPair(candidate);
        }
    }
    return hasAtLeastThreeVowels && hasAtLeastOneDouble && hasNoNaughtySubString;
}

private static boolean isVowel(char letter) {
    for (char vowel : VOWELS) {
        if (vowel == letter) {
            return true;
        }
    }
    return false;
}

private static boolean isNaughtyPair(String candidate) {
    for (String naughtyString : NAUGHTY_SUBSTRINGS_PART_ONE) {
        if (naughtyString.equals(candidate)) {
            return true;
        }
    }
    return false;
}

// PART TWO
private static int countTrueNiceEntriesFromList(String[] names) {
    int niceEntries = 0;
    for (String entry : names) {
        if (isTrueNice(entry)) {
            niceEntries++;
        }
    }
    return niceEntries;
}

private static boolean isTrueNice(String entry) {
    boolean hasTwoLetterRepeat = false;
    boolean hasLetterDoubleWithOneGapLetter = false;
    int nameLength = entry.length();
    int maxGapIndex = nameLength - 2;
    for (int charIndex = 0; charIndex < maxGapIndex; charIndex++) {
        char currentLetter = entry.charAt(charIndex);
        if (!hasTwoLetterRepeat) {
            String pair = entry.substring(charIndex, charIndex + 2);
            String restOfEntry = entry.substring(charIndex + 2, nameLength);
            hasTwoLetterRepeat = restOfEntry.contains(pair);
        }

        if (!hasLetterDoubleWithOneGapLetter) {
            hasLetterDoubleWithOneGapLetter = currentLetter == entry.charAt(charIndex + 2);
        }
    }
    return hasTwoLetterRepeat && hasLetterDoubleWithOneGapLetter;
}