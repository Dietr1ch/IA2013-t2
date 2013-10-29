package ai.hw2;

import ai.utils.NoSolutionFoundException;
import ai.utils.Utils;
import aima.core.environment.sliders.SlidersBoard;
import aima.core.environment.sliders.SlidersBoardFunctionFactory;
import aima.core.environment.sliders.SlidersGoalTest;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;

public class IA_tarea2 {

  // problem??
  static Problem problem;

  public static void runMySearch(int[] b, int randomMoves) {

    try {
      SlidersBoard board = new SlidersBoard(b);

      for (int i = 0; i < randomMoves; i++)
        board.doRandomMove();

      Utils.out.println(board);

      problem = new Problem(board,
          SlidersBoardFunctionFactory.getActionsFunction(),
          SlidersBoardFunctionFactory.getResultFunction(),
          new SlidersGoalTest());

      HeuristicFunction h = new MySlidersHeuristic();
      Search s = new MySearch(h);
      runSearch(s);
    } catch (NoSolutionFoundException e) {
      Utils.out.println("No se encontró una solución");
    } catch (Exception e) {
      e.printStackTrace();
      Utils.out.println("Esto es lamentable");
    }
  }

  static void runSearch(Search s) throws Exception {
    SearchAgent a = new SearchAgent(problem, s);
    Utils.out.println("Search Statistics:");
    printStats(a);
  }

  static void printStats(SearchAgent agent) {
    Utils.out.format("Largo Plan=%d\n" + "Nodos Expandidos=%s\n", agent
        .getActions().size(),
        agent.getInstrumentation().getProperty("nodesExpanded"));
    Utils.out.format("Plan=%s\n", agent.getActions());
    Utils.out.println();
  }

  public static void main(String[] args) {
    int[] b1  = { 8, 7, 9, 4, 3, 1, 6, 2, 5 }; // 5
    int[] b2  = { 4, 8, 3, 9, 2, 7, 1, 5, 6 }; // 6
    int[] b3  = { 4, 3, 1, 7, 5, 6, 2, 8, 9 }; // 2
    int[] b4  = { 3, 5, 9, 8, 2, 6, 4, 1, 7 }; // 7
    int[] b5  = { 2, 7, 8, 5, 4, 1, 6, 9, 3 }; // 7
    int[] b6  = { 2, 9, 7, 5, 8, 1, 6, 3, 4 }; // 6
    int[] b7  = { 1, 3, 2, 5, 4, 6, 7, 8, 9 }; // 6
    int[] b8  = { 2, 7, 4, 1, 6, 9, 5, 3, 8 }; // 7
    int[] b9  = { 7, 6, 4, 1, 3, 5, 2, 8, 9 }; // 7
    int[] b10 = { 6, 4, 5, 3, 8, 1, 9, 2, 7 }; // 8
    int[] b11 = { 6, 7, 9, 1, 8, 3, 4, 2, 5 };

    int[] b20 = {  1,  2,  3,  4,  5,  6, 7, 8,  9, 10, 11, 12, 13, 14, 15, 16 };
    int[] b21 = { 10, 14, 13, 12,  8, 11, 3, 5,  6,  7,  4, 16,  9,  1,  2, 15 };
    int[] b22 = {  2,  4,  1, 13, 15,  5, 3, 6,  9, 10,  7, 16, 14, 12, 11,  8 };
    int[] b23 = { 13,  1,  2, 15,  8,  5, 6, 3, 12, 10,  7, 16,  9, 14, 11,  4 };

    // TODO: Test harder instances.
    int[][] testInstances = { b3, b11, b22, b23 };
    // int[][] testInstances={b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11};

    int i = 0;
    for (int[] w : testInstances) {
      Utils.out.format("Instance %d:\n", ++i);
      runMySearch(w, 0);
    }
  }

}
