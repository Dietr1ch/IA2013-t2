/**
 * @author Jorge Baier, based on EightPuzzleBoard by Ravi Mohan & R. Lunde
 */
package aima.core.environment.sliders;/**

 */

import aima.core.search.framework.GoalTest;

public class SlidersGoalTest implements GoalTest {

	public boolean isGoalState(Object state) {
		SlidersBoard board = (SlidersBoard) state;

		int boardState[] = board.getState();

		for (int i = 0; i < boardState.length; i++) {
			if (boardState[i] != i + 1)
				return false;
		}
		return true;
	}
}
