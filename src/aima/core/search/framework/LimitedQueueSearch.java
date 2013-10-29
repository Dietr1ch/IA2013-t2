package aima.core.search.framework;
//package ai.hw2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.Node;
import aima.core.search.framework.NodeExpander;
import aima.core.search.framework.Problem;
import aima.core.search.framework.SearchUtils;
import aima.core.util.CancelableThread;
import aima.core.util.datastructure.Queue;

/**
 * BEWARE: This class works, but it is unused and not easy to integrate with the rest of the library.
 * 
 * @author Dietrich Daroch
 */
public abstract class LimitedQueueSearch extends NodeExpander {
  
  public static final String METRIC_QUEUE_SIZE     = "queueSize";
  public static final String METRIC_MAX_QUEUE_SIZE = "maxQueueSize";
  public static final String METRIC_PATH_COST      = "pathCost";
  
  
  private Queue<Node> frontier = null;
  private boolean checkGoalBeforeAddingToFrontier = false;

  public boolean isFailure(List<Action> result) {
    return 0 == result.size();
  }
  
  
  
  
  public List<Action> localSearch(Object current, Problem problem, Queue<Node> frontier, int lookaheadLimit){
    this.frontier = frontier;

    clearInstrumentation();
    // initialize the frontier using the current state of the problem
    Node root = new Node(current);
    if (isCheckGoalBeforeAddingToFrontier()) {
      if (SearchUtils.isGoalState(problem, root)) {
        return SearchUtils.actionsFromNodes(root.getPathFromRoot());
      }
    }
    frontier.insert(root);
    setQueueSize(frontier.size());
    int expansions = 0;
    double bestPathCost = Double.POSITIVE_INFINITY;
    List<Action> bestPathFound = new LinkedList<Action>();
    
    
    while (!(frontier.isEmpty()) && !CancelableThread.currIsCanceled() && expansions<=lookaheadLimit) {
      
      // choose a leaf node and remove it from the frontier
      Node nodeToExpand = popNodeFromFrontier();
      setQueueSize(frontier.size());
      
      //nodeToExpand.
      double newPathCost = nodeToExpand.getPathCost();
      if (newPathCost<bestPathCost){
        bestPathCost  = newPathCost;
        bestPathFound = SearchUtils.actionsFromNodes(nodeToExpand
            .getPathFromRoot());
      }
      
      
      
      
      
      // Only need to check the nodeToExpand if have not already
      // checked before adding to the frontier
      if (!isCheckGoalBeforeAddingToFrontier()) {
        // if the node contains (is) a goal state then return the
        // corresponding solution
        if (SearchUtils.isGoalState(problem, nodeToExpand)) {
          setPathCost(nodeToExpand.getPathCost());
          return SearchUtils.actionsFromNodes(nodeToExpand
              .getPathFromRoot());
        }
      }
      // expand the chosen node, adding the resulting nodes to the
      // frontier
      expansions++;
      for (Node fn : getResultingNodesToAddToFrontier(nodeToExpand,problem)) {
        if (isCheckGoalBeforeAddingToFrontier()) {
          if (SearchUtils.isGoalState(problem, fn)) {
            setPathCost(fn.getPathCost());
            return SearchUtils.actionsFromNodes(fn
                .getPathFromRoot());
          }
        }
        frontier.insert(fn);
      }
      setQueueSize(frontier.size());
    }
    
    if(frontier.isEmpty())
      return failure();
    
    return bestPathFound;
  }
  

  /**
   * Returns a list of actions to the goal if the goal was found, a list
   * containing a single NoOp Action if already at the goal, or an empty list
   * if the goal could not be found.
   * 
   * @param problem
   *            the search problem
   * @param frontier
   *            the collection of nodes that are waiting to be expanded
   * 
   * @return a list of actions to the goal if the goal was found, a list
   *         containing a single NoOp Action if already at the goal, or an
   *         empty list if the goal could not be found.
   */
  public List<Action> search(Problem problem, Queue<Node> frontier) {
    
    List<Action> path = new LinkedList<Action>();
    Object currentState = problem.getInitialState();
    Node currentNode = new Node(currentState);
        
    while(!problem.isGoalState(currentState)){
      List<Action> nextMoves = localSearch(currentNode, problem, frontier, 32); 
      for(Action a : nextMoves)
        currentState = problem.getResultFunction().result(currentState, a);
      currentNode = new Node(currentState);
      path.addAll(nextMoves);
    }
    return path;
  }
  
  

  public boolean isCheckGoalBeforeAddingToFrontier() {
    return checkGoalBeforeAddingToFrontier;
  }

  public void setCheckGoalBeforeAddingToFrontier(
      boolean checkGoalBeforeAddingToFrontier) {
    this.checkGoalBeforeAddingToFrontier = checkGoalBeforeAddingToFrontier;
  }

  /**
   * Removes and returns the node at the head of the frontier.
   * 
   * @return the node at the head of the frontier.
   */
  public Node popNodeFromFrontier() {
    return frontier.pop();
  }

  public boolean removeNodeFromFrontier(Node toRemove) {
    return frontier.remove(toRemove);
  }

  public abstract List<Node> getResultingNodesToAddToFrontier(
      Node nodeToExpand, Problem p);

  @Override
  public void clearInstrumentation() {
    super.clearInstrumentation();
    metrics.set(METRIC_QUEUE_SIZE, 0);
    metrics.set(METRIC_MAX_QUEUE_SIZE, 0);
    metrics.set(METRIC_PATH_COST, 0);
  }

  public int getQueueSize() {
    return metrics.getInt("queueSize");
  }

  public void setQueueSize(int queueSize) {

    metrics.set(METRIC_QUEUE_SIZE, queueSize);
    int maxQSize = metrics.getInt(METRIC_MAX_QUEUE_SIZE);
    if (queueSize > maxQSize) {
      metrics.set(METRIC_MAX_QUEUE_SIZE, queueSize);
    }
  }

  public int getMaxQueueSize() {
    return metrics.getInt(METRIC_MAX_QUEUE_SIZE);
  }

  public double getPathCost() {
    return metrics.getDouble(METRIC_PATH_COST);
  }

  public void setPathCost(Double pathCost) {
    metrics.set(METRIC_PATH_COST, pathCost);
  }

  //
  // PRIVATE METHODS
  //
  private List<Action> failure() {
    return Collections.emptyList();
  }
}