import java.util.*;


/**
 * Created by hideyoshitakahashi on 4/17/17.
 */
public class Graph {
    Map<Long, Node> nodes;
    HashMap<Long, LinkedList<Long>> adjacencyList;

    public Graph() {
        nodes = new HashMap<>();
        adjacencyList = new HashMap();
    }

    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        LinkedList<Long> l = new LinkedList<>();
        for (Map.Entry<Long, LinkedList<Long>> entry : adjacencyList.entrySet()) {
            if (entry.getValue().size() != 0) {
                l.add(entry.getKey());
            }
        }
        return l;
    }

    Iterable<Long> adjacent(long nodeId) {
        if (adjacencyList.containsKey(nodeId)) {
            return adjacencyList.get(nodeId);
        }
        return null;
    }


    double distance(long node1, long node2) {
        Node n1 = nodes.get(node1);
        Node n2 = nodes.get(node2);
        return distance(n1, n2);
    }

    double distance(Node node1, Node node2) {
        double lon1 = node1.getLon();
        double lad1 = node1.getLat();
        double lon2 = node2.getLon();
        double lad2 = node2.getLat();
        return Math.sqrt((lon1 - lon2) *  (lon1 - lon2) + (lad1 - lad2) * (lad1 - lad2));
    }

    long closest(double lon, double lat) {
        Node node = new Node("0", Double.toString(lon), Double.toString(lat));
        Node closestNode = null;
        double minDistance = 36000*18000;
        for (Map.Entry<Long, Node> entry : nodes.entrySet()) {
            Node n = entry.getValue();
            double dist = distance(node, n);
            if (minDistance >= dist) {
                minDistance = dist;
                closestNode = n;
            }
        }
        return closestNode.getId();
    }

    Node closestNode(double lon, double lat) {
        return getNode(closest(lon, lat));
    }

    public void clean() { // theta(N)
        for (Map.Entry<Long, LinkedList<Long>> entry : adjacencyList.entrySet()) {
            if (entry.getValue().size() == 0) {
                removeNode(entry.getKey());
            }
        }
    }

    public void addNode(Node node) {
        if (!(nodes.containsKey(node.getId()))) {
            nodes.put(node.getId(), node);
        }
        if (!(adjacencyList.containsKey(node.getId()))) {
            adjacencyList.put(node.getId(), new LinkedList<>());
        }
    }

    public void addNode(String node) {
        Node n = nodes.get(Long.parseLong(node));
        addNode(n);
    }


    public Node getNode(long id) {
        if (nodes.containsKey(id)) {
            return nodes.get(id);
        }
        return null;
    }

    public Node getNode(String id) {
        long d = Long.parseLong(id);
        Node returnNode =  nodes.get(d);
        return returnNode;
    }

    public void addEdge(Node node1, Node node2) {
        long id1 = node1.getId();
        long id2 = node2.getId();
        addEdge(id1, id2);
    }

    public void removeNode(Long nodeID) {
        if ((nodes.containsKey(nodeID))) {
            nodes.remove(nodeID);
            //nodes.containsKey(nodeID);
        }
    }

    public void addEdge(long node1, long node2) {
        if (!(nodes.containsKey(node1) && nodes.containsKey(node2))) {
            return;
        }
        if (!(adjacencyList.get(node1).contains(node2))) {
            adjacencyList.get(node1).add(node2);
        }
        if (!(adjacencyList.get(node2).contains(node1))) {
            adjacencyList.get(node2).add(node1);
        }
    }
    /** Longitude of vertex v. */
    double lon(long v) {
        return nodes.get(v).getLon();
    }

    /** Latitude of vertex v. */
    double lat(long v) {
        return nodes.get(v).getLat();
    }

    boolean contains(String id) {
        return nodes.containsKey(Long.parseLong(id));
    }

    boolean contains(long id) {
        return nodes.containsKey(id);
    }

    public void takeLists(Node node) {
        /*
        LinkedList<Long> b = adjacencyList.get(node.getId());
        b = new LinkedList<Long>();
    */ //WTF
    }

    public boolean containNode(Node node) {
        return nodes.containsKey(node.getId());
    }

    public boolean containNodeAdj(Node node) {
        return adjacencyList.containsKey(node.getId());
    }

}

class Node {
    long id;
    double lon;
    double lat;
    double distFromSource;
    double heuristic;
    double f;

    public Node(String id, String lon, String lat) {
        this.id = Long.parseLong(id);
        this.lon = Double.parseDouble(lon);
        this.lat = Double.parseDouble(lat);
        distFromSource = 99999999999999999.9;
    }
    public long getId() {
        return id;
    }
    public double getLon() {
        return lon;
    }
    public double getLat() {
        return lat;
    }

    public double getDistFromSource() {
        return distFromSource;
    }

    public void setDistFromSource(Node prev) {
        distFromSource = distFromSource + prev.getDistFromSource();
    }
    public void setDistFromSource(double d) {
        distFromSource = d;
    }

    public double getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(double h) {
        heuristic = h;
    }


    public double getF() {
        return f;
    }

    public void setF() {
        f = distFromSource + heuristic;
    }

}

