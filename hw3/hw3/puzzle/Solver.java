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
    ArrayList<WorldState> solution;
    Stack<WorldState> stack;
    int numOfMoveToGoal;

    //constructor
    public Solver(WorldState initial) {
        sn = new SearchNode<>(initial, null);
        fringe = new MinPQ<SearchNode>((Comparator) sn);
        solution = new ArrayList<>();
        stack = new Stack<>();

        fringe.insert(sn);
        while (!(fringe.isEmpty())) {
            SearchNode currentStep = fringe.delMin();
            numOfMoveToGoal = currentStep.numOfMove();
            if (currentStep.isGoal()) {
                solution = solutionMaker(stack, currentStep);
                break;
            }
            for (Object neighbor : currentStep.neighbors()) {
                //SearchNode updatedNeighbor = update(currentStep, (SearchNode) neighbor);
                SearchNode newNode = new SearchNode((WorldState) neighbor, currentStep);
                fringe.insert(newNode);
            }
        }
    }

    private ArrayList<WorldState> solutionMaker(Stack s, SearchNode current) {
        while (current != null) {
            s.push(current.current());
            current = current.previous();
        }
        ArrayList<WorldState> solution = new ArrayList<>();
        while (!(stack.isEmpty())) {
            solution.add((WorldState) s.pop());
        }
        return solution;
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
        return numOfMoveToGoal;
        /*
        Iterable<WorldState> i =solution();
        int counter = 0;
        for (WorldState w : i) {
            counter++;
        }
        counter -= 1;
        return counter;
        */
    }
    /*
    Returns the minimum number of moves to solve the puzzle starting
    at the initial WorldState.
    */

    public Iterable<WorldState> solution() {
        return solution;
        /*
        SearchNode pointer = sn;
        Stack<WorldState> sol = new Stack<>();
        while (pointer != null) {
            sol.push(pointer.current());
            pointer = pointer.previous();
        }
        //Collections.reverse(sol);
        return sol;
        */
    }
    /*
    Returns a sequence of WorldStates from the initial WorldState
    to the solution.
    */
}
