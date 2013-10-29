package aima.core.environment.generalizedEightPuzzle;

import aima.core.search.framework.GoalTest;

/**
 * @author Ravi Mohan
 * 
 */
public class GeneralizedEightPuzzleGoalTest implements GoalTest {
  
  
	GeneralizedEightPuzzleBoard goal = GeneralizedEightPuzzleBoard.getGoal();

	public boolean isGoalState(Object state) {
		GeneralizedEightPuzzleBoard board = (GeneralizedEightPuzzleBoard) state;
		return board.equals(goal);
	}
}