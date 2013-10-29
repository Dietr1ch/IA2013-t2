package aima.core.environment.generalizedEightPuzzle;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;
import aima.core.util.datastructure.XYLocation;

/**
 * 
 * Generalization of the 8Puzzle to N^2-1 Puzzle
 * 
 * @author Dietrich Daroch
 * based on the work of Ravi Mohan and R. Lunde.
 */
public class GeneralizedEightPuzzleBoard {

  public static int size   = 3;
  public static int square = size*size;
  
	public static Action LEFT  = new DynamicAction("Left");
	public static Action RIGHT = new DynamicAction("Right");
	public static Action UP    = new DynamicAction("Up");
	public static Action DOWN  = new DynamicAction("Down");
	

	private int[] state;
	//
	// PUBLIC METHODS
	//

	public GeneralizedEightPuzzleBoard() {
	  
	  state = new int[square];
	  
	  //state = new int[] { 5, 4, 0, 6, 1, 8, 7, 3, 2 };
	  for (int i = 0; i < size*size; i++)
      state[i]=i;
	}
	
	public GeneralizedEightPuzzleBoard buildEightPuzzleBoard(int[] state){
	  
	  int sq = state.length;
	  int sz = (int)Math.rint(Math.sqrt(sq));
	  
	  if(sz*sz!=sq)
	    return null;
	  
	  int[] occurrences = new int[sq];
	  for (int i : state)
	    occurrences[i]++;
	  
	  for (int o : occurrences)
	    if(o!=1)
	      return null;
	  
	  GeneralizedEightPuzzleBoard nb = new GeneralizedEightPuzzleBoard();
	  nb.state = state;
	  
	  return nb;
	}
	
	
	public static void setSize(int s){
	  size   = s;
	  square = s*s; 
	}
	

	public GeneralizedEightPuzzleBoard(int[] state) {
		this.state = new int[state.length];
		System.arraycopy(state, 0, this.state, 0, state.length);
	}

	public GeneralizedEightPuzzleBoard(GeneralizedEightPuzzleBoard copyBoard) {
		this(copyBoard.getState());
	}

	public int[] getState() {
		return state;
	}

	public int getValueAt(XYLocation loc) {
		return getValueAt(loc.getXCoOrdinate(), loc.getYCoOrdinate());
	}

	public XYLocation getLocationOf(int val) {
		int absPos = getPositionOf(val);
		return new XYLocation(getXCoord(absPos), getYCoord(absPos));
	}

	public void moveGapRight() {
		int gapPos = getGapPosition();
		int x = getXCoord(gapPos);
		int ypos = getYCoord(gapPos);
		if (!(ypos == size-1)) {
			int valueOnRight = getValueAt(x, ypos + 1);
			setValue(x, ypos, valueOnRight);
			setValue(x, ypos + 1, 0);
		}

	}

	public void moveGapLeft() {
		int gapPos = getGapPosition();
		int x = getXCoord(gapPos);
		int ypos = getYCoord(gapPos);
		if (!(ypos == 0)) {
			int valueOnLeft = getValueAt(x, ypos - 1);
			setValue(x, ypos, valueOnLeft);
			setValue(x, ypos - 1, 0);
		}

	}

	public void moveGapDown() {
		int gapPos = getGapPosition();
		int x = getXCoord(gapPos);
		int y = getYCoord(gapPos);
		if (!(x == size-1)) {
			int valueOnBottom = getValueAt(x + 1, y);
			setValue(x, y, valueOnBottom);
			setValue(x + 1, y, 0);
		}

	}

	public void moveGapUp() {
		int gapPos = getGapPosition();
		int x = getXCoord(gapPos);
		int y = getYCoord(gapPos);
		if (!(x == 0)) {
			int valueOnTop = getValueAt(x - 1, y);
			setValue(x, y, valueOnTop);
			setValue(x - 1, y, 0);
		}
	}

	public List<XYLocation> getPositions() {
		ArrayList<XYLocation> locations = new ArrayList<XYLocation>();

		for (int i = 0; i < square; i++) {
			int absPos = getPositionOf(i);
			
			int x = getXCoord(absPos);
			int y = getYCoord(absPos);
			XYLocation loc = new XYLocation(x, y);
			
			locations.add(loc);
		}
		return locations;
	}

	public void setBoard(List<XYLocation> locs) {
		int count = 0;
		for (int i = 0; i < locs.size(); i++) {
			XYLocation loc = locs.get(i);
			this.setValue(loc.getXCoOrdinate(), loc.getYCoOrdinate(), count);
			count = count + 1;
		}
	}

	public boolean canMoveGap(Action where) {
		boolean retVal = true;
		int absPos = getPositionOf(0);
		if (where.equals(LEFT))
			retVal = (getYCoord(absPos) != 0);
		else if (where.equals(RIGHT))
			retVal = (getYCoord(absPos) != size-1);
		else if (where.equals(UP))
			retVal = (getXCoord(absPos) != 0);
		else if (where.equals(DOWN))
			retVal = (getXCoord(absPos) != size-1);
		return retVal;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		GeneralizedEightPuzzleBoard aBoard = (GeneralizedEightPuzzleBoard) o;
		
		for (int i = 0; i < square-1; i++) {
			if (this.getPositionOf(i) != aBoard.getPositionOf(i)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 17;
		for (int i = 0; i < square-1; i++) {
			int position = this.getPositionOf(i);
			result = 37 * result + position;
		}
		return result;
	}

	@Override
	public String toString() {
		String retVal = state[0] + " " + state[1] + " " + state[2] + "\n"
				+ state[3] + " " + state[4] + " " + state[5] + " " + "\n"
				+ state[6] + " " + state[7] + " " + state[8];
		return retVal;
	}

	
	
	//
	// PRIVATE METHODS
	//
	/**
	 * Note: The graphic representation maps x values on row numbers (x-axis in
	 * vertical direction).
	 */
	private int getXCoord(int absPos) {
		return absPos / size;
	}

	/**
	 * Note: The graphic representation maps y values on column numbers (y-axis
	 * in horizontal direction).
	 */
	private int getYCoord(int absPos) {
		return absPos % size;
	}

	private int getAbsPosition(int x, int y) {
		return x * size + y;
	}

	private int getValueAt(int x, int y) {
		// refactor this use either case or a div/mod soln
		return state[getAbsPosition(x, y)];
	}

	private int getGapPosition() {
		return getPositionOf(0);
	}

	private int getPositionOf(int val) {
		int retVal = -1;
		for (int i = 0; i < square; i++) {
			if (state[i] == val) {
				retVal = i;
			}
		}
		return retVal;
	}

	private void setValue(int x, int y, int val) {
		int absPos = getAbsPosition(x, y);
		state[absPos] = val;

	}


  public static GeneralizedEightPuzzleBoard getGoal() {
    return new GeneralizedEightPuzzleBoard();
  }
}