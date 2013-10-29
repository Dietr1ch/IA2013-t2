/**
 * @author Jorge Baier, based on EightPuzzleBoard by Ravi Mohan & R. Lunde
 */
package aima.core.environment.sliders;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Vector;

import aima.core.agent.Action;


public class SlidersBoard  {

	private static LinkedHashSet<Action> allActions;
	private static Vector<Action> allActionsVector;//Used by the random generator
	private int[] state;
	/**
	 * Size of the current puzzles, this should be an instance variable :C
	 */
	private static int SIZE;
	private int first_time = 1;

	public static void initActions(int length) {
		setSIZE((int) Math.sqrt(length));
		allActions = new LinkedHashSet<Action>();
		allActionsVector = new Vector<Action>();
		for (int i = 0; i < getSIZE(); i++) {
			allActions.add(new SlidersAction(SlidersActionType.UP, i));
			allActions.add(new SlidersAction(SlidersActionType.DOWN, i));
			allActions.add(new SlidersAction(SlidersActionType.LEFT, i));
			allActions.add(new SlidersAction(SlidersActionType.RIGHT, i));
			allActionsVector.add(new SlidersAction(SlidersActionType.UP, i));
			allActionsVector.add(new SlidersAction(SlidersActionType.DOWN, i));
			allActionsVector.add(new SlidersAction(SlidersActionType.LEFT, i));
			allActionsVector.add(new SlidersAction(SlidersActionType.RIGHT, i));
		}
	}

	public SlidersBoard(int[] initialState) {

		if (first_time == 1) {
			initActions(initialState.length);
			first_time = 0;
		}
		setState(new int[getSIZE() * getSIZE()]);

		System.arraycopy(initialState, 0, getState(), 0, getSIZE() * getSIZE());
	}

	public SlidersBoard(SlidersBoard board) {

		if (first_time == 1) {
			initActions(board.getState().length);
			first_time = 0;
		}
		setState(new int[getSIZE() * getSIZE()]);

		System.arraycopy(board.getState(), 0, getState(), 0, getSIZE() * getSIZE());
	}

	public static LinkedHashSet<Action> getAllActions() {
		return allActions;
	}

	public int getYCoord(int absPos) {
		return absPos / getSIZE();
	}

	public int getXCoord(int absPos) {
		return absPos % getSIZE();
	}

	public int getAbsPosition(int x, int y) {
		return y * getSIZE() + x;
	}

	public int getValueAt(int x, int y) {
		// refactor this use either case or a div/mod soln
		return getState()[getAbsPosition(x, y)];
	}

	public int getPositionOf(int val) {
		int retVal = -1;
		for (int i = 0; i < state.length; i++) {
			if (state[i] == val) {
				retVal = i;
			}
		}
		return retVal;
	}

	private void setValue(int x, int y, int val) {
		int absPos = getAbsPosition(x, y);
		getState()[absPos] = val;

	}

	@Override
	public String toString() {
		String string = new String();

		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				string += String.format("%4d", getValueAt(x, y));
			}
			string += "\n";
		}
		return string;

	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if ((o == null) || (this.getClass() != o.getClass()))
			return false;

		SlidersBoard aBoard = (SlidersBoard) o;
		return Arrays.equals(getState(), aBoard.getState());
	}

	@Override
	public int hashCode() {
		int result = 0;
		int cellCount = state.length;
		
		for (int i : state)
			result = cellCount * result + i - 1;
		
		return result;
	}

	public int actionSign(SlidersAction action) {
		if (action.getType() == SlidersActionType.UP || action.getType() == SlidersActionType.LEFT)
			return -1;
		if (action.getType() == SlidersActionType.DOWN || action.getType() == SlidersActionType.RIGHT)
			return 1;
		return 0;
	}

	public int actionIndex(SlidersAction action) {
		return action.getIndex();
	}

	public void doMove(SlidersAction action) {

		if (action.getType() == SlidersActionType.UP || action.getType() == SlidersActionType.DOWN) {

			int xIndex = actionIndex(action);
			int sign = actionSign(action);

			Vector<Integer> value = new Vector<Integer>();

			// value stores the values in column xIndex

			for (int i = 0; i < getSIZE(); i++) {
				value.add(getValueAt(xIndex, i));
			}

			for (int i = 0; i < getSIZE(); i++) {
				setValue(xIndex, (getSIZE() + i + sign) % getSIZE(), value.elementAt(i));
			}
		}

		if (action.getType() == SlidersActionType.RIGHT || action.getType() == SlidersActionType.LEFT) {

			int yIndex = actionIndex(action);
			int sign = actionSign(action);

			Vector<Integer> value = new Vector<Integer>();

			for (int i = 0; i < getSIZE(); i++) {
				value.add(getValueAt(i, yIndex));
			}

			for (int i = 0; i < getSIZE(); i++) {
				setValue((getSIZE() + i + sign) % getSIZE(), yIndex, value.elementAt(i));
			}
		}

	}

	public void doRandomMove() {
		int randomPos = (int) (Math.random() * allActionsVector.size());
		SlidersAction a = (SlidersAction) allActionsVector.elementAt(randomPos);
		doMove(a);
	}

	public static int getSIZE() {
		return SIZE;
	}

	public static void setSIZE(int sIZE) {
		SIZE = sIZE;
	}

	public int[] getState() {
		return state;
	}

	public void setState(int[] state) {
		this.state = state;
	}

}
