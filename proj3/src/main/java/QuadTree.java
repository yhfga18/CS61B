/**
 * Created by hideyoshitakahashi on 4/14/17.
 */

import java.util.HashMap;
import java.util.Map;

public class QuadTree {
    public class Node implements Comparable<Node>{
        private String filename;
        private int depth;
        private Map<String, Double> picScale;
        private Node sub1, sub2, sub3, sub4;

        public Node(String name, int dep) {
            filename = name;
            depth = dep;
            picScale = new HashMap<String, Double>();
//            sub1 = null;
//            sub2 = null;
//            sub3 = null;
//            sub4 = null;

        }

        public Node(String name, int dep, Map<String, Double> scale) {
            filename = name;
            depth = dep;
            picScale = scale;
//            sub1 = null;
//            sub2 = null;
//            sub3 = null;
//            sub4 = null;
        }

        public int compareTo(Node node) {
            return Integer.parseInt(this.filename) - Integer.parseInt(node.filename);
        }

        public Map<String, Double> getPicScale() {
            return picScale;
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

    public Map<String, Double> getNodeValues(Node node) {
        return node.picScale;
    }

    private void scaleCalculator(Node node, Node parentNode) {
        /*
        if (node.filename.length() < 1 || node.filename.equals("root")) {
            node.picScale.put("ullat", parentNode.picScale.get("ULLAT")); //upper left lati 横
            node.picScale.put("ullon", parentNode.picScale.get("ULLON")); //upper left long 縦
            node.picScale.put("lrlat", parentNode.picScale.get("LRLAT")); //lower right lati
            node.picScale.put("lrlon", parentNode.picScale.get("LRLON")); //lower right long
        }
        */
        //else {
            char lastChar = node.filename.charAt(node.filename.length() - 1);
            if (lastChar == '1') {
                node.picScale.put("ullat", parentNode.picScale.get("ullat"));
                node.picScale.put("ullon", parentNode.picScale.get("ullon"));
                node.picScale.put("lrlat", (parentNode.picScale.get("ullat") + parentNode.picScale.get("lrlat")) / 2);
                node.picScale.put("lrlon", (parentNode.picScale.get("ullon") + parentNode.picScale.get("lrlon")) / 2);
            } else if (lastChar == '2') {
                node.picScale.put("ullat", parentNode.picScale.get("ullat"));
                node.picScale.put("ullon", (parentNode.picScale.get("ullon") + parentNode.picScale.get("ullon")) / 2);
                node.picScale.put("lrlat", (parentNode.picScale.get("ullat") + parentNode.picScale.get("lrlat")) / 2);
                node.picScale.put("lrlon", parentNode.picScale.get("lrlon"));
            } else if (lastChar == '3') {
                node.picScale.put("ullat", (parentNode.picScale.get("ullat") + parentNode.picScale.get("ullat")) / 2);
                node.picScale.put("ullon", parentNode.picScale.get("ullon"));
                node.picScale.put("lrlat", parentNode.picScale.get("lrlat"));
                node.picScale.put("lrlon", (parentNode.picScale.get("ullon") + parentNode.picScale.get("lrlon")) / 2);
            } else if (lastChar == '4') {
                node.picScale.put("ullat", (parentNode.picScale.get("ullat") + parentNode.picScale.get("ullat")) / 2);
                node.picScale.put("ullon", (parentNode.picScale.get("ullon") + parentNode.picScale.get("ullon")) / 2);
                node.picScale.put("lrlat", parentNode.picScale.get("lrlat"));
                node.picScale.put("lrlon", parentNode.picScale.get("lrlon"));
            }
        node.picScale.put("w", 256.0);
        //}
    }

    public Node getRoot() {
        return root;
    }
//    private void scaleCalcHelper(Node node, Node parentNode, ) {}
}


/*
    public void addSubs(Node node, String s1, String s2, String s3, String s4) {
        node.sub1 = new Node(s1);
        node.sub2 = new Node(s2);
        node.sub3 = new Node(s3);
        node.sub4 = new Node(s4);
    }

    public void addSubs(Node node) {
        node.sub1 = new Node(node.filename + "1");
        node.sub2 = new Node(node.filename + "2");
        node.sub3 = new Node(node.filename + "3");
        node.sub4 = new Node(node.filename + "4");
    }

    public void addSubN(int n) {
        addSubN(n, root);
    }

    public void addSubN(int n, Node p) {
        if (n < 1) {return;}
        addSubs(p);
        addSubN(n-1, p.sub1);
        addSubN(n-1, p.sub2);
        addSubN(n-1, p.sub3);
        addSubN(n-1, p.sub4);

   }
*/