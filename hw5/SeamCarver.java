/**
 * Created by hideyoshitakahashi on 4/25/17.
 */

import edu.princeton.cs.algs4.Picture;
//import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;

public class SeamCarver {

    Picture pic;
    int width;
    int height;
    double[][] energyPaths;

    public SeamCarver(Picture picture) {
        pic = picture;
        width = picture.width();
        height = picture.height();
        energyPaths = new double[width][height];
        /*
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                energy[i][j] = energy(i, j);
//                edgeTo[i][j] = Integer.MAX_VALUE;
            }
        }
        */
    }
    public Picture picture() { // current picture
        return pic;
    }
    public int width() { // width of current picture
        return width;
    }
    public int height() { // height of current picture
        return pic.width();
    }
    public double energy(int x, int y) { // energy of pixel at column x and row y

        Color left;// = pic.get(x - 1, y);
        Color right;// = pic.get(x + 1, y);
        Color above;// = pic.get(x, y + 1);
        Color below;// = pic.get(x, y - 1);
        if (width == 1) {

        }
        if (x == width() - 1) {
            left = pic.get(x - 1, y);
            right = pic.get(0, y);
        } else if (x == 0){
            left = pic.get(width() - 1, y);
            right = pic.get(x + 1, y);
        } else {
            left = pic.get(x - 1, y);
            right= pic.get(x + 1, y);
        }

        if (y == height()) {
            above = pic.get(x, 0);
            below = pic.get(x, y - 1);
        } else if (y == 0) {
            above = pic.get(x, height() - 1);
            below = pic.get(x, y + 1);
        } else {
            above = pic.get(x, y + 1);
            below = pic.get(x, y - 1);
        }

        int xEnergyR = right.getRed() - left.getRed();
        int xEnergyG = right.getGreen() - left.getGreen();
        int xEnergyB = right.getBlue() - left.getBlue();
        int xEnergy = xEnergyR * xEnergyR + xEnergyG * xEnergyG + xEnergyB * xEnergyB;

        int yEnergyR = above.getRed() - below.getRed();
        int yEnergyG = above.getGreen() - below.getGreen();
        int yEnergyB = above.getBlue() - below.getBlue();
        int yEnergy = yEnergyR * yEnergyR + yEnergyG * yEnergyG + yEnergyB * yEnergyB;

        return xEnergy + yEnergy;


    }

    public int[] findHorizontalSeam() { // sequence of indices for horizontal seam
        return new int[0];
    }
    public int[] findVerticalSeam() {
        int[] trackIndices = new int[height];
        for (int s = 0; s < width; s++) {
            energyPaths[s][0] = energy(s,0);
        }
        for (int j = 1; j < height; j++) {  // smallest の node の 2d array を作った
            for (int i = 0; i < width; i++) {
                double p1, p2, p3;
                if (i == 0) {
                    p1 = energyPaths[i][j - 1];
                    p2 = energyPaths[i + 1][j - 1];
                    p3 = 99999;
                } else if (i  == width - 1) {
                    p1 = energyPaths[i][j - 1];
                    p2 = energyPaths[i - 1][j - 1];
                    p3 = 99999;
                } else {
                    p1 = energyPaths[i][j - 1];
                    p2 = energyPaths[i - 1][j - 1]; // いずれ out of bound exception が出る
                    p3 = energyPaths[i + 1][j - 1];
                }
                energyPaths[i][j] = Math.min(energyPaths[i][j], energy(i, j) + minOfThree(p1, p2, p3));
            }
        }

        double min = energyPaths[0][height - 1];
        int minIndex = 0;
        for (int s = 1; s < width; s++) {
            min = Math.min(energyPaths[s][height - 1], min);
            minIndex = s;
        }
        double d1;
        double d2;
        double d3;
        int trackIndex = minIndex;
        for (int i = 1; i < width; i++) {
            if (trackIndex == 0) {
                d1 = 99999;
                d2 = energyPaths[trackIndex][height - i - 1];
                d3 = energyPaths[trackIndex + 1][height - i - 1];
            } else if (trackIndex == width - 1) {
                d1 = energyPaths[trackIndex - 1][height - i - 1];
                d2 = energyPaths[trackIndex][height - i - 1];
                d3 = 99999;
            } else {
                d1 = energyPaths[trackIndex - 1][height - i - 1];
                d2 = energyPaths[trackIndex][height - i - 1];
                d3 = energyPaths[trackIndex + 1][height - i - 1];
            }
            int mindIndex = minOfThreeIndex(d1, d2, d3);
            trackIndices[height - i] = trackIndex + mindIndex;
        }

        return trackIndices;

        /*
        for (int i = 0; i < width; i++) { // for each starting node
            for (int j = 0; j < height - 1; j++) { // till get the bottom
                int min = minOfThree(energy[i][j - 1], energy[i][j], energy[i][j + 1]);
                edgeTo[i][j + min] = ; // 自分自身のxにたいして
                // 右なのか中央なのか左なのかを入れればいい。y方向には１づつしか動かない
            }
        }
        */
    }

    public double minOfThree(double d1, double d2, double d3) {
        if(d1 < d2 && d1 < d3){
            return d1;
        }else if(d2 < d3 && d2 < d1){
            return d2;
        }else{
            return d3;
        }
    }
    public int minOfThreeIndex(double d1, double d2, double d3) {
        if(d1 < d2 && d1 < d3){
            return -1;
        }else if(d2 < d3 && d2 < d1){
            return 0;
        }else{
            return 1;
        }
    }

    public void removeHorizontalSeam(int[] seam) { // remove horizontal seam from picture

    }
    public void removeVerticalSeam(int[] seam) { // remove vertical seam from picture
        int[] indices = findVerticalSeam();
    }
}