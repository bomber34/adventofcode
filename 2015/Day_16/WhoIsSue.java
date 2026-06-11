import java.util.HashMap;

private static final Pattern REGEX = Pattern.compile("(?<name>[a-z]+): (?<value>[0-9]+)");
private static final String[] GREATER_THAN_PROPS = new String[] {"cats", "trees"};
private static final String[] LESS_THAN_PROPS = new String[] {"pomeranians", "goldfish"};

record RPair(String name, int value) {

}

private static final List<RPair> WANTED_PROPERTIES = Arrays.stream(new RPair[]{
        new RPair("children", 3),
        new RPair("cats", 7),
        new RPair("samoyeds", 2),
        new RPair("pomeranians", 3),
        new RPair("akitas", 0),
        new RPair("vizslas", 0),
        new RPair("goldfish", 5),
        new RPair("trees", 3),
        new RPair("cars", 2),
        new RPair("perfumes", 1),
}
).toList();

private static final Sue WANTED_SUE = new Sue(0, WANTED_PROPERTIES);

static class Sue {
    public final int id;
    private final HashMap<String, Integer> _propertyValues;

    Sue(int sueId, List<RPair> properties) {
        id = sueId;
        _propertyValues = new HashMap<>();
        for (RPair pair : properties) {
            _propertyValues.put(pair.name(), pair.value());
        }
    }

    public Set<String> getStoredPropertiesKeys() {
        return _propertyValues.keySet();
    }

    public Integer getStoredValueForProperty(String property) {
        return _propertyValues.get(property);
    }

    public int similarityScore(Sue other, boolean isPartB) {
        int score = 0;
        for (String prop : other.getStoredPropertiesKeys()) {
            Integer value = _propertyValues.get(prop);
            Integer otherValue = other.getStoredValueForProperty(prop);
            if (value == null || otherValue == null) {
                continue;
            }

            boolean isLessThanProp = isLessThanProp(prop);
            boolean isGreaterThanProp = isGreaterThanProp(prop);
            if (!isPartB || (!isLessThanProp && !isGreaterThanProp)) {
                score += compareDirect(value, otherValue);
            } else if (isLessThanProp) {
                score += compareLess(value, otherValue);
            } else {
                score += compareGreater(value, otherValue);
            }
        }
        return score;
    }

    private boolean isLessThanProp(String prop) {
        for (String p : LESS_THAN_PROPS) {
            if (p.equals(prop)) {
                return true;
            }
        }
        return false;
    }

    private boolean isGreaterThanProp(String prop) {
        for (String p : GREATER_THAN_PROPS) {
            if (p.equals(prop)) {
                return true;
            }
        }
        return false;
    }

    public int compareDirect(int thisVal, int otherVal) {
        return thisVal == otherVal ? 1 : 0;
    }

    public int compareLess(int thisVal, int otherVal) {
        return thisVal < otherVal ? 1 : 0;
    }

    public int compareGreater(int thisVal, int otherVal) {
        return thisVal > otherVal ? 1 : 0;
    }
}

void main() {
    ArrayList<Sue> sues = getInput();
    findSue(sues, false);
    findSue(sues, true);
}

private void findSue(ArrayList<Sue> sues, boolean isPartB) {
    int bestSue = -1;
    int bestScore = -1;
    for (Sue sue : sues) {
        int score = sue.similarityScore(WANTED_SUE, isPartB);
        if (score > bestScore) {
            bestScore = score;
            bestSue = sue.id;
        }
    }
    IO.println(String.format("Best Sue is number %d with score %d", bestSue, bestScore));
}

private ArrayList<Sue> getInput() {
    File inputFile = new File("input.txt");
    ArrayList<Sue> people = new ArrayList<>();
    try (Scanner reader = new Scanner(inputFile)) {
        while (reader.hasNextLine()) {
            String line = reader.nextLine();

            int id = Integer.parseInt(line.substring(line.indexOf(" ")+1, line.indexOf(":")));
            ArrayList<RPair> pairs = new ArrayList<>();
            Matcher match = REGEX.matcher(line);
            while (match.find()) {
                String propName = match.group("name");
                int propValue = Integer.parseInt(match.group("value"));
                pairs.add(new RPair(propName, propValue));
            }
            people.add(new Sue(id, pairs));
        }
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }

    return people;
}