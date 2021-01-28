package model.server;

import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class StateComparator implements Comparator<State> {

	@Override
	public int compare(State state1, State state2) {
		if (state1.getCost() < state2.getCost()) { return -1; }
		else if(state1.getCost() == state2.getCost()) { return 0; }
		
		return 1;
	}
}