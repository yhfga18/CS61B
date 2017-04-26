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
        pic = new Picture(picture);
        width = picture.width();
        height = picture.height();
    }
    public Picture picture() { // current picture
        return new Picture(pic);
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
                above = pic.get(x, y - 1);
                below = pic.get(x, 0);
            } else if (y == 0) {
                above = pic.get(x, height - 1);
                below = pic.get(x, y + 1);
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
            // Transpose picture.
        Picture original = pic;
        Picture transpose = new Picture(original.height(), original.width());

        for (int w = 0; w < transpose.width(); w++) {
            for (int h = 0; h < transpose.height(); h++) {
                Color color = original.get(h, w); // *****************
                transpose.set(w, h, color);
            }
        }
        pic = transpose;
        width = transpose.width();
        height = transpose.height();
        int[] horizontalSeam = findVerticalSeam();
        pic = original;
        width = original.width();
        height = original.height();
        return horizontalSeam;
    }

    public int[] findVerticalSeam() {

        energyPaths = new double[width][height];

        for (int s = 0; s < pic.width(); s++) {
            energyPaths[s][0] = energy(s, 0);
        }

        for (int j = 0; j < height - 1; j++) {
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
        }

        int[] trackIndices = new int[height]; // 最終的に返す array
        trackIndices[height - 1] = minIndex;

        if (width == 1) {
            for (int i = 0; i < height - 1; i++) {
                trackIndices[(height - 1) - i] = 0;
            }
            return trackIndices;
        }

        double d1;
        double d2;
        double d3;

        int trackIndex = minIndex;

        for (int i = 1; i < height; i++) {
            if (trackIndex == 0) {
                d1 = Double.POSITIVE_INFINITY;
                d2 = energyPaths[trackIndex][(height - 1) - i];
                d3 = energyPaths[trackIndex + 1][(height - 1) - i];
            } else if (trackIndex == width - 1) {
                d1 = energyPaths[trackIndex - 1][(height - 1) - i];
                d2 = energyPaths[trackIndex][(height - 1) - i];
                d3 = Double.POSITIVE_INFINITY;
            } else {
                d1 = energyPaths[trackIndex - 1][(height - 1) - i];
                d2 = energyPaths[trackIndex][(height - 1) - i];
                d3 = energyPaths[trackIndex + 1][(height - 1) - i];
            }
            int indexOfMin = minOfThreeIndex(d1, d2, d3);
            trackIndex = trackIndex + indexOfMin;
            trackIndices[(height - 1) - i] = trackIndex;

        }

        return trackIndices;

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
        if (d1 <= d2 && d1 <= d3) {
            return -1;
        } else if (d2 <= d3 && d2 <= d1) {
            return 0;
        } else {
            return 1;
        }
    }

    public void removeHorizontalSeam(int[] seam) {

    }
    public void removeVerticalSeam(int[] seam) {
        int[] indices = findVerticalSeam();
    }
}


