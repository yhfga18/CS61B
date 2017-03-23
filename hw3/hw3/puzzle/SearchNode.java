package hw3.puzzle;

import java.util.Comparator;

/**
 * Created by hideyoshitakahashi on 3/20/17.
 */
public class SearchNode<T> implements Comparator<T> {
    private WorldState thisWS;
    private SearchNode previousSN;
    private int numOfMove; //num Of Move From Initial
    private int priority; // sum of (
                  // the number of moves made to reach this world state from the initial state
                  // + the WorldState's estimatedDistanceToGoal).
    //WorldState thisWS;
    public SearchNode(WorldState ws, SearchNode previous) {
        thisWS = ws;
        previousSN = previous;
        if (previous == null) {
            numOfMove = 0;
        } else {
            numOfMove = previous.numOfMove() + 1;
        }
    }

    public int numOfMove() {
        return numOfMove;
    }

    public WorldState current() {
        return thisWS;
    }

    public SearchNode previous() {
        return previousSN;
    }

    int estimatedDistanceToGoal() {
        return thisWS.estimatedDistanceToGoal();
    }

    Iterable<WorldState> neighbors() {
        return (Iterable<WorldState>) thisWS.neighbors();
    }

    public boolean isGoal() {
        return thisWS.isGoal();
    }

    public int compare(T obj1, T obj2) {
        SearchNode ws1 = (SearchNode) obj1;
        SearchNode ws2 = (SearchNode) obj2;
        int ws1dis = ws1.estimatedDistanceToGoal() + ws1.numOfMove;
        int ws2dis = ws2.estimatedDistanceToGoal() + ws2.numOfMove;
        return ws1dis - ws2dis;
        //ws2.estimatedDistanceToGoal();

    }

    public boolean equals(Object obj) {
        return obj == this;
    }

    public int hashCode() {
        return ((Object) this).hashCode();
    }
}
