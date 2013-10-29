package aima.core.environment.generalizedEightPuzzle;

import aima.core.search.framework.HeuristicFunction;
import aima.core.util.datastructure.XYLocation;

/**
 * @author Dietrich Daroch
 * based on the work of Ravi Mohan
 */
public class ManhattanHeuristicFunction implements HeuristicFunction {

	public double h(Object state) {
		GeneralizedEightPuzzleBoard board = (GeneralizedEightPuzzleBoard) state;
		int retVal = 0;
		for (int i = 1; i < 9; i++) {
			XYLocation loc = board.getLocationOf(i);
			retVal += evaluateManhattanDistanceOf(i, loc);
		}
		return retVal;

	}

	public int evaluateManhattanDistanceOf(int i, XYLocation loc) {
		int retVal = -1;
		int xpos = loc.getXCoOrdinate();
		int ypos = loc.getYCoOrdinate();
		
		
		int targetX = i/GeneralizedEightPuzzleBoard.size;
		int targetY = i%GeneralizedEightPuzzleBoard.size;
		
		if(0<i && i<GeneralizedEightPuzzleBoard.square)
		  return Math.abs(xpos-targetX)+Math.abs(ypos-targetY); 
		
		
/*		switch (i) {
		case 1:
			retVal = Math.abs(xpos - 0) + Math.abs(ypos - 1);
			break;
		case 2:
			retVal = Math.abs(xpos - 0) + Math.abs(ypos - 2);
			break;
		case 3:
			retVal = Math.abs(xpos - 1) + Math.abs(ypos - 0);
			break;
		case 4:
			retVal = Math.abs(xpos - 1) + Math.abs(ypos - 1);
			break;
		case 5:
			retVal = Math.abs(xpos - 1) + Math.abs(ypos - 2);
			break;
		case 6:
			retVal = Math.abs(xpos - 2) + Math.abs(ypos - 0);
			break;
		case 7:
			retVal = Math.abs(xpos - 2) + Math.abs(ypos - 1);
			break;
		case 8:
			retVal = Math.abs(xpos - 2) + Math.abs(ypos - 2);
			break;
		}*/
		return retVal;
	}
}