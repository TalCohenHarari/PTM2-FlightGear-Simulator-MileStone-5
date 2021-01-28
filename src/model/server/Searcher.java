package model.server;

public interface Searcher<Solution> {
	public Solution search(Searchable searchable);
	public int getNumberOfNodesEvaluated();
}