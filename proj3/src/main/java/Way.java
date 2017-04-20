import java.util.LinkedList;

/**
 * Created by hideyoshitakahashi on 4/20/17.
 */

public class Way {
    long wayId;
    LinkedList<Long> nodes;
    GraphDB g;

    public Way(GraphDB g, String wayID) {
        this.g = g;
        wayId = Long.parseLong(wayID);
        nodes = new LinkedList<>();
    }

    public void addNodeToWay(String nodeId) {
        nodes.addLast(Long.parseLong(nodeId));
    }

    public void addEdgeToNodes() {
        if (nodes.size() >= 1) {
            for (int i = 0; i < nodes.size() - 1 ; i++) {
                g.addEdge(nodes.get(i), nodes.get(i + 1));
            }
        }
    }


}
