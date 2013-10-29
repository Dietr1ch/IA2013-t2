package ai.hw2;

import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.Node;
import aima.core.search.informed.AStarEvaluationFunction;

/**
 * A* lookahead ranking function.
 * Usually, 
 * <pre>
 *        f(n) = g(n) + h(n).
 * </pre>
 * 
 * @author Dietrich Daroch
 * based on the work of Ciaran O'Reilly and Mike Stampone.
 */
public class MyAStarEvaluationFunction extends AStarEvaluationFunction {
  
  public MyAStarEvaluationFunction(HeuristicFunction hf) {
    super(hf);
  }
  
  /**
   * Returns <em>g(n)</em> the cost to reach the node, plus <em>h(n)</em> the
   * heuristic cost to get from the specified node to the goal.
   * 
   * @param n
   *            a node
   * @return g(n) + h(n)
   */
  public double f(Node n) {
    double g = gf.g(n);
    double h = hf.h(n.getState());
    
    //TODO: Review the raking function.
    
    double f_0 = g+h;
    //double f_1 = g+h+1;
    
    //Utils.err.format("\nMyAstar: Using %3f instead of %3f\n", f_1, f_0);

    return f_0;
    //return f_1;
  }
}
