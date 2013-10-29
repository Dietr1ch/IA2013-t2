/**
 * @author Jorge Baier, based on EightPuzzleBoard by Ravi Mohan & R. Lunde
 */
package aima.core.environment.sliders;

import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.*;


public class SlidersBoardFunctionFactory {

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
			return SlidersBoard.getAllActions();
		}
	}
	
	private static class EPResultFunction implements ResultFunction {
			public Object result(Object s, Action a) {				
				SlidersBoard newBoard = new SlidersBoard((SlidersBoard) s);
				newBoard.doMove((SlidersAction) a);
				return newBoard;
			}

	}
}
