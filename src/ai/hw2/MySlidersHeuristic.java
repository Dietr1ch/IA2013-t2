package ai.hw2;

import ai.utils.Utils;
import aima.core.environment.sliders.SlidersBoard;
import aima.core.search.framework.HeuristicFunction;

public class MySlidersHeuristic implements HeuristicFunction {

  @Override
  public double h(Object state) {
    double h = 0;
    try {
      SlidersBoard board = (SlidersBoard) state;
      h = h(board);
    } catch (Exception e) {
      Utils.err.println("MySlidersHeuristic.h(double) :"+e);
      return Integer.MAX_VALUE;
    }

    return h;
  }

  static int h(SlidersBoard board) {

    //Loads _size and _map
    loadMap(board);
    
    //TODO: implement a better heuristic
    
    for(int y = 0; y < _size; y++)
      for(int x = 0; x < _size; x++)
        if (wrappingManhattanDistance(x, y) > 0) //Non goal
          return 1;
    
    return 0;
  }
  
  
  
  
  static int _size;
  static int[][] map;
  static void loadMap(SlidersBoard board) {
    _size = SlidersBoard.getSIZE();
    map = new int[_size][_size];
    for(int y = _size - 1; y >= 0; y--)
      for(int x = 0; x < _size; x++)
        map[x][y] = board.getValueAt(x, y);
  }
  
  
  
  /**
   * Returns the WMD of a number cell and it's final position.
   */
  static int wrappingManhattanDistance(int x, int y) {
    int num = map[x][y];
    num--;

    int numX = num % _size;
    int numY = num / _size;

    int dist = wrappingManhattanDistance(x, y, numX, numY);
    return dist;
  }
  
  /**
   * Returns the WMD between a pair of points. 
   */
  static int wrappingManhattanDistance(int x1, int y1, int x2, int y2) {
    
    // Get Manhattan Distance
    int dx = (x1 - x2);
    if (dx < 0)
      dx = -dx;


    int dy = (y1 - y2);
    if (dy < 0)
      dy = -dy;
    
    // Wrap
    if (_size - dx < dx)
      dx = _size - dx;
    if (_size - dy < dy)
      dy = _size - dy;

    return dx + dy;
  }

}
