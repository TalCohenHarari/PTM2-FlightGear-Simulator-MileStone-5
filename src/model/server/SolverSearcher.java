package model.server;

public class SolverSearcher<Problem,Solution> implements Solver<Problem, Solution> {
	// Data member.
	private Searcher searcher;
	
	// CTOR.
	public SolverSearcher(Searcher searcher) { this.searcher = searcher; }
	
	@Override
	public Solution Solve(Problem problem) { return (Solution) searcher.search((Searchable)problem); }
}