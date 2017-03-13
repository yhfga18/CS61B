package hw2;
import edu.princeton.cs.introcs.StdRandom; // generate random number
import edu.princeton.cs.introcs.StdStats;


// N*Nのgridを開けまくっていってpercolateしたらストップ。percolateするのに

// 必要だったfraction Xt( = openの回数 / N*N)を記録する。

// この手順をT回繰り返してresultのaverageをとる



public class PercolationStats {
    private double[] x;
    private int experimentNum;
    public PercolationStats(int N, int T) {
        experimentNum = T;
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        x = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation perco = new Percolation(N);
            for (int j = 0; j < N * N; j++) {
                if (perco.percolates()) {
                    j = N * N;
                } else {
                    int randomRow = StdRandom.uniform(0, N);
                    int randomCol = StdRandom.uniform(0, N);
                    if (!(perco.isOpen(randomRow, randomCol))) {
                        perco.open(randomRow, randomCol);
                        if (perco.percolates()) {
                            double jj = (double) j + 1;
                            x[i] = jj / (N * N);  // "/" でちゃんとdoubleになるか
                            //System.out.println("x[" + i + "] is : " + (x[i]));
                        }
                    } else {
                        j--;
                    }
                }
            }
        }

    }  // perform T independent experiments on an N-by-N grid



    public double mean() {
        return StdStats.mean(x);
    } // sample mean of percolation threshold
    public double stddev() {
        return StdStats.stddev(x);
    } // sample standard deviation of percolation threshold
    public double confidenceLow() {
        return mean() - (1.96 * stddev()) / Math.sqrt(experimentNum);
    } // low  endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + (1.96 * stddev()) / Math.sqrt(experimentNum);
    } // high endpoint of 95% confidence interval
}
