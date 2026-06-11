private static final int REINDEER_NAME_INDEX = 0;
private static final int SPEED_INDEX = 3;
private static final int DURATION_INDEX = 6;
private static final int PAUSE_DURATION_INDEX =13;

private static final boolean IS_TEST = false;
private static final int RACE_DURATION = IS_TEST ? 1000 : 2503;

record RReindeer(String name, int speed, int duration, int pause) {

}

static class ReindeerRacer {
    private final RReindeer _racer;
    private int _currentDuration;
    private boolean _isRunning;
    private int _totalTravelled;
    private int _points;

    public ReindeerRacer(RReindeer racer) {
        _racer = racer;
        _currentDuration = racer.duration;
        _isRunning = true;
        _totalTravelled = 0;
        _points = 0;
    }

    /**
     * Update reindeer state in regards to movement
     */
    public void updateMove() {
        if (_currentDuration == 0) {
            _isRunning = !_isRunning;
            _currentDuration = _isRunning ? _racer.duration : _racer.pause;
        }
        int traveled = _isRunning ? _racer.speed : 0;
        _totalTravelled += traveled;
        _currentDuration--;
    }

    /**
     * update point by one
     */
    public void updatePoint() {
        _points++;
    }

    public int getTotalTravelled() {
        return _totalTravelled;
    }

    public int getPoints() {
        return _points;
    }

    public String getName() {
        return _racer.name;
    }
}

void main() {
    ArrayList<RReindeer> reindeer;
    reindeer = IS_TEST ? getExample() : getInput();
    int winningDistance = getWinningDistance(reindeer);
    IO.println(String.format("The winning distance is %d km", winningDistance));
    ReindeerRacer winner = getWinningPoints(reindeer);
    IO.println(String.format("Winner is %s with %d points", winner.getName(), winner.getPoints()));

}

private ReindeerRacer getWinningPoints(ArrayList<RReindeer> reindeer) {
    int remainingDuration = RACE_DURATION;
    List<ReindeerRacer> racers = reindeer.stream().map(ReindeerRacer::new).toList();
    while (remainingDuration > 0) {
        racers.forEach(ReindeerRacer::updateMove);

        int maxTravelled = 0;
        for (ReindeerRacer racer : racers) {
            int travelDistance = racer.getTotalTravelled();
            if (maxTravelled < travelDistance) {
                maxTravelled = travelDistance;
            }
        }

        for (ReindeerRacer racer : racers) {
            if (racer.getTotalTravelled() == maxTravelled) {
                racer.updatePoint();
            }
        }

        remainingDuration--;
    }

    ReindeerRacer winner = racers.getFirst();
    int maxPoints = 0;
    for (ReindeerRacer racer : racers) {
        IO.println(String.format("%s travelled %d km and got %d points", racer.getName(), racer.getTotalTravelled(), racer.getPoints()));
        if (maxPoints < racer.getPoints()) {
            maxPoints = racer.getPoints();
            winner = racer;
        }
    }
    return winner;
}

private int getWinningDistance(ArrayList<RReindeer> racers) {
    int maxDistance = -1;
    for (RReindeer reindeer : racers) {
        int distanceTravelled = 0;
        int interval = reindeer.duration + reindeer.pause;
        int distance = reindeer.speed * reindeer.duration;
        int numberOfIntervals = (RACE_DURATION / interval);
        distanceTravelled += numberOfIntervals * distance;
        int restOfTime = RACE_DURATION % interval;
        if (reindeer.duration <= restOfTime) {
            distanceTravelled += distance;
        } else {
            distanceTravelled += restOfTime * reindeer.speed;
        }

        IO.println(String.format("%s travelled %d km in %d seconds", reindeer.name, distanceTravelled, RACE_DURATION));
        if (maxDistance < distanceTravelled) {
            maxDistance = distanceTravelled;
        }
    }
    return maxDistance;
}

private ArrayList<RReindeer> getInput() {
    File inputFile = new File("input.txt");
    ArrayList<RReindeer> racers = new ArrayList<>();
    try (Scanner reader = new Scanner(inputFile)) {
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            String[] tokens = line.split(" ");
            String name = tokens[REINDEER_NAME_INDEX];
            int speed = Integer.parseInt(tokens[SPEED_INDEX]);
            int duration = Integer.parseInt(tokens[DURATION_INDEX]);
            int pause = Integer.parseInt(tokens[PAUSE_DURATION_INDEX]);

            racers.add(new RReindeer(name, speed, duration, pause));
        }
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }

    return racers;
}

private ArrayList<RReindeer> getExample() {
    ArrayList<RReindeer> racers = new ArrayList<>();
    RReindeer a = new RReindeer("Comet", 14, 10, 127);
    RReindeer b = new RReindeer("Dancer", 16, 11, 162);
    racers.add(a);
    racers.add(b);
    return racers;
}