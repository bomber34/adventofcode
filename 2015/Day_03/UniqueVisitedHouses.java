import java.awt.Point;
import java.util.List;

void main() {
    String directions = getInput();
    int uniqueHouses = uniqueHousesBySantaAlone(directions); // part 1
    IO.println("Amount of unique houses if Santa travels alone: " + uniqueHouses);
    uniqueHouses = uniqueHousesByTeam(directions, 2); // part 2
    IO.println("Amount of unique houses if Santa travels with Robot: " + uniqueHouses);

    // was not asked for but for curiosity, number of unique houses with different team sizes
    int maxAmount = uniqueHouses;
    int bestTeamSize = 2;
    int maxTeamSize = directions.length();
    for (int teamSize = 1; teamSize <= maxTeamSize; teamSize++) {
        uniqueHouses = uniqueHousesByTeam(directions, teamSize);
        IO.println(String.format("Number of unique houses with teamSize %d: %d", teamSize, uniqueHouses));
        if (uniqueHouses > maxAmount) {
            maxAmount = uniqueHouses;
            bestTeamSize = teamSize;
        }
    }
    // best team size for the directions provided by task is 4 and delivers to 2741 unique houses
    IO.println(String.format("Best Team Size is %d and delivers to %d unique houses", bestTeamSize, maxAmount));
}

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

private static Point updatePoint(Point lastPoint, char direction) {
    int x = lastPoint.x;
    int y = lastPoint.y;
    switch (direction) {
        case '^':
            y++;
            break;
        case '>':
            x++;
            break;
        case 'v':
            y--;
            break;
        case '<':
            x--;
            break;
        default:
            // ignore invalid directions
            break;
    }
    return new Point(x, y);
}

private static int uniqueHousesBySantaAlone(String directions) {
    HashSet<Point> visitedHouses = trackVisitedHouses(new Point(0, 0), directions, null);
    return visitedHouses.size(); // 2565
}

private static HashSet<Point> trackVisitedHouses(Point startingPoint, String directions, HashSet<Point> visitedHouses) {
    Point lastPoint = startingPoint;

    if (visitedHouses == null) {
        visitedHouses = new HashSet<>();
    }
    visitedHouses.add(startingPoint);

    for (char direction : directions.toCharArray()) {
        Point newPoint = updatePoint(lastPoint, direction);
        visitedHouses.add(newPoint);
        lastPoint = newPoint;
    }

    return visitedHouses;
}

// suppressing warning for teamSize, because exercise only requires call of this function once
@SuppressWarnings("SameParameterValue")
private static int uniqueHousesByTeam(String directions, final int teamSize) {
    Point startingPoint = new Point(0, 0);
    HashSet<Point> visitedHouses = new HashSet<>();
    visitedHouses.add(startingPoint);
    String[] subDirectionsPerTeam = divideDirections(directions, teamSize);

    for (String subDirections : subDirectionsPerTeam) {
        trackVisitedHouses(startingPoint, subDirections, visitedHouses);
    }
    return visitedHouses.size();
}

private static String[] divideDirections(String directions, final int teamSize) {
    if (teamSize < 1) {
        throw new IllegalArgumentException("teamSize has to be a positive integer but was " + teamSize);
    }

    List<StringBuilder> splitDirections = new ArrayList<>(teamSize);
    for (int index = 0; index < teamSize; ++index) {
        splitDirections.add(new StringBuilder());
    }

    int index = 0;
    for (char c : directions.toCharArray()) {
        int splitIndex = index % teamSize;
        splitDirections.get(splitIndex).append(c);
        index++;
    }

    return splitDirections.stream().map(StringBuilder::toString).toArray(String[]::new);
}