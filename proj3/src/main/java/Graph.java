import java.util.*;


/**
 * Created by hideyoshitakahashi on 4/17/17.
 */
public class Graph {
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
        Node n1 = groupIDMatcher(whichGroupContains(node1)).get(node1);
        Node n2 = groupIDMatcher(whichGroupContains(node2)).get(node2);
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

        Map<Long, Node>  nodeSet = groupIDMatcher(nodesSelector(lon, lat, Rullon, Rullat, Rlrlon, Rlrlat));

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

    private int nodesSelector(double lon, double lat, double ullon, double ullat,
                                          double lrlon, double lrlat) {
        int groupID;
        boolean c1 = lat > (lrlat + ullat) / 2;
        boolean c2 = lon > (ullon + lrlon) / 2;
        if (c1 && !c2) {
            groupID = 1;
        } else if (c1 && c2) {
            groupID = 2;
        } else if (!c1 && !c2) {
            groupID = 3;
        } else {//if (lat < (lrlat + ullat) / 2 && lon > (ullon + lrlon) / 2) {
            groupID = 4;
        }
        return groupID;
    }

    private Map<Long, Node> groupIDMatcher(int groupID) {
        if (groupID == 1) {
            return nodes1;
        } else if (groupID == 2) {
            return nodes2;
        } else if (groupID == 3) {
            return nodes3;
        } else {
            return nodes4;
        }
    }

    private int whichGroupContains(long id) {
        if (nodes1.containsKey(id)) {
            return 1;
        } else if (nodes2.containsKey(id)) {
            return 2;
        } else if (nodes3.containsKey(id)) {
            return 3;
        } else {
            return 4;
        }
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
        //if (!(nodes.containsKey(node.getId()))) {
            //nodes.put(node.getId(), node);
            Double lon = node.getLon();
            Double lat = node.getLat();
            int groupID = nodesSelector(lon, lat, Rullon, Rullat, Rlrlon, Rlrlat);
            groupIDMatcher(groupID).put(node.getId(), node);
            node.setGroupID(groupID);
        //}

        if (!(adjacencyList.containsKey(node.getId()))) {
            adjacencyList.put(node.getId(), new LinkedList<>());
        }
    }

    public Node getNode(long id) {
        int groupID = whichGroupContains(id);
        Map<Long, Node> m = groupIDMatcher(groupID);
        return m.get(id);
    }

    public void removeNode(Long nodeID) {
        //if ((nodes.containsKey(nodeID))) {
            //Node node = nodes.get(nodeID);
            //nodes.remove(nodeID);
            Node node = groupIDMatcher(whichGroupContains(nodeID)).get(nodeID);
            Double lon = node.getLon();
            Double lat = node.getLat();
            int groupID = nodesSelector(lon, lat, Rullon, Rullat, Rlrlon, Rlrlat);
            groupIDMatcher(groupID).remove(node.getId(), node);
        //}
    }

    public void addEdge(long node1, long node2) {
        boolean cond1 = groupIDMatcher(whichGroupContains(node1)).containsKey(node1);
        boolean cond2 = groupIDMatcher(whichGroupContains(node2)).containsKey(node2);

        if (!(cond1 && cond2)) {
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
        return groupIDMatcher(whichGroupContains(v)).get(v).getLon();
    }

    /** Latitude of vertex v. */
    double lat(long v) {
        return groupIDMatcher(whichGroupContains(v)).get(v).getLat();
    }

    boolean contains(String id) {
        return groupIDMatcher(whichGroupContains(Long.parseLong(id))).containsKey(Long.parseLong(id));
    }

    public void addWay(Way way) {
        ways.put(way.wayId, way);
    }

}

class Node {
    int groupID;
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

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int id) {
        groupID = id;
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
        int len = nodes.size();
        /*
        if (len > 2) {
            Long n = nodes.removeFirst();
            while (len > 1) {
                graph.addEdge(n, nodes.removeFirst());
            }
            */
        if (len > 1) {
            for (int i = 0; i < len - 1; i++) {
                graph.addEdge(nodes.get(i), nodes.get(i + 1));
            }
        }
    }


}

