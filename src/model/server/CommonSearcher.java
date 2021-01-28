package model.server;

import java.util.Comparator;
import java.util.PriorityQueue;

public abstract class CommonSearcher<Solution> implements Searcher<Solution> {
	// Data members.
	protected PriorityQueue<State> openList;
	protected int evluateNodes;
	
	// CTOR.
	public CommonSearcher(){
		Comparator<State> comparator = new StateComparator();
		openList = new PriorityQueue<State>(comparator);
		evluateNodes = 0;
	}
	
	protected State popOpenList() {
		evluateNodes++;
		return openList.poll();
	}
	
	protected Solution backTrace(State goalState, State initialState) {
		if(goalState.equals(initialState)) { return (Solution)initialState.getState(); }
		return (Solution)(backTrace(goalState.getCameFrom(), initialState) + "->" + goalState.getState());
	}
}