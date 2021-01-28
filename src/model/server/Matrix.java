package model.server;

import java.util.ArrayList;
import java.util.Arrays;

public class Matrix implements Searchable {
	// Data members.
	private State initialState;
	private State goalState;
	private int[][] matrix;
	private MatrixState[][] stateMatrix;
	
	// Default CTOR.
	public Matrix() {
		matrix = null;
		initialState = null;
		goalState = null;
	}
	
	// CTOR.
	public Matrix(int[][] matrix) {
		this.matrix = matrix;
		stateMatrix = new MatrixState[matrix.length][matrix[0].length];
		for (int i = 0; i < (matrix.length - 1); i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				stateMatrix[i][j]=new MatrixState(i+","+j);
				stateMatrix[i][j].setCost(matrix[i][j]);
			}
		}
	}
	
	// Set & Get initialState.
	public void setIntialState(String string) {
		String[] loc = string.split(",");
		int row;
		int col;
		
		row = Integer.parseInt(loc[0]);
		col = Integer.parseInt(loc[1]);
		
		this.initialState = stateMatrix[row][col];
	}
	
	@Override
	public State getInitialState() { return this.initialState; }
	
	// Set & Get goalState.
	public void setGoalState(String string) {
		String[] loc = string.split(",");
		int row;
		int col;
		
		row = Integer.parseInt(loc[0]);
		col = Integer.parseInt(loc[1]);
		
		this.goalState = stateMatrix[row][col];
	}

	@Override
	public State getGoalState() { return this.goalState; }

	// Set & Get matrix.
	public int[][] getMatrix() { return matrix; }
	
	public void setMatrix(int[][] matrix) { this.matrix = matrix; }
	
	
	@Override
	public ArrayList<State> getAllPossibleStates(State state) {
		String[] loc = ((String) state.getState()).split(",");
		int row;
		int col;
		int tmp = 0;
		
		row = Integer.parseInt(loc[0]);
		col = Integer.parseInt(loc[1]);
		
		State right = null;
		State left = null; 
		State up = null;
		State down = null;
		
		ArrayList<State> answer = new ArrayList<State>();
		
		if(row == 0) { tmp += 3; }
		if(row == (matrix.length - 1)) { tmp += 7; }
		if(col == 0) { tmp += 5; }
		if(col == (matrix[row].length - 1)) { tmp += 11; }
		
		switch (tmp) {
		case 8:
			right = stateMatrix[row][col + 1];
			down = stateMatrix[row + 1][col];
			break;
		case 3:
			right = stateMatrix[row][col + 1];
			down = stateMatrix[row + 1][col];
			left = stateMatrix[row][col - 1];
			break;
		case 5:
			right = stateMatrix[row][col + 1];
			down = stateMatrix[row + 1][col];
			up = stateMatrix[row - 1][col];
			break;
		case 7:
			right = stateMatrix[row][col + 1];
			up = stateMatrix[row - 1][col];
			left = stateMatrix[row][col - 1];
			break;
		case 11:
			up = stateMatrix[row - 1][col];
			left = stateMatrix[row][col - 1];
			down = stateMatrix[row + 1][col];
			break;
		case 14:
			left = stateMatrix[row][col - 1];
			down = stateMatrix[row + 1][col];
			break;
		case 12:
			up = stateMatrix[row - 1][col];
			right = stateMatrix[row][col + 1];
			break;
		case 18:
			up = stateMatrix[row - 1][col];
			left = stateMatrix[row][col - 1];
			break;
		default:
			up = stateMatrix[row - 1][col];
			left = stateMatrix[row][col - 1];
			right = stateMatrix[row][col + 1];
			down = stateMatrix[row + 1][col];
			break;
		}
		
		State[] surrounds = {right ,left ,up ,down};
		for (State surround : surrounds) {
			if(surround != null) {
				if(surround != state.getCameFrom()) { answer.add(surround); }
			}
		}
		
		return answer;
	}
	
	@Override
	public String toString() {
		return "Matrix [initialState=" + initialState + ", goalState=" + goalState + ", matrix="
				+ Arrays.toString(matrix) + "]";
	}
}