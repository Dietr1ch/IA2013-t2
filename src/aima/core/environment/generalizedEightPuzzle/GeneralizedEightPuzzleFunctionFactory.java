package aima.core.environment.generalizedEightPuzzle;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.ResultFunction;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class GeneralizedEightPuzzleFunctionFactory {
	private static ActionsFunction _actionsFunction = null;
	private static ResultFunction _resultFunction = null;

	public static ActionsFunction getActionsFunction() {
		if (null == _actionsFunction) {
			_actionsFunction = new EPActionsFunction();
		}
		return _actionsFunction;
	}

	public static ResultFunction getResultFunction() {
		if (null == _resultFunction) {
			_resultFunction = new EPResultFunction();
		}
		return _resultFunction;
	}

	private static class EPActionsFunction implements ActionsFunction {
		public Set<Action> actions(Object state) {
			GeneralizedEightPuzzleBoard board = (GeneralizedEightPuzzleBoard) state;

			Set<Action> actions = new LinkedHashSet<Action>();

			if (board.canMoveGap(GeneralizedEightPuzzleBoard.UP)) {
				actions.add(GeneralizedEightPuzzleBoard.UP);
			}
			if (board.canMoveGap(GeneralizedEightPuzzleBoard.DOWN)) {
				actions.add(GeneralizedEightPuzzleBoard.DOWN);
			}
			if (board.canMoveGap(GeneralizedEightPuzzleBoard.LEFT)) {
				actions.add(GeneralizedEightPuzzleBoard.LEFT);
			}
			if (board.canMoveGap(GeneralizedEightPuzzleBoard.RIGHT)) {
				actions.add(GeneralizedEightPuzzleBoard.RIGHT);
			}

			return actions;
		}
	}

	private static class EPResultFunction implements ResultFunction {
		public Object result(Object s, Action a) {
			GeneralizedEightPuzzleBoard board = (GeneralizedEightPuzzleBoard) s;

			if (GeneralizedEightPuzzleBoard.UP.equals(a)
					&& board.canMoveGap(GeneralizedEightPuzzleBoard.UP)) {
				GeneralizedEightPuzzleBoard newBoard = new GeneralizedEightPuzzleBoard(board);
				newBoard.moveGapUp();
				return newBoard;
			} else if (GeneralizedEightPuzzleBoard.DOWN.equals(a)
					&& board.canMoveGap(GeneralizedEightPuzzleBoard.DOWN)) {
				GeneralizedEightPuzzleBoard newBoard = new GeneralizedEightPuzzleBoard(board);
				newBoard.moveGapDown();
				return newBoard;
			} else if (GeneralizedEightPuzzleBoard.LEFT.equals(a)
					&& board.canMoveGap(GeneralizedEightPuzzleBoard.LEFT)) {
				GeneralizedEightPuzzleBoard newBoard = new GeneralizedEightPuzzleBoard(board);
				newBoard.moveGapLeft();
				return newBoard;
			} else if (GeneralizedEightPuzzleBoard.RIGHT.equals(a)
					&& board.canMoveGap(GeneralizedEightPuzzleBoard.RIGHT)) {
				GeneralizedEightPuzzleBoard newBoard = new GeneralizedEightPuzzleBoard(board);
				newBoard.moveGapRight();
				return newBoard;
			}

			// The Action is not understood or is a NoOp
			// the result will be the current state.
			return s;
		}
	}
}