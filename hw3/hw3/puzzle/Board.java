package hw3.puzzle;
import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState{
    int size;
    int[][] tiles;
    public Board(int[][] tiles) {
        size = tiles.length;
        this.tiles = tiles;
    }
    public int tileAt(int i, int j) {
        int tile = tiles[i][j];
        if (tile == 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return tile;
    }
    public int size() {
        return size;
    }
    public Iterable<WorldState> neighbors() { // copied from the prof Hug version of nerighbors().
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == 0) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = 0;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = 0;
                }
            }
        }
        return neighbors;
    }
    public int hamming() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != 0) {
                    if (tiles[i][j] != size * i + j + 1) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
    public int manhattan() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != 0) {
                    int row = (tiles[i][j] - 1) / size;
                    int col = (tiles[i][j] % size - 1) % 4;
                    count += Math.abs(i - row) + Math.abs(j - col);
                }
            }
        }
        return count;

    }
    public int estimatedDistanceToGoal() {
        return manhattan();
    }
    public boolean isGoal() {
        return estimatedDistanceToGoal() == 0;
    }
    public boolean equals(Object y) {
        if (this.getClass() != y.getClass()) {
            return false;
        }
        Board tiles2 = (Board) y;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != tiles2.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Returns the string representation of the board.
     * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }



    /*
    Board(tiles): Constructs a board from an N-by-N array of tiles where
    tiles[i][j] = tile at row i, column j
    tileAt(i, j): Returns value of tile at row i, column j (or 0 if blank)
    size():       Returns the board size N
    neighbors():  Returns the neighbors of the current board
    hamming():    Hamming estimate described below
    manhattan():  Manhattan estimate described below
    estimatedDistanceToGoal(): Estimated distance to goal. This method should
    simply return the results of manhattan() when submitted to
              Gradescope.
                      isGoal():     Returns true if is this board the goal board
    equals(y):    Returns true if this board's tile values are the same
    position as y's
    toString():   Returns the string representation of the board. This
    method is provided in the skeleton
    */

}
