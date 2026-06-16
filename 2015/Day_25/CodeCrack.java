
private static final long START_NUMBER  = 20151125L;
private static final long MULTIPLIER    = 252533L;
private static final long MODULO        = 33554393L;
private static final int GOAL_ROW       = 2947;
private static final int GOAL_COL       = 3029;

/*
 * I am sure you can just calculate any number of the grid instead of computing the whole thing
 */
void main() {
    long[][] grid = gridField(GOAL_ROW, GOAL_COL);
    long finalIt = grid[GOAL_ROW-1][GOAL_COL-1];

    long num = START_NUMBER;
    for (long i = 1; i < finalIt; i++) {
        num = (num * MULTIPLIER) % MODULO;
    }
    IO.println("Code is " + num);
}

long[][] gridField(int rows, int cols) {
    int maxSize = Math.max(rows, cols);
    long[][] grid = new long[maxSize][maxSize];
    grid[0][0] = 1;
    for (int c = 1; c < grid.length; c++) {
        grid[0][c] = grid[0][c-1] + (c+1);
        grid[c][0] = grid[0][c] - c;
    }

    for (int r = 1; r < grid.length; r++) {
        for (int c = 1; c < grid.length;c++) {
            grid[r][c] = grid[r-1][c] + (r + c );
        }
    }

    return grid;
}
