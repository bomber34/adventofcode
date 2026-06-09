private static final Pattern NUMBER_REGEX = Pattern.compile("-?[0-9]+");
private static final String RED_STRING = "\"red\"";

private static String getInput() {
    StringBuilder input = new StringBuilder();
    File inputFile = new File("input.txt");
    try (Scanner reader = new Scanner(inputFile)) {
        while (reader.hasNextLine()) {
            input.append(reader.nextLine());
        }
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }
    return input.toString();
}

private static int sumOfNumbers(String json) {
    // Task is nice enough to guarantee no digits inside of strings
    Matcher foundNumbers = NUMBER_REGEX.matcher(json);
    int sum = 0;
    while(foundNumbers.find()) {
        sum += Integer.parseInt(foundNumbers.group());
    }
    return sum;
}

void main() {
    String input = getInput();
    int sum = sumOfNumbers(input);
    IO.println(String.format("Sum of Json numbers is %d", sum));
    TreeNodeList tree = new TreeNodeList(input);
    sum = tree.getNodeSum();
    IO.println(String.format("Sum of Json without red objects numbers is %d", sum));
}

/**
 * Trying to solve this puzzle with regex alone was not successful, neither was iterating with a stack through the string
 * Instead I create a tree with three kind of nodes. Curly, Bracket or Text nodes.
 * Text nodes represent leaves, while Curly and Bracket nodes represent trees themselves.
 * At the end, each tree can calculate its number sum as it is the sum of all of its nodes.
 * If a tree is marked as curly and contains the illegal string in one of its Text nodes, we ignore the sum.
 *
 * The tree assumes that if the given tree starts with a opening curly or bracket that it also ends with the corresponding bracket!
 */
static class TreeNodeList {
    EType _type;
    protected final String _source;
    ArrayList<TreeNodeList> _nodes;

    public TreeNodeList(String src) {
        _nodes = new ArrayList<>();
        if (isCurlySource(src)) {
            _type = EType.CURLY;
            _source = src.substring(1, src.length()-1);
        } else if (isBracketSource(src)) {
            _type = EType.BRACKET;
            _source = src.substring(1, src.length()-1);
        } else {
            _type = EType.TEXT;
            _source = src != null ? src : "";
        }

        if (_type != EType.TEXT) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < _source.length(); i++) {
                char currentLetter = _source.charAt(i);
                if (currentLetter != '{' && currentLetter != '[') {
                    sb.append(currentLetter);
                } else {
                    char closingChar = currentLetter == '{' ? '}' : ']';
                    String subStr = sb.toString();
                    if (!subStr.isEmpty()) {
                        _nodes.add(new TreeNodeList(subStr));
                    }
                    sb.setLength(0);
                    int closingCurlyIndex = findClosingIndex(i, currentLetter, closingChar);
                    String otherSubStr = _source.substring(i, closingCurlyIndex + 1);
                    _nodes.add(new TreeNodeList(otherSubStr));
                    i = closingCurlyIndex;
                }
            }
            String rest = sb.toString();
            if (!rest.isEmpty()) {
                _nodes.add(new TreeNodeList(rest));
            }
        }
    }

    public int getNodeSum() {
        if (_type == EType.TEXT) {
            return sumOfNumbers(_source);
        } else if (isCurlyWithRed()) {
            return 0;
        }
        int sum = 0;

        for (TreeNodeList node : _nodes) {
            sum += node.getNodeSum();
        }

        return sum;
    }

    public boolean isCurlyWithRed() {
        if (_type == EType.CURLY) {
            for (TreeNodeList node : _nodes) {
                if (node._type == EType.TEXT && node._source.contains(RED_STRING)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int findClosingIndex(int index, char openingChar, char closingChar) {
        int counter = 1;
        while (counter > 0) {
            index++;
            char currentChar = _source.charAt(index);
            if (currentChar == openingChar) {
                counter++;
            } else if (currentChar == closingChar) {
                counter--;
            }
        }
        return index;
    }

    private boolean isCurlySource(String src) {
        return src != null && !src.isEmpty() && src.charAt(0) == '{';
    }

    private boolean isBracketSource(String src) {
        return src != null && !src.isEmpty() && src.charAt(0) == '[';
    }

    private enum EType {
        CURLY,
        BRACKET,
        TEXT
    }

}