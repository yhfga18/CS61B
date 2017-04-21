import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
//import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;


/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    Map<Long, Node> nodes;
    Map<Integer, Set<Long>> actualNodes;
    Set<Long> vertices;
    //Map<Long, Way> ways;
    double rullat = MapServer.ROOT_ULLAT;
    double rullon = MapServer.ROOT_ULLON;
    double rlrlat = MapServer.ROOT_LRLAT;
    double rlrlon =  MapServer.ROOT_LRLON;
    double HALF_LON = -122.255859;
    Node latestNode;
    Way latestEdge;

    HashMap<Long, LinkedList<Long>> adjacencyList;

    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */

    public GraphDB(String dbPath) {
        nodes = new HashMap<>();
        //ways = new HashMap<>();
        adjacencyList = new HashMap();
        vertices = new HashSet<>();
        actualNodes = new HashMap<Integer, Set<Long>>();
        actualNodes.put(1, new HashSet<Long>());
        actualNodes.put(-1, new HashSet<Long>());

        try {
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        // clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    /*
    private void clean() {
        graph.clean();
    }
    */
    public void clean() { // theta(N)
        for (Map.Entry<Long, LinkedList<Long>> entry : adjacencyList.entrySet()) {
            if (entry.getValue() == null) {
                removeNode(entry.getKey());
            }

            /*
            if (entry.getValue().size() == 0) {
                removeNode(entry.getKey());
            }
            */
        }
    }


    /*
    public HashMap<Long, LinkedList<Long>> adjacencyList() {
        return graph.adjacencyList;
    }
    */

    /** Returns an iterable of all vertex IDs in the graph. */
    /*Iterable<Long> vertices() {
        return graph.vertices();
    }
    */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        /*
        LinkedList<Long> l = new LinkedList<>();
        for (Map.Entry<Long, LinkedList<Long>> entry : adjacencyList.entrySet()) {
            if (entry.getValue() != null) {
                l.add(entry.getKey());
            }
        }
        return l;
        */

        return vertices;
    }

    /** Returns ids of all vertices adjacent to v. */
    Iterable<Long> adjacent(long nodeId) {
        LinkedList<Long> list = adjacencyList.get(nodeId);
        if (!(list == null)) {
            return list;
        }
        return new LinkedList<Long>();
    }

    /** Returns the Euclidean distance between vertices v and w, where Euclidean distance
     *  is defined as sqrt( (lonV - lonV)^2 + (latV - latV)^2 ). */
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

    double distance(double lon1, double lad1, Node node2) {
        double lon2 = node2.getLon();
        double lat2 = node2.getLat();
        return Math.sqrt((lon1 - lon2) *  (lon1 - lon2) + (lad1 - lat2) * (lad1 - lat2));
    }


    /** Returns the vertex id closest to the given longitude and latitude. */
    /*
    long closest(double lon, double lat) {
        return graph.closest(lon, lat);
    }

    Node closestNode(double lon, double lat) {
        return graph.closestNode(lon, lat);
    }
    */
    long closest(double lon, double lat) {
        Node closestNode = null;
        double minDistance = 99999999999.9;
        Set<Long> nodeSet;
        if (lon < HALF_LON) {
            nodeSet = actualNodes.get(1);
        } else {
            nodeSet = actualNodes.get(-1);
        }
        //for (Map.Entry<Long, Node> entry : nodes.entrySet()) {
        for (long entry : nodeSet) {
            //if ((Math.abs(n.lat - lat) < 0.03) && (Math.abs(n.lon - lon)) < 0.03) {
            Node n = nodes.get(entry);
            double dist = distance(lon, lat, n);
            if (minDistance > dist) {
                minDistance = dist;
                closestNode = n;
            }
            //}
        }

        /*
        for (Long n : adjacent(closestNode.getId())) {
            double d = distance(nodes.get(n), node);
            if (minDistance > d) {
                minDistance = d;
                closestNode = nodes.get(n);
            }
        }
        */

        return closestNode.getId();
    }


    Node closestNode(double lon, double lat) {
        return getNode(closest(lon, lat));
    }

    /** Longitude of vertex v. */
    /*
    double lon(long v) {
        return graph.lon(v);
    }

    double lat(long v) {
        return graph.lat(v);
    }
    */

    double lon(long v) {
        return nodes.get(v).getLon();
    }

    /** Latitude of vertex v. */
    double lat(long v) {
        return nodes.get(v).getLat();
    }


    /*
    public void addNode(Node node) {
        graph.addNode(node);
    }
    */
    public void addNode(Node node) {
        if (!(nodes.containsKey(node.getId()))) {
            nodes.put(node.getId(), node);
            //char f = Long.toString(node.getId()).charAt(0);
        }

        /* ------------------------------------------------------------
        if (!(adjacencyList.containsKey(node.getId()))) {
            adjacencyList.put(node.getId(), null);
        }
        */

    }

    public void removeNode(Long nodeID) {
        if ((nodes.containsKey(nodeID))) {
            nodes.remove(nodeID);
            //char f = Long.toString(nodeID).charAt(0);
            //nodes.containsKey(nodeID);
//            adjacencyList.put(nodeID, null);
        }
    }
    /*
    public Node getNode(long id) {
        return graph.getNode(id);
    }
    */

    public Node getNode(long id) {
        if (nodes.containsKey(id)) {
            return nodes.get(id);
        }
        return null;
    }
    /*
    public void addEdge(long node1, long node2) {
        graph.addEdge(node1, node2);
    }
    */
    public void addEdge(long node1, long node2) {
        if (!(nodes.containsKey(node1) && nodes.containsKey(node2))) {
            return;
        }

        addEdgeHelper(node1, node2);
        addEdgeHelper(node2, node1);
        /*
        if (adjacencyList.get(node1) == null) {
            LinkedList<Long> list = new LinkedList<>();
            adjacencyList.put(node1, list);
        }
        adjacencyList.get(node1).add(node2);

        if (adjacencyList.get(node2) == null) {
            LinkedList<Long> list = new LinkedList<>();
            adjacencyList.put(node2, list);
        }
        adjacencyList.get(node2).add(node1);
        */

    }

    public void addEdgeHelper(long node1, long node2) {
        if (!(adjacencyList.containsKey(node1))) {
            vertices.add(node1);
            LinkedList<Long> l = new LinkedList<Long>();
            l.add(node2);
            adjacencyList.put(node1, l);

            if (nodes.get(node1).lon < HALF_LON) {
                actualNodes.get(1).add(node1);
            } else {
                actualNodes.get(-1).add(node1);
            }
        } else {
            adjacencyList.get(node1).add(node2);
        }
    }
    /*
    public boolean contains(String id) {
        return graph.contains(id);
    }
    */

    boolean contains(String id) {
        return nodes.containsKey(Long.parseLong(id));
    }


    public void setParent(long current, long child) {
        Node childNode = nodes.get(child);
        childNode.setParent(current);
    }

    public long getParent(long current) {
        Node currentNode = nodes.get(current);
        return currentNode.getParent(current);
    }

}


