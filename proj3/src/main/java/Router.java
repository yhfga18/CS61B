import java.util.Map;
import java.util.HashMap;
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

    static class NodeComparator implements Comparator<Node> {
        Node goal;
        NodeComparator(Node destination) {
            goal = destination;
        }

        public int compare(Node node1, Node node2) {
            if (node1.getF() > node2.getF()) {
                return 1;
            } else if (node1.getF() < node2.getF()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public static LinkedList<Long> shortestPath(GraphDB g, double stlon,
                                                double stlat, double destlon, double destlat) {

        LinkedList<Long> resultList = new LinkedList<>();
        Node current;

        Node initial = g.closestNode(stlon, stlat);
        Node goal = g.closestNode(destlon, destlat);
        PriorityQueue<Node> minPQ = new PriorityQueue<>(new NodeComparator(goal));

        // set initial's d & h. No need initial's f to be set.
        initial.setDistFromSource(0);
        initial.setHeuristic(heuristic(g, initial, goal));

        // add initial to MinPQ
        minPQ.add(initial);


        Map<Long, Long> path = new HashMap<>(); // path
        HashSet<Node> visited = new HashSet<>(); // visited node
        path.put(initial.getId(), initial.getId());

        while (!(minPQ.isEmpty())) {
            current = minPQ.poll(); // MinPQ's smallest pulled
            resultList.addLast(current.getId());

            if (current.getId() == (goal.getId())) {
                resultList = pathMaker(path, current, goal);
                return resultList;
            }

            visited.add(current);

            for (Long neig : g.adjacent(current.getId())) {
                Node neighbor = g.getNode(neig);
                neighbor.setHeuristic(heuristic(g, neighbor, goal));

                if (visited.contains(neighbor)) {
                    continue;
                }

                double distToNeighbor = g.distance(current.getId(), neighbor.getId());
                double distanceSoFar = distToNeighbor + current.getDistFromSource();

                if (neighbor.getDistFromSource() > distanceSoFar) {
                    neighbor.setDistFromSource(distanceSoFar);
                    neighbor.setF();
                    path.put(neighbor.getId(), current.getId());
                    minPQ.add(neighbor);
                }
            }
        }
        return null;
    }

    public static double heuristic(GraphDB g, Node current, Node destination) {
        return g.distance(current.getId(), destination.getId());
    }

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
}
