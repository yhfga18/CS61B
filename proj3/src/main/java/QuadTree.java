/**
 * Created by hideyoshitakahashi on 4/14/17.
 */

import java.util.HashMap;
import java.util.Map;

public class QuadTree {
    public int counter;
    public class Node implements Comparable<Node>{
        private String filename;
        private long filenameLong;
        private int depth;
        private double ULLAT;
        private double ULLON;
        private double LRLAT;
        private double LRLON;
        private double Width;
        private double Height;
        private double LonDPP;

        private Node sub1, sub2, sub3, sub4;

        public Node(long name, int dep) {
            counter += 1;
            filenameLong = name;
            depth = dep;
        }

        public Node(String name, int dep, Map<String, Double> scale) {
            filename = name;
            filenameLong = 0;
            depth = dep;
            ULLAT = scale.get("ullat");
            ULLON = scale.get("ullon");
            LRLAT = scale.get("lrlat");
            LRLON = scale.get("lrlon");
            Width = scale.get("w");
        }

        public int compareTo(Node node) {
            return Integer.parseInt(this.filename) - Integer.parseInt(node.filename);
        }

        public double getULLAT() {
            return ULLAT;
        }
        public double getULLON() {
            return ULLON;
        }
        public double getLRLAT() {
            return LRLAT;
        }
        public double getLRLON() {
            return LRLON;
        }
        public double getWidth() {
            return Width;
        }
        public void setULLAT(double val) {
            ULLAT = val;
        }
        public void setULLON(double val) {
            ULLON = val;
        }
        public void setLRLAT(double val) {
            LRLAT = val;
        }
        public void setLRLON(double val) {
            LRLON = val;
        }
        public void setWidth(double val) {
            Width = val;
        }

        public String getFilename() {
            return filename;
        }
        public long getFilenameLong() {
            return filenameLong;
        }

        public Node getSub1() {
            return sub1;
        }

        public Node getSub2() {
            return sub2;
        }

        public Node getSub3() {
            return sub3;
        }

        public Node getSub4() {
            return sub4;
        }
    }

    private Node root;

    public QuadTree(Map<String, Double> scales) { //String rootName
        counter = 0;
        root = new Node("root", 0, scales);
        addSubsToRoot(root, 1, 2, 3, 4);
        addSubN(1, root.sub1);
        addSubN(1, root.sub2);
        addSubN(1, root.sub3);
        addSubN(1, root.sub4);
    }

    private void addSubsToRoot(Node node, long s1, long s2, long s3, long s4) {
        node.sub1 = new Node(s1, 1);
        node.sub2 = new Node(s2, 1);
        node.sub3 = new Node(s3, 1);
        node.sub4 = new Node(s4, 1);
        scaleCalculator(node.sub1, node);
        scaleCalculator(node.sub2, node);
        scaleCalculator(node.sub3, node);
        scaleCalculator(node.sub4, node);
    }

    private void addSubs(Node node, int n) {
        node.sub1 = new Node(node.filenameLong*10 + 1, n);
        node.sub2 = new Node(node.filenameLong*10 + 2, n);
        node.sub3 = new Node(node.filenameLong*10 + 3, n);
        node.sub4 = new Node(node.filenameLong*10 + 4, n);
        scaleCalculator(node.sub1, node);
        scaleCalculator(node.sub2, node);
        scaleCalculator(node.sub3, node);
        scaleCalculator(node.sub4, node);

    }

    private void addSubN(int n, Node p) {
        if (n > 6) {return;}
        addSubs(p, n);
        addSubN(n + 1, p.sub1);
        addSubN(n + 1, p.sub2);
        addSubN(n + 1, p.sub3);
        addSubN(n + 1, p.sub4);
    }

    private void scaleCalculator(Node node, Node parentNode) {
        long lastNum = node.filenameLong % 10;
        //char lastChar = node.filename.charAt(node.filename.length() - 1);
        if (lastNum == 1) {
            node.setULLAT(parentNode.getULLAT());
            node.setULLON(parentNode.getULLON());
            node.setLRLAT((parentNode.getULLAT() + parentNode.getLRLAT()) / 2);
            node.setLRLON((parentNode.getULLON() + parentNode.getLRLON()) / 2);
        } else if (lastNum == 2) {
            node.setULLAT(parentNode.getULLAT());
            node.setULLON((parentNode.getULLON() + parentNode.getLRLON()) / 2);
            node.setLRLAT((parentNode.getULLAT() + parentNode.getLRLAT()) / 2);
            node.setLRLON(parentNode.getLRLON());
        } else if (lastNum == 3) {
            node.setULLAT((parentNode.getULLAT() + parentNode.getLRLAT()) / 2);
            node.setULLON(parentNode.getULLON());
            node.setLRLAT(parentNode.getLRLAT());
            node.setLRLON((parentNode.getULLON() + parentNode.getLRLON()) / 2);
        } else if (lastNum == 4) {
            node.setULLAT((parentNode.getULLAT() + parentNode.getLRLAT()) / 2);
            node.setULLON((parentNode.getULLON() + parentNode.getLRLON()) / 2);
            node.setLRLAT(parentNode.getLRLAT());
            node.setLRLON(parentNode.getLRLON());
        }
        node.setWidth(256.0);
    }

    public Node getRoot() {
        return root;
    }

    public Node getNode(long fileName) {
        return getHelper(root, fileName,0);
    }

    private Node getHelper(Node node, long fileName, int level) {
        if (level > 7) {
            return null;
        }
        if (node.getFilenameLong() == fileName) {
            return node;
        }
        char match = Long.toString(fileName).charAt(level);
        if (match ==  '1') {
            return getHelper(node.getSub1(), fileName, level + 1);
        } else if (match == '2') {
            return getHelper(node.getSub2(), fileName, level + 1);
        } else if (match == '3') {
            return getHelper(node.getSub3(), fileName, level + 1);
        } else if (match == '4') {
            return getHelper(node.getSub4(), fileName, level + 1);
        }
        return null;
    }
}
/*
    public Node getNode(String fileName) {
        return getHelper(root, fileName, 0);
    }

    private Node getHelper(Node node, String fileName, int level) {
        if (level > 7) {
            return null;
        }
        if (node.getFilename().equals(fileName)) {
            return node;
        }
        if (fileName.charAt(level) == '1') {
            return getHelper(node.getSub1(), fileName, level + 1);
        } else if (fileName.charAt(level) == '2') {
            return getHelper(node.getSub2(), fileName, level + 1);
        } else if (fileName.charAt(level) == '3') {
            return getHelper(node.getSub3(), fileName, level + 1);
        } else if (fileName.charAt(level) == '4') {
            return getHelper(node.getSub4(), fileName, level + 1);
        }
        return null;
    }
*/