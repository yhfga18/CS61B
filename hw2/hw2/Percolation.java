package hw2;                       

//import com.sun.javafx.scene.traversal.WeightedClosestCorner;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF uni;
    private int numRow;
    private int numCol;
    private int virtualTop;
    private int virtualBottom;

    public Percolation(int N) {
        grid = new boolean[N][N];
        numRow = N;
        numCol = N;
        virtualTop = numRow * numCol;
        virtualBottom = numRow * numCol + 1;
        uni = new WeightedQuickUnionUF((numRow*numCol) + 2);

        /*
        for (int i = 0; i < numCol; i++) {
            uni.union(virtualTop, to1D(0, i));
            uni.union(virtualBottom, to1D(numRow-1, i));
        }
        */

    }

    //二つの次元での話。一つは2D arrayのgrid上でtrue/falseでopen/closeを再現すること
    //もう一つはWeightedQuickUnionUFでconnectednessを保存していくこと。
    //二つを同時に、別々に進める必要が有る。

    // isFull = open & connected to virtualTop

    public void open(int row, int col) { // modify grid & connect
        grid[row][col] = true;
        connectAround(to1D(row, col));
    }

    private void connectAround(int center){ // takes # of grid (not location)
        if (0 <= center && center < numCol) {
            uni.union(center, virtualTop);
        } else if ((numRow-1)*(numCol) <= center && center < (numRow)*numCol) {
            uni.union(center, virtualBottom);
        }

        int[] center2D = to2D(center);
        for (int i = -1; i <= 1; i += 2) {
            int row = center2D[0] + i;
            int col = center2D[1];
            if (row >= 0 && row < numRow && col >= 0 && col < numCol){
                if (isOpen(row, col)) {
                    uni.union(center, to1D(row, col));
                }
            }
        }
        for (int j = -1; j <= 1; j += 2) {
            int row = center2D[0];
            int col = center2D[1] + j;
            if (row >= 0 && row < numRow && col >= 0 && col < numCol){
                if (isOpen(row, col)) {
                    uni.union(center, to1D(row, col));
                }
            }
        }
    }

    private int to1D(int r, int c) { // # of grid → location
        return numCol * r + c; //numCol = grid[0].length + 1
    }

    private int[] to2D(int x) { // location → # of grid
        int[] iArray = {x/numRow, x%numCol};
        return iArray;
    }

    public boolean isOpen(int row, int col){ // location T/F
        return grid[row][col];
    } // is the site (row, col) open?

    public boolean isFull(int row, int col) { // # conneced to virtualTop?
        int target = to1D(row, col);
        for (int i = 0; i < numRow; i++){
           if((uni.connected(target, virtualTop))){
               return true;
           }
        }
        return false;
    } // is the site (row, col) full?

    public int numberOfOpenSites() {
        int counter = 0;
        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numRow; j++) {
                if (isOpen(i, j)) {
                    counter++;
                }
            }
        }
        return counter;
    } // number of open sites

    public boolean percolates() {
        return uni.connected(virtualTop, virtualBottom);
    } // does the system percolate?

    //public static void main(String[] args) {

    //} // unit testing (not required
}                       
