package model.server;



public class State<T> {
	// Data members.
	protected T state;
	private double cost;
	private State cameFrom;
	
	// Default CTOR.
	public State() {
		this.state = null;
		this.cost = 0;
		this.cameFrom = null;
	}
		
	// CTOR.
	public State(T state) { this.state = state; }
	
	// Returns true if the received state is the current.
	public boolean equals(State state) { return state.equals(state.getState()); }

	// Returns the current state.
	public T getState() { return state; }

	// Sets the current state.
	public void setState(T state) { this.state = state; }

	@Override
	public String toString() { return "State [state=" + state + ", cost=" + cost + ", cameFrom=" + cameFrom + "]"; }
	
	// Set & get of cost.
	public void setCost(double cost) { this.cost = cost; }

	public double getCost() {
		if(this.getCameFrom() != null) { return this.getCameFrom().getCost()+cost; }
		
		return cost;
	}

	// Set & get of cameFrom.
	public void setCameFrom(State cameFrom) { this.cameFrom = cameFrom; }

	public State getCameFrom() { return cameFrom; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}
}