package ai.hw2;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ai.utils.*;
import aima.core.agent.Action;
import aima.core.environment.sliders.*;
import aima.core.search.framework.*;
import aima.core.search.informed.*;

public class MySearch implements TimedSeach {
  
  public final long sec = 1000;
  static int _size;
  
  /**
   * Random/Greedy search depth limit, may be decreased overtime, but it should
   *  be big enough at the start
   */
  static int depthLimit;

  public List<Action> bestPlan;
  Metrics bestPlanMetrics;
  private Metrics lastPlanMetrics = new Metrics();
  
  
  int proposedSolutionsCount = 0;
  int solutionSearchesCount  = 0;
  
  SlidersBoard initialState;
  ActionsFunction actionsFunction;
  Problem problem;
  GoalTest goalTest;
  
  HeuristicFunction heuristic;
  //long defaultTime=1*sec;
  long defaultTime=15*sec;
  
  public MySearch(HeuristicFunction h){
    heuristic = h;
  }
  
  
  
  /**
   * Loops timedSearches and returns the best solution found on the default given time
   * @param p Problem Instance
   */
  @Override
  public List<Action> search(Problem p) throws Exception {
    return timedSearch(p,  defaultTime);
  }
  
  
  /**
   * Loops timedSearches and returns the best solution found on the given time
   * @param p Problem Instance
   * @param timeGiven in milliseconds
   */
  @Override
  public List<Action> timedSearch(Problem p, long timeGiven) throws Exception {
    problem = p;
    initialState    = (SlidersBoard) problem.getInitialState();
    actionsFunction = problem.getActionsFunction();
    goalTest        = problem.getGoalTest();
    _size = SlidersBoard.getSIZE();
    depthLimit = _size*_size-1;//See declaration
    
    long used = 0;
    
    while(used<timeGiven){
      
      long startTime = System.currentTimeMillis();
      List<Action> newPlan = timedSearch(p, timeGiven, timeGiven-used);
      long endTime = System.currentTimeMillis();
      long duration = endTime - startTime;
      used+= duration;
      
      
      if(used>timeGiven){//time out!
        Utils.err.println("\nTimed out");
        break;
      }
      
      solutionSearchesCount++;
      
      if(newPlan==null){
        //Search failed, no solution found :C
        //Utils.err.print(".");
        continue;
      }
      

      
      //Execute Solution
      Object currentState = initialState;
      for(Action a : newPlan)
        currentState = p.getResultFunction().result(currentState, a);
      
      //Goal Test
      if(!p.getGoalTest().isGoalState(currentState)){
        //"Solution" found does not solve the instance
        //Utils.err.print("\n???");
        continue;
      }
      proposedSolutionsCount++;
      
      
      //Remember if its better
      if (bestPlan==null || newPlan.size()<bestPlan.size()){
        bestPlan=newPlan;
        bestPlanMetrics = lastPlanMetrics;
        Utils.err.format("\nA better solution was found (%d)\n", bestPlan.size());
        depthLimit = bestPlan.size();
        //printSol(newPlan);
      } else{
        //Solution found was not better
        //Utils.err.print("!");
      }
    }
    
    Utils.err.format("%d results considered (%d solutions)\n", solutionSearchesCount, proposedSolutionsCount);
    
    if (proposedSolutionsCount>0)
      return bestPlan;
    throw new NoSolutionFoundException();
  }
  
  
  




  /**
   * Tries to get a plan hopefully on time 
   * @param p Problem Instance
   * @param timeGiven in milliseconds
   * @param timeLeft
   * @return plan
   */
  public List<Action> timedSearch(Problem p, long timeGiven, long timeLeft){
    
    //TODO: implement a better timed search policy.
    
    //Known information that could be useful:      
    // if(solutionSearchesCount==0)
    //   Utils.err.println("Solving new instance");
    // if(proposedSolutionsCount==0)
    //   Utils.err.println("No solution has been found");
    
    if(!GreedyRan){
      List<Action> greedyPlan = runGreedy(p, depthLimit);
      System.gc();
      return greedyPlan;
    }
    if(!AStarRan && timeLeft > _size*_size*sec){//time condition may not work at all. !!!
      AStarEvaluationFunction ev = new MyAStarEvaluationFunction(heuristic);
      List<Action> aStarPlan = runAstar(p, ev);
      System.gc();
      return aStarPlan;
    }
    return runRandom(p, depthLimit);
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
/*
 * Astar
 */
  
  //A* search is deterministic, no need to run twice using the same f() and h().
  boolean AStarRan=false;
  
  
  /**
   * Returns the result of an A* search based on MyHeuristic, this will probably take too much time
   * @param p Problem Instance
   * @return
   */
  public List<Action> runAstar(Problem p, AStarEvaluationFunction ef){
    Utils.err.format("%15s\n", "AStar Search");
    GraphSearch g = new GraphSearch();
    Search aStar = new AStarSearch(g, ef);
    AStarRan=true;
    
    try {
      SearchAgent a = new SearchAgent(problem, aStar);
      lastPlanMetrics = aStar.getMetrics();
      return a.getActions();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  
  
  
  
  
  
  
/*
 * Greedy
 */
  
  //Greedy search is deterministic, no need to run twice using the same h().
  boolean GreedyRan=false;
  
  
  /**
   * Performs a greedy search, the depth limit should be adequate!
   * @param p Problem Instance
   * @return
   */
  public List<Action> runGreedy(Problem p, int depthLimit){
    Utils.err.format("%15s\n", "Greedy Search");
    LinkedList<Action> actions = new LinkedList<Action>();
    SlidersBoard currentState = getInitialState();
    int count = 0;
    GreedyRan=true;
    
    
    while(!isGoal(currentState) && count++ < depthLimit){
      Action a = getGreedyAction(currentState);
      actions.add(a);
      currentState = (SlidersBoard) p.getResultFunction().result(currentState,  a);
      //currentState.doMove((SlidersAction) a);
    }
    
    if(isGoal(currentState))
      return actions;
    return null;
  }
  
  Action getGreedyAction(SlidersBoard currentState){
    Action selectedAction = null;
    double bestH = Double.POSITIVE_INFINITY;

    
    //UP, DOWN, LEFT, RIGHT
    for(SlidersActionType st : SlidersActionType.values())
      //Column row Index (Down0, Up3, ...)
      for(int i = 0; i < _size; i++) {
        
        SlidersBoard newState = cloneBoard(currentState);
        SlidersAction sa = move(newState, st, i);
        
        double newH = heuristic.h(newState);
        
        if(newH > bestH)
          continue;
        
        bestH = newH;
        selectedAction = sa;
      }

    return selectedAction;
  }  
  
  
  
  
  




/*
 * Random
 */
  
  
  /**
   * Moves randomly until a goal is found, it's 'known' to work on 'reversible' problems.
   * @param p
   * @return
   */
  public List<Action> runRandom(Problem p, int depthLimit){
    //Utils.err.format("%15s ", "Random Search");
    LinkedList<Action> actions = new LinkedList<Action>();
    Object currentState = getInitialState();
    Set<Action> possibleActions;
    
    int count = 0;
    
    while(!isGoal(currentState)  && count++ < depthLimit){
      possibleActions = getPossibleActions(currentState);
      Action a = getRandomAction(possibleActions);
      actions.add(a);
      currentState = (SlidersBoard) p.getResultFunction().result(currentState,  a);
    }
    
    if(isGoal(currentState))
      return actions;
    return null;
  }
  
  Action getRandomAction(Set<Action> actions){
    int size = actions.size();
    int selected = (int) Math.rint(size*Math.random());
    
    for(Action a : actions){
      if(selected-->0) continue;
      return a;
    }
    
    return actions.iterator().next();
  }

  
  
  
  
  
  
/*
 * Helpers, aliases and stuff
 */
  
  
  @SuppressWarnings("unused")
  private void printSol(List<Action> newPlan) {
    SlidersBoard cS = getInitialState();
    System.out.println(cS);
    for(Action a : newPlan){
      System.out.println(a);
      cS = (SlidersBoard) problem.getResultFunction().result(cS, a);
      System.out.println(cS);
    }
  }
  
  boolean isGoal(Object state){
    return goalTest.isGoalState(state);
  }
  
  Set<Action> getPossibleActions(Object currentState){
    return actionsFunction.actions(currentState);
  }
  private SlidersBoard getInitialState() {
    return cloneBoard(initialState);
  }
  SlidersBoard cloneBoard(SlidersBoard b){
    return new SlidersBoard(b.getState());
  }
  
  static SlidersAction move(SlidersBoard b, SlidersActionType a, int i) {
    SlidersAction sa = new SlidersAction(a, i);
    b.doMove(sa);
    return sa;
  }

  @Override
  public Metrics getMetrics() {
    return bestPlanMetrics;
  }
}
