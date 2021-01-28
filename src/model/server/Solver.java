package model.server;

public interface Solver<Problem, Solution> {
	public Solution Solve(Problem problem);
}