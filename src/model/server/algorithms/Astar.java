package model.server.algorithms;

import java.util.ArrayList;
import java.util.HashSet;

import model.server.CommonSearcher;
import model.server.Searchable;
import model.server.State;

public class Astar<Solution,heuristic> extends CommonSearcher<Solution> {
	// Inner interface - Heuristic.
	public interface Heuristic{
		public double cost(State s,State goalState);
	}
	
	// Data member.
	Heuristic heuristic;
	
	// CTOR.
	public Astar(Heuristic heuristic) { this.heuristic = heuristic; }
	
	@Override
	public Solution search(Searchable searchable) {
		openList.add(searchable.getInitialState());
		HashSet<State> closedSet = new HashSet<State>();
		
		while(openList.size() > 0)
		{
			State state = popOpenList();
			
			closedSet.add(state);
			
			ArrayList<State> successors=searchable.getAllPossibleStates(state);
			state.setCost(state.getCost()+heuristic.cost(state,searchable.getGoalState()));
			
			if(state.equals(searchable.getGoalState())) { return backTrace(state, searchable.getInitialState()); } 
			
			for(State successor : successors) {
				successor.setCost(successor.getCost() + heuristic.cost(successor,searchable.getGoalState()));
				if(!closedSet.contains(successor) && (!openList.contains(successor))) {
					successor.setCameFrom(state);
					openList.add(successor);
				}
				else if((state.getCost() + (successor.getCost() - successor.getCameFrom().getCost())) < successor.getCost()) {
					 	if(openList.contains(successor)) {
					 		successor.setCameFrom(state);
					 	}
						else {
							successor.setCameFrom(state);
							closedSet.remove(successor);
							openList.add(successor);
					}
				}
			}
		}
		
		return backTrace(searchable.getGoalState(), searchable.getInitialState());
	}

	@Override
	public int getNumberOfNodesEvaluated() { return evluateNodes; }
}