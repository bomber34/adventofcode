private static final int GOAL = 29000000;

void main() {
    partOne();
    partTwo();
}

@SuppressWarnings("unused")
void example() {
    for (int i = 2; i < 10; i++) {
        IO.println(String.format("Divisor sum of %d = %d", i, divisorSum(i)*10));
    }
}

private void partOne() {
    final int goal = GOAL / 10;
    //highlyCompositeApproach(goal); // unable to figure out efficient way to produce highly composite numbers
    IO.println(String.format("Part1: First house to surpass %d is %d", GOAL, bruteForce(goal)));
}

private void partTwo() {
    IO.println(String.format("Part2: First house to surpass %d is %d", GOAL, bruteForcePartTwo(GOAL)));
}

int divisorSum(int n) {
    int limit = (int) Math.sqrt(n);
    int sum = 1 + n; // skip first loop
    for (int i = 2; i <= limit; i++) {
        if (n % i == 0) {
            int div = n / i;
            sum += (div != i) ? div + i : i;
        }
    }
    return sum;
}

@SuppressWarnings("SameParameterValue")
int bruteForce(int goal) {
    int highestSum = 1;
    int highestSumIndex = 1;
    int i = 1;
    while (highestSum < goal) {
        i++;
        int sum = divisorSum(i);
        if (sum > highestSum) {
            highestSum = sum;
            highestSumIndex = i;
        }
    }
    return highestSumIndex;
}

int part2Sum(int n) {
    int limit = (int) Math.sqrt(n);
    int sum = 0;

    for (int i = 1; i <= limit; i++) {
        if (n % i == 0) {
            int div = n / i;

            if (i != div && n / i <= 50) {
                sum += (i * 11);
            }

            if (n / div <= 50) {
                sum += (div * 11);
            }
        }
    }
    return sum;
}

int bruteForcePartTwo(int goal) {
    int highestSum = 1;
    int highestSumIndex = 1;
    int i = 1;
    while (highestSum < goal) {
        i++;
        int sum = part2Sum(i);
        if (sum > highestSum) {
            highestSum = sum;
            highestSumIndex = i;
        }
    }
    return highestSumIndex;
}

/**
 * A smart approach for the part 1 problem would be to toy around with highly composite numbers.
 * I lack a good approach though to efficiently provide the number of exponents to the different prime numbers.
 * The order of the next higher composite numbers is a bit too chaotic.
 * See <a href="https://en.wikipedia.org/wiki/Highly_composite_number#Examples">Wikipedia</a>
 * Also the problem is still easy enough to quickly solve it by brute force
 *
 * @param goal to find the minimum divisor sum to meet
 */
@SuppressWarnings("unused")
void highlyCompositeApproach(int goal) {
    List<Integer> primes = getAllPrimesUntil((int) Math.sqrt(goal));
    int upperLimit = 1;
    int lastPrimeIndex = 0;
    for (Integer p : primes) {
        lastPrimeIndex++;
        upperLimit *= p;
        if (upperLimit >= goal) {
            break;
        }
    }
}

private List<Integer> getAllPrimesUntil(int limit) {
    if (limit <= 1) {
        return List.of();
    }

    boolean[] isNotPrimeList = new boolean[limit+1];
    isNotPrimeList[0] = true;
    isNotPrimeList[1] = true;

    ArrayList<Integer> primes = new ArrayList<>((int) Math.ceil(Math.log(limit)));
    for (int n = 0; n < isNotPrimeList.length; n++) {
        if (!isNotPrimeList[n]) {
            primes.add(n);
            for (int multiple = n*2; multiple < isNotPrimeList.length; multiple+=n) {
                isNotPrimeList[multiple] = true;
            }
        }
    }
    return Collections.unmodifiableList(primes);
}