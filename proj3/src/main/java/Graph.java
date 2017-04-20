//import java.util.*;
//
//
///**
// * Created by hideyoshitakahashi on 4/17/17.
// */
//
//public class Graph {
//    Map<Long, Node> nodes;
//    //Map<Long, Way> ways;
//    double Rullat = MapServer.ROOT_ULLAT;
//    double Rullon = MapServer.ROOT_ULLON;
//    double Rlrlat = MapServer.ROOT_LRLAT;
//    double Rlrlon =  MapServer.ROOT_LRLON;
//
//    HashMap<Long, LinkedList<Long>> adjacencyList;
//
//    public Graph() {
//        nodes = new HashMap<>();
//        //ways = new HashMap<>();
//        adjacencyList = new HashMap();
//    }
//
//    Iterable<Long> vertices() {
//        //YOUR CODE HERE, this currently returns only an empty list.
//        LinkedList<Long> l = new LinkedList<>();
//        for (Map.Entry<Long, LinkedList<Long>> entry : adjacencyList.entrySet()) {
//            if (entry.getValue() != null) {
//                l.add(entry.getKey());
//            }
//        }
//        return l;
//    }
//
//    Iterable<Long> adjacent(long nodeId) {
//        if (adjacencyList.containsKey(nodeId)) {
//            if (!(adjacencyList.get(nodeId) == null)) {
//                return adjacencyList.get(nodeId);
//            }
//        }
//        return new LinkedList<Long>();
//    }
//
//
//    double distance(long node1, long node2) {
//        Node n1 = nodes.get(node1);
//        Node n2 = nodes.get(node2);
//        return distance(n1, n2);
//    }
//
//    double distance(Node node1, Node node2) {
//        double lon1 = node1.getLon();
//        double lad1 = node1.getLat();
//        double lon2 = node2.getLon();
//        double lad2 = node2.getLat();
//        return Math.sqrt((lon1 - lon2) *  (lon1 - lon2) + (lad1 - lad2) * (lad1 - lad2));
//    }
//
//    double distance(double lon1, double lad1, Node node2) {
//        double lon2 = node2.getLon();
//        double lat2 = node2.getLat();
//        return Math.sqrt((lon1 - lon2) *  (lon1 - lon2) + (lad1 - lat2) * (lad1 - lat2));
//    }
//
//    long closest(double lon, double lat) {
//        Node closestNode = null;
//        double minDistance = 99999999999.9;
//
//        for (Map.Entry<Long, Node> entry : nodes.entrySet()) {
//            Node n = entry.getValue();
//            if ((Math.abs(n.lat - lat) < 0.005) && (Math.abs(n.lon - lon)) < 0.005) {
//                double dist = distance(lon, lat, n);
//                if (minDistance > dist) {
//                    minDistance = dist;
//                    closestNode = n;
//                }
//            }
//        }
//
//        /*
//        for (Long n : adjacent(closestNode.getId())) {
//            double d = distance(nodes.get(n), node);
//            if (minDistance > d) {
//                minDistance = d;
//                closestNode = nodes.get(n);
//            }
//        }
//        */
//
//        return closestNode.getId();
//    }
//
//
//    Node closestNode(double lon, double lat) {
//        return getNode(closest(lon, lat));
//    }
//
//
//    public void clean() { // theta(N)
//        for (Map.Entry<Long, LinkedList<Long>> entry : adjacencyList.entrySet()) {
//            if (entry.getValue() == null) {
//                removeNode(entry.getKey());
//            }
//
//            /*
//            if (entry.getValue().size() == 0) {
//                removeNode(entry.getKey());
//            }
//            */
//        }
//    }
//
//    public void addNode(Node node) {
//        if (!(nodes.containsKey(node.getId()))) {
//            nodes.put(node.getId(), node);
//            //char f = Long.toString(node.getId()).charAt(0);
//        }
//
//        if (!(adjacencyList.containsKey(node.getId()))) {
//            adjacencyList.put(node.getId(), null);
//        }
//
//    }
//
//    public Node getNode(long id) {
//        if (nodes.containsKey(id)) {
//            return nodes.get(id);
//        }
//        return null;
//    }
//
//    public void removeNode(Long nodeID) {
//        if ((nodes.containsKey(nodeID))) {
//            nodes.remove(nodeID);
//            //char f = Long.toString(nodeID).charAt(0);
//            //nodes.containsKey(nodeID);
////            adjacencyList.put(nodeID, null);
//        }
//    }
//
//    public void addEdge(long node1, long node2) {
//        if (!(nodes.containsKey(node1) && nodes.containsKey(node2))) {
//            return;
//        }
//
//        if (adjacencyList.get(node1) == null) {
//            LinkedList<Long> list = new LinkedList<>();
//            adjacencyList.put(node1, list);
//        }
//        adjacencyList.get(node1).add(node2);
//
//        if (adjacencyList.get(node2) == null) {
//            LinkedList<Long> list = new LinkedList<>();
//            adjacencyList.put(node2, list);
//        }
//        adjacencyList.get(node2).add(node1);
//
//    }
//    /** Longitude of vertex v. */
//    double lon(long v) {
//        return nodes.get(v).getLon();
//    }
//
//    /** Latitude of vertex v. */
//    double lat(long v) {
//        return nodes.get(v).getLat();
//    }
//
//    boolean contains(String id) {
//        return nodes.containsKey(Long.parseLong(id));
//    }
//
//}
//
//
//
//
//
//*/
//
//
//
//
//
//class Node {
//    long id;
//    double lon;
//    double lat;
//    double distFromSource;
//    double heuristic;
//    double f;
//
//    public Node(String id, String lon, String lat) {
//        this.id = Long.parseLong(id);
//        this.lon = Double.parseDouble(lon);
//        this.lat = Double.parseDouble(lat);
//        distFromSource = 9999999999999.9;
//    }
//    public long getId() {
//        return id;
//    }
//    public double getLon() {
//        return lon;
//    }
//    public double getLat() {
//        return lat;
//    }
//
//
//    public double getDistFromSource() {
//        return distFromSource;
//    }
//    public void setDistFromSource(Node prev) {
//        distFromSource = distFromSource + prev.getDistFromSource();
//    }
//    public void setDistFromSource(double d) {
//        distFromSource = d;
//    }
//    public double getHeuristic() {
//        return heuristic;
//    }
//    public void setHeuristic(double h) {
//        heuristic = h;
//    }
//    public double getF() {
//        return f;
//    }
//    public void setF() {
//        f = distFromSource + heuristic;
//    }
//}
//
//
//class Way {
//    long wayId;
//    LinkedList<Long> nodes;
//    Graph graph;
//
//    public Way(GraphDB g, String wayID) {
//        graph = g.graph;
//        wayId = Long.parseLong(wayID);
//        nodes = new LinkedList<>();
//    }
//
//    public void addNodeToWay(String nodeId) {
//        nodes.addLast(Long.parseLong(nodeId));
//    }
//
//    public void addEdgeToNodes() {
//        if (nodes.size() >= 1) {
//            for (int i = 0; i < nodes.size() - 1 ; i++) {
//                graph.addEdge(nodes.get(i), nodes.get(i + 1));
//            }
//        }
//    }
//
//
//}
//*/
//
//*/
