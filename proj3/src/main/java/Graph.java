import java.util.*;


/**
 * Created by hideyoshitakahashi on 4/17/17.
 */
public class Graph {
    Map<Long, Node> nodes;
    Map<Long, Node> nodes1;
    Map<Long, Node> nodes2;
    Map<Long, Node> nodes3;
    Map<Long, Node> nodes4;
    Map<Long, Way> ways;
    double Rullat = MapServer.ROOT_ULLAT;
    double Rullon = MapServer.ROOT_ULLON;
    double Rlrlat = MapServer.ROOT_LRLAT;
    double Rlrlon =  MapServer.ROOT_LRLON;

    HashMap<Long, LinkedList<Long>> adjacencyList;

    public Graph() {
        nodes = new HashMap<>();
        nodes1 = new HashMap<>();
        nodes2 = new HashMap<>();
        nodes3 = new HashMap<>();
        nodes4 = new HashMap<>();
        ways = new HashMap<>();
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
        double minDistance = 999999999.9;
        //for (Map.Entry<Long, Node> entry : nodes.entrySet()) {

        Map<Long, Node>  nodeSet = nodesSelector(lon, lat, Rullon, Rullat, Rlrlon, Rlrlat);

        for (Map.Entry<Long, Node> entry : nodeSet.entrySet()) {
            Node n = entry.getValue();
            //if ((Math.abs(n.lat - node.lat) < 0.001) && (Math.abs(n.lon - node.lon)) < 0.001) {
                double dist = distance(node, n);
                if (minDistance >= dist) {
                    minDistance = dist;
                    closestNode = n;
                }
            //}
        }

        return closestNode.getId();
    }

    private Map<Long, Node> nodesSelector(double lon, double lat, double ullon, double ullat,
                                          double lrlon, double lrlat) {
        Map<Long, Node> nodeSet;
        boolean c1 = lat > (lrlat + ullat) / 2;
        boolean c2 = lon > (ullon + lrlon) / 2;
        if (c1 && !c2) {
            nodeSet = nodes1;
        } else if (c1 && c2) {
            nodeSet = nodes2;
        } else if (!c1 && !c2) {
            nodeSet = nodes3;
        } else {//if (lat < (lrlat + ullat) / 2 && lon > (ullon + lrlon) / 2) {
            nodeSet = nodes4;
        }
        return nodeSet;
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
            Double lon = node.getLon();
            Double lat = node.getLat();
            Map<Long, Node> nodeSet = nodesSelector(lon, lat, Rullon, Rullat, Rlrlon, Rlrlat);
            nodeSet.put(node.getId(), node);
        }
        if (!(adjacencyList.containsKey(node.getId()))) {
            adjacencyList.put(node.getId(), new LinkedList<>());
        }
    }

    public Node getNode(long id) {
        if (nodes.containsKey(id)) {
            return nodes.get(id);
        }
        return null;
    }

    public void removeNode(Long nodeID) {
        if ((nodes.containsKey(nodeID))) {
            Node node = nodes.get(nodeID);
            nodes.remove(nodeID);
            Double lon = node.getLon();
            Double lat = node.getLat();
            Map<Long, Node>  nodeSet = nodesSelector(lon, lat, Rullon, Rullat, Rlrlon, Rlrlat);
            nodeSet.remove(node.getId(), node);
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

    public void addWay(Way way) {
        ways.put(way.wayId, way);
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
        distFromSource = 9999999999999.9;
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

    public void setDistFromSource(double d) {
        distFromSource = d;
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


class Way {
    long wayId;
    LinkedList<Long> nodes;
    Graph graph;
    boolean isWay;

    public Way(GraphDB g, String wayID) {
        graph = g.graph;
        wayId = Long.parseLong(wayID);
        nodes = new LinkedList<>();
    }

    public void addNodeToWay(String nodeId) {
        nodes.addLast(Long.parseLong(nodeId));
    }

    public void addEdgeToNodes() {
        if (nodes.size() >= 1) {
            for (int i = 0; i < nodes.size() - 1 ; i++) {
                graph.addEdge(nodes.get(i), nodes.get(i + 1));
            }
        }
    }


}

