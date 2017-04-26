/**
 * Created by hideyoshitakahashi on 4/25/17.
 */

import edu.princeton.cs.algs4.Picture;
//import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;

public class SeamCarver {

    private Picture pic;
    private Picture dummyPic;
    private int width;
    private int height;
    private double[][] energyPaths;

    public SeamCarver(Picture picture) {
        pic = picture;
        dummyPic = new Picture(pic);
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
        dummyPic = new Picture(pic);
        return dummyPic;
    }
    public int width() { // width of current picture
        return width;
    }
    public int height() { // height of current picture
        return height;
    }
    public double energy(int x, int y) { // energy of pixel at column x and row y

        Color left, right, above, below;
        int xEnergyR, xEnergyG, xEnergyB, xEnergy, yEnergyR, yEnergyG, yEnergyB, yEnergy;

        if (width != 1) {
            if (x == width - 1) {
                left = pic.get(x - 1, y);
                right = pic.get(0, y);
            } else if (x == 0) {
                left = pic.get(width - 1, y);
                right = pic.get(x + 1, y);
            } else {
                left = pic.get(x - 1, y);
                right = pic.get(x + 1, y);
            }
            xEnergyR = right.getRed() - left.getRed();
            xEnergyG = right.getGreen() - left.getGreen();
            xEnergyB = right.getBlue() - left.getBlue();
            xEnergy = xEnergyR * xEnergyR + xEnergyG * xEnergyG + xEnergyB * xEnergyB;

        } else {
            xEnergy = 0;
        }

        if (height != 1) {
            if (y == height - 1) {
                above = pic.get(x, 0);
                below = pic.get(x, y - 1);
            } else if (y == 0) {
                above = pic.get(x, y + 1);
                below = pic.get(x, height - 1);
            } else {
                above = pic.get(x, y + 1);
                below = pic.get(x, y - 1);
            }
            yEnergyR = above.getRed() - below.getRed();
            yEnergyG = above.getGreen() - below.getGreen();
            yEnergyB = above.getBlue() - below.getBlue();
            yEnergy = yEnergyR * yEnergyR + yEnergyG * yEnergyG + yEnergyB * yEnergyB;
        } else {
            yEnergy = 0;
        }

        return xEnergy + yEnergy;


    }

    public int[] findHorizontalSeam() { // sequence of indices for horizontal seam
        return new int[0];
    }
    public int[] findVerticalSeam() {

        for (int s = 0; s < width; s++) {
            energyPaths[s][0] = energy(s,0);
        }
        for (int j = 1; j < height - 1; j++) {  // smallest の node の 2d array を作った
            for (int i = 0; i < width; i++) {
                for (int c = -1; c <= 1; c++) {
                    if ((c == -1 && i == 0) || (c == 1 && i == width - 1)) {
                        continue;
                    }
                    if (energyPaths[i + c][j + 1] != 0) {
                        double oldEnergy = energyPaths[i + c][j + 1];
                        double newEnergy = energyPaths[i][j] + energy(i + c, j + 1);
                        energyPaths[i + c][j + 1] = Math.min(oldEnergy, newEnergy);
                    } else {
                        energyPaths[i + c][j + 1] = energyPaths[i][j] + energy(i + c, j + 1);
                    }
                }
            }
        }

        int minIndex = 0;
        double min = energyPaths[0][height - 1];
        for (int s = 1; s < width; s++) {
            double newMin = energyPaths[s][height - 1];
            if (newMin < min) {
                min = newMin;
                minIndex = s;
            }
        } // energyPaths の一番下のrowの中からminimumを見つけた(==shortest path の goal を見つけた)

        int[] trackIndices = new int[height]; // 最終的に返す array
        trackIndices[height - 1] = minIndex;

        double d1;
        double d2;
        double d3;

        int trackIndex = minIndex;

        for (int i = 1; i < height; i++) {
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
            /*
            double dif = min - energy(trackIndex, height - i - 1);
            if (dif == min - d1) {
                trackIndex = trackIndex - 1;
            } else if (dif == min - d2) {
                trackIndex = trackIndex + 0;
            } else {
                trackIndex = trackIndex + 1;
            }
            */
            int indexOfMin = minOfThreeIndex(d1, d2, d3);
            trackIndex = trackIndex + indexOfMin;
            trackIndices[height - i - 1] = trackIndex;

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

    /*
    public double minOfThree(double d1, double d2, double d3) {
        if(d1 < d2 && d1 < d3){
            return d1;
        }else if(d2 < d3 && d2 < d1){
            return d2;
        }else{
            return d3;
        }
    }
    */

    private int minOfThreeIndex(double d1, double d2, double d3) {
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

/*

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
        p2 = energyPaths[i - 1][j - 1];
        p3 = energyPaths[i + 1][j - 1];
        }

*/


/*
        for (int j = 1; j < height; j++) {  // smallest の node の 2d array を作った
            for (int i = 0; i < width; i++) {

                double p1, p2, p3;
                if (i == 0) {
                    p1 = energy(i, j - 1);
                    p2 = energy(i + 1, j - 1);
                    p3 = 99999;
                } else if (i  == width - 1) {
                    p1 = energy(i, j - 1);
                    p2 = energy(i - 1, j - 1);
                    p3 = 99999;
                } else {
                    p1 = energy(i, j - 1);
                    p2 = energy(i - 1, j - 1);
                    p3 = energy(i + 1, j - 1);
                }
                energyPaths[i][j] = Math.min(energyPaths[i][j], energy(i, j) + minOfThree(p1, p2, p3));
            }
        }

 */