package model.server;

import java.util.ArrayList;

public interface Searchable {
	State getInitialState();
	State getGoalState();
	ArrayList<State> getAllPossibleStates(State state);
}