private static final int PERSON_INDEX = 0;
private static final int CHANGE_INDEX = 2;
private static final int NUMBER_INDEX = 3;

/**
 * This is basically day 9 with a twist
 */
static class PeopleHappiness {
    private final HashMap<String, Set<RLikePoints>> _neighborHappiness;

    PeopleHappiness() {
        _neighborHappiness = new HashMap<>();
    }

    public void addConnection(RLikePoints pair) {
        _neighborHappiness.putIfAbsent(pair.name, new HashSet<>());
        Set<RLikePoints> likeList = _neighborHappiness.get(pair.name);
        likeList.add(pair);
    }

    public void addMe() {
        String myName = "<MyName>";

        if (_neighborHappiness.get(myName) != null) {
            return;
        }

        String[] names = getAllPeople();
        for (String n : names) {
            RLikePoints pairA = new RLikePoints(myName, n, 0);
            RLikePoints pairB = new RLikePoints(n, myName, 0);
            addConnection(pairA);
            addConnection(pairB);
        }
    }

    public String[] getAllPeople() {
        return _neighborHappiness.keySet().toArray(new String[0]);
    }

    public int calculateTotalHappiness(String[] names) {
        int sum = 0;
        int len = names.length;
        for (int i = 0; i < len; i++) {
            String name = names[i];
            String leftNeighbor = names[(len + i - 1) % len];
            String rightNeighbor = names[(i + 1) % len];

            sum += getPair(name, leftNeighbor).happiness;
            sum += getPair(name, rightNeighbor).happiness;
        }
        return sum;
    }

    private RLikePoints getPair(String name, String neighbor) {
        Set<RLikePoints> scores =  _neighborHappiness.get(name);
        for (RLikePoints score : scores) {
            if (score.neighbor.equals(neighbor)) {
                return score;
            }
        }
        throw new IllegalStateException(String.format("No neighbor pair exists for %s and %s", name, neighbor));
    }
}

record RLikePoints(String name, String neighbor, int happiness) {

}

void main() {
    PeopleHappiness people = getInput();
    findBestOrder(people); // part1
    people.addMe();
    findBestOrder(people); // part2
}

private static void swap(String[] arr, int indexA, int indexB) {
    String tmp = arr[indexA];
    arr[indexA] = arr[indexB];
    arr[indexB] = tmp;
}

private static void findBestOrder(PeopleHappiness graph) {

    String[] names = graph.getAllPeople();
    final int numberOfPeople = names.length;
    int maxHappiness = graph.calculateTotalHappiness(names);

    /*
     * Permutation algorithm from https://en.wikipedia.org/wiki/Heap%27s_algorithm
     */
    int[] indexes = new int[numberOfPeople];

    int i = 0;
    while (i < numberOfPeople) {
        if (indexes[i] < i) {
            swap(names, i % 2 == 0 ?  0: indexes[i], i);
            int distance = graph.calculateTotalHappiness(names);

            if (distance > maxHappiness) {
                maxHappiness = distance;
            }
            indexes[i]++;
            i = 0;
        }
        else {
            indexes[i] = 0;
            i++;
        }
    }

    IO.println(String.format("Max Happiness is %d", maxHappiness));
}

private static PeopleHappiness getInput() {
    File inputFile = new File("input.txt");
    PeopleHappiness graph = new PeopleHappiness();
    try (Scanner reader = new Scanner(inputFile)) {
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            String[] tokens = line.split(" ");
            String person = tokens[PERSON_INDEX];
            int sign = tokens[CHANGE_INDEX].equals("lose") ? -1 : 1;
            int number = Integer.parseInt(tokens[NUMBER_INDEX]) * sign;

            String neighbour = tokens[tokens.length-1];
            neighbour = neighbour.substring(0, neighbour.length()-1); //get rid of period

            graph.addConnection(new RLikePoints(person, neighbour, number));
        }
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }

    return graph;
}