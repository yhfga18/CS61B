//import java.util.Map;
//import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
//import java.util.List;
//import java.util.Set;
//import java.util.HashSet;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a LinkedList of <code>Long</code>s representing the shortest path from st to dest,
     * where the longs are node IDs.
     */
    static class NodeComparator implements Comparator<Long> {
        GraphDB graph;
        NodeComparator(GraphDB g) {
            graph = g;
        }
        public int compare(Long n1, Long n2) {
            double node1F = graph.getNode(n1).getF();
            double node2F = graph.getNode(n2).getF();
            if (node1F > node2F) {
                return 1;
            } else if (node1F < node2F) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public static LinkedList<Long> shortestPath(GraphDB g, double stlon,
                                                double stlat, double destlon, double destlat) {

        LinkedList<Long> resultList = new LinkedList<>();

        long initial = g.closest(stlon, stlat);
        long goal = g.closest(destlon, destlat);

        PriorityQueue<Long> minPQ = new PriorityQueue<>(new NodeComparator(g));

        // set initial's d & h. No need initial's f to be set.
        g.setDistance(initial, initial);
        g.setHeuristic(initial, goal);

        // add initial to MinPQ
        minPQ.add(initial);


        //Map<Long, Long> path = new HashMap<>(); // path
        HashSet<Long> visited = new HashSet<>(); // visited node

        //path.put(initial.getId(), initial.getId());

        long currentID = minPQ.poll();

        while (true) {

            if (currentID == goal) {
                break;
            }

            visited.add(currentID);

            for (Long v: g.adjacent(currentID)) {
                if (visited.contains(v)) {
                    continue;
                }
                if (minPQ.contains(v)) {
                    if (g.getHypoScore(currentID, v, goal) < g.getF(v)) {
                        g.setDistance(currentID, v);
                        g.setHeuristic(v, goal);
                        minPQ.add(v);
                        g.setParent(currentID, v);
                    }
                } else {
                    g.setDistance(currentID, v);
                    g.setHeuristic(v, goal);
                    minPQ.add(v);
                    g.setParent(currentID, v);
                }
            }
            if (minPQ.isEmpty()) {
                break;
            }
            currentID = minPQ.poll();
        }
        return pathMaker(g, goal, initial, resultList);
    }

    public static LinkedList<Long> pathMaker(GraphDB g, long currentID,
                                             long initial, LinkedList resultList) {
        resultList.addLast(currentID);
        while (currentID != initial) {
            currentID = g.getParent(currentID);
            resultList.addFirst(currentID);
        }
        return resultList;
    }
    /*
    public static LinkedList<Long> pathMaker(Map<Long, Long> path, Node current, Node goal) {
        Long start = current.getId();
        Long tracking = goal.getId();
        LinkedList<Long> resultList = new LinkedList<>();
        while (!(path.get(tracking).equals(tracking))
                && !(path.get(tracking).equals(start))) {

            resultList.addFirst(tracking);
            tracking = path.get(tracking);
        }
        resultList.addFirst(tracking);
        return resultList;
    }
    */
}
