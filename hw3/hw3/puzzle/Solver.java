package hw3.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import java.util.*;

/**
 * Created by hideyoshitakahashi on 3/20/17.
 */
public class Solver {
    SearchNode sn;
    HashSet<SearchNode> searched;
    MinPQ<SearchNode> fringe;

    //constructor
    public Solver(WorldState initial) {
        searched = new HashSet<SearchNode>();
        sn = new SearchNode<>(initial, null);
        fringe = new MinPQ<SearchNode>((Comparator) sn);
        fringe.insert(sn);
        while (!(fringe.min().isGoal())) {
            SearchNode currentStep = fringe.delMin();
            searched.add(currentStep);
            for (Object neighbor : currentStep.neighbors()) {
                //SearchNode updatedNeighbor = update(currentStep, (SearchNode) neighbor);
                if (!(searched.contains(neighbor))) {
                    SearchNode newNode = new SearchNode((WorldState) neighbor, currentStep);
                    searched.add(newNode);
                    fringe.insert(newNode);
                }
            }
        }
    }

    public SearchNode update(SearchNode current, SearchNode neighbor) {
        return neighbor;
        //if (neighbor.numOfMove())
    }

    /*
    Constructor which solves the puzzle, computing
    everything necessary for moves() and solution() to
    not have to solve the problem again. Solves the
    puzzle using the A* algorithm. Assumes a solution exists.
    */

    public int moves() {
        Iterable<WorldState> i =solution();
        int counter = 0;
        for (WorldState w : i) {
            counter++;
        }
        counter -= 1;
        return counter;
    }
    /*
    Returns the minimum number of moves to solve the puzzle starting
    at the initial WorldState.
    */

    public Iterable<WorldState> solution() {
        SearchNode pointer = sn;
        LinkedList<WorldState> sol = new LinkedList<>();
        while (pointer != null) {
            sol.add(pointer.current());
            pointer = pointer.previous();
        }
        //Collections.reverse(sol);
        return sol;
    }
    /*
    Returns a sequence of WorldStates from the initial WorldState
    to the solution.
    */
}
