package model.server.algorithms;

import java.util.ArrayList;
import java.util.HashSet;

import model.server.CommonSearcher;
import model.server.Searchable;
import model.server.State;

public class BFS<Solution> extends CommonSearcher<Solution> {

	@Override
	public Solution search(Searchable searchable) 
	{
		openList.add(searchable.getInitialState());
		HashSet<State> closedSet = new HashSet<State>();
		
		while(openList.size() > 0)
		{
			State state = popOpenList();
			closedSet.add(state);
			ArrayList<State> successors = searchable.getAllPossibleStates(state);

			if(state.equals(searchable.getGoalState())) { return backTrace(state, searchable.getInitialState()); }
			
			for(State successor : successors){
				if(!closedSet.contains(successor) && (!openList.contains(successor))) {
					successor.setCameFrom(state);
					openList.add(successor);
				}
				else if((state.getCost() + (successor.getCost() - successor.getCameFrom().getCost())) < successor.getCost()) {
					 	if(openList.contains(successor)) {
					 		successor.setCameFrom(state);
					 	}
						else  {
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
