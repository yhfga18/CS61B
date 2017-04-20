/**
 * Created by hideyoshitakahashi on 4/14/17.
 */

import java.util.HashMap;
import java.util.Map;

public class QuadTree {
    public int counter;
    public class Node implements Comparable<Node>{
        private String filename;
        private int depth;
        private Map<String, Double> picScale;
        private double ULLAT;
        private double ULLON;
        private double LRLAT;
        private double LRLON;
        private double Width;
        private double Height;
        private double LonDPP;

        private Node sub1, sub2, sub3, sub4;

        public Node(String name, int dep) {
            counter += 1;
            filename = name;
            depth = dep;
            picScale = new HashMap<String, Double>();
        }

        public Node(String name, int dep, Map<String, Double> scale) {
            filename = name;
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

        public Map<String, Double> getPicScale() {
            return picScale;
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

        public int getDepth() {
            return depth;
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
        addSubsToRoot(root, "1", "2", "3", "4");
        addSubN(1, root.sub1);
        addSubN(1, root.sub2);
        addSubN(1, root.sub3);
        addSubN(1, root.sub4);
    }

    private void addSubsToRoot(Node node, String s1, String s2, String s3, String s4) {
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
        node.sub1 = new Node(node.filename + "1", n);
        node.sub2 = new Node(node.filename + "2", n);
        node.sub3 = new Node(node.filename + "3", n);
        node.sub4 = new Node(node.filename + "4", n);
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
            char lastChar = node.filename.charAt(node.filename.length() - 1);
            if (lastChar == '1') {
                node.setULLAT(parentNode.getULLAT());
                node.setULLON(parentNode.getULLON());
                node.setLRLAT((parentNode.getULLAT() + parentNode.getLRLAT()) / 2);
                node.setLRLON((parentNode.getULLON() + parentNode.getLRLON()) / 2);
            } else if (lastChar == '2') {
                node.setULLAT(parentNode.getULLAT());
                node.setULLON((parentNode.getULLON() + parentNode.getLRLON()) / 2);
                node.setLRLAT((parentNode.getULLAT() + parentNode.getLRLAT()) / 2);
                node.setLRLON(parentNode.getLRLON());
            } else if (lastChar == '3') {
                node.setULLAT((parentNode.getULLAT() + parentNode.getLRLAT()) / 2);
                node.setULLON(parentNode.getULLON());
                node.setLRLAT(parentNode.getLRLAT());
                node.setLRLON((parentNode.getULLON() + parentNode.getLRLON()) / 2);
            } else if (lastChar == '4') {
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
}

