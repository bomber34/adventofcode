@SuppressWarnings("SpellCheckingInspection")
private static final String INPUT = "yzbqklnj";
private static final String LEADING_ZEROES_PART_ONE = "00000";
private static final String LEADING_ZEROES_PART_TWO = "000000";
private static final int MAX_NUMBER_ATTEMPT = Integer.MAX_VALUE; // to avoid endless loops

void main() throws NoSuchAlgorithmException {
    IO.println(String.format("Part1: Password is '%s'", findPassword(LEADING_ZEROES_PART_ONE)));
    IO.println(String.format("Part2: Password is '%s'", findPassword(LEADING_ZEROES_PART_TWO)));
}

private static int findPassword(final String searchedPrefix) throws NoSuchAlgorithmException {
    StringBuilder md5hashInput = new StringBuilder(INPUT);
    MessageDigest md5Hasher = MessageDigest.getInstance("MD5");
    int number = 1;
    while (number < MAX_NUMBER_ATTEMPT) {
        md5hashInput.append(number);
        String attemptedPassword = md5hashInput.toString();
        byte[] bytes = md5Hasher.digest(attemptedPassword.getBytes());
        String hexValue = byteArrayToHex(bytes);

        if (hexValue.startsWith(searchedPrefix)) {
            break;
        }

        number++;
        md5hashInput.setLength(0);
        md5hashInput.append(INPUT);
    }

    return number;
}

// I'm not bothering with building this tiny project with DatatypeConverter that is not included in the standard lib
private static String byteArrayToHex(byte[] input) {
    StringBuilder hexBuilder = new StringBuilder(input.length*2);
    for (byte b : input) {
        hexBuilder.append(byteToHex(b));
    }
    return hexBuilder.toString();
}

/*
 * Using Integer.toHexString has the issue that negative numbers cause issues
 * due to the fact that converting a negative byte to an int will lead to a negative integer as well.
 * So we use some bit manipulation as the lower nibble will always be treated as a positve number.
 */
private static String byteToHex(byte input) {
    int lowerNibble = input & 0x0F;
    int upperNibble = (input >> 4) & 0b00001111;
    return Integer.toHexString(upperNibble) + Integer.toHexString(lowerNibble);
}