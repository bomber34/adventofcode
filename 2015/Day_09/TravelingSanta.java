private static final int LOCATION_A_INDEX = 0;
private static final int LOCATION_B_INDEX = 2;
private static final int DISTANCE_INDEX = 4;

static class CityGraph {
    private final HashMap<String, ArrayList<RConnection>> _cities;

    public CityGraph() {
        _cities = new HashMap<>();
    }

    /**
     * Add bidirectional connection to graph.
     *
     * @param locationA String name of first location
     * @param locationB String name of second location
     * @param distance int
     */
    public void addConnection(String locationA, String locationB, int distance) {
        RConnection aToB = new RConnection(locationB, distance);
        RConnection bToA = new RConnection(locationA, distance);
        addToMap(locationA, aToB);
        addToMap(locationB, bToA);
    }

    /**
     * @return Set<String> of all entered cities
     */
    public Set<String> getCities() {
        return _cities.keySet();
    }

    /**
     * Gets distance between the two provided locations
     *
     * @param from String of location to start
     * @param to String of location to end
     * @return int distance
     */
    public int getDistance(String from, String to) {
        ArrayList<RConnection> list = _cities.get(from);
        if (list == null) {
            throw new IllegalArgumentException(String.format("No entry for key %s exists", from));
        }

        for (RConnection connection : list) {
            if (connection.name.equals(to)) {
                return connection.distance;
            }
        }
        throw new IllegalArgumentException(String.format("No connection from %s to %s", from, to));
    }

    public int travelDistance(String[] cityOrder) {
        int totalDistance = 0;
        int lastIndex = cityOrder.length - 1;
        for (int index = 0; index < lastIndex; index++) {
            totalDistance += getDistance(cityOrder[index], cityOrder[index+1]);
        }
        return totalDistance;
    }

    private void addToMap(String location, RConnection connection) {
        ArrayList<RConnection> locationList = _cities.get(location);
        if (locationList == null) {
            locationList = new ArrayList<>();
        }
        locationList.add(connection);
        _cities.put(location, locationList);
    }

    record RConnection(String name, int distance) {

    }
}

void main() {
    CityGraph graph = getInput();
    travelingSalesMan(graph);
}

private static void swap(String[] arr, int indexA, int indexB) {
    String tmp = arr[indexA];
    arr[indexA] = arr[indexB];
    arr[indexB] = tmp;
}

private static void travelingSalesMan(CityGraph graph) {
    /*
     * With how the graph is bidirectional we could cut down the number of needed permutations down by half
     */
    String[] cityOrder = graph.getCities().toArray(String[]::new);
    final int numberOfCities = cityOrder.length;
    int minDistance = graph.travelDistance(cityOrder);
    int maxDistance = minDistance;

    /*
     * Permutation algorithm from https://en.wikipedia.org/wiki/Heap%27s_algorithm
     */
    int[] indexes = new int[numberOfCities];

    int i = 0;
    while (i < numberOfCities) {
        if (indexes[i] < i) {
            swap(cityOrder, i % 2 == 0 ?  0: indexes[i], i);
            int distance = graph.travelDistance(cityOrder);
            if (distance < minDistance) {
                minDistance = distance;
            }
            if (distance > maxDistance) {
                maxDistance = distance;
            }
            indexes[i]++;
            i = 0;
        }
        else {
            indexes[i] = 0;
            i++;
        }
    }

    IO.println(String.format("Part1: MinDistance calc is %d", minDistance));
    IO.println(String.format("Part2: MaxDistance calc is %d", maxDistance));
}

private static CityGraph getInput() {
    File inputFile = new File("input.txt");
    CityGraph graph = new CityGraph();
    try (Scanner reader = new Scanner(inputFile)) {
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            String[] tokens = line.split(" ");
            String locationA = tokens[LOCATION_A_INDEX];
            String locationB = tokens[LOCATION_B_INDEX];
            int distance = Integer.parseInt(tokens[DISTANCE_INDEX]);
            graph.addConnection(locationA, locationB, distance);
        }
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }

    return graph;
}