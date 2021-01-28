package model.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import model.server.algorithms.Astar;


public class MyClientHandler implements ClientHandler {
	// Data members.
	CacheManager cacheManager ;
	Solver solver;
	
	// CTOR.
	public MyClientHandler(CacheManager cacheManager ) { this.cacheManager = cacheManager; }
	
	@Override
	public void handleClient(InputStream in, OutputStream out) {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(out));
		try {
			String Line;
			String Solved;
			ArrayList<String[]> lines = new ArrayList<>();
			
			while(!(Line = bufferedReader.readLine()).equals("end"))
			{
				lines.add(Line.split(","));
			}
			
			int j = 0;
			int[][]mat = new int[lines.size()][];
			
			for (int i = 0; i < (mat.length - 1); i++) {
				String[] tmp = lines.get(i);
				mat[i] = new int[tmp.length];
				for (String string : tmp) {
						mat[i][j] = Integer.parseInt(string);
						j++;
				}
				
				j = 0;
			}
				
			Matrix matrix = new Matrix(mat);
			
			Astar.Heuristic heuristic = new Astar.Heuristic() { 
				@Override
				public double cost(State state, State goalState) {
					String start = (String)(state.getState());
					String[] tmp = start.split(",");
					double x1 = Integer.parseInt(tmp[0]);
					double y1 = Integer.parseInt(tmp[1]);
					String end = (String)goalState.getState();
					tmp = end.split(",");
					double x2 = Integer.parseInt(tmp[0]);
					double y2 = Integer.parseInt(tmp[1]);
					
					return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
				}
			};
			
			Searcher searcher = new Astar(heuristic);
			
			solver = new SolverSearcher<>(searcher);
			
			matrix.setIntialState(Line = bufferedReader.readLine());
			matrix.setGoalState(Line = bufferedReader.readLine());
			if(cacheManager.Check(matrix.toString()))
			{
				Solved = (String) cacheManager .Extract(matrix.toString());
			}
			else {
				Solved = (String)solver.Solve(matrix);
				String[] arrows = Solved.split("->");
				Solved = "";
				
				String[] arrow1;
				String[] arrow2;
				
				int x;
				int y;
				
				for (int i = 0; i < (arrows.length - 1); i++) {
					arrow1 = arrows[i].split(",");
					arrow2 = arrows[i + 1].split(",");
					
					x = Integer.parseInt(arrow2[0]) - Integer.parseInt(arrow1[0]);
					y = Integer.parseInt(arrow2[1]) - Integer.parseInt(arrow1[1]);
					if(x > 0) {
						Solved += "Down" + ",";
					} else if(x < 0) { 
						Solved += "Up" + ",";
					} else {
						if(y > 0) {
							Solved += "Right" + ",";
						} else {
							Solved += "Left" + ",";
						}
					}
				}
				
				cacheManager .Save(matrix.toString(), Solved);
			}
			
			printWriter.println(Solved.substring(0, Solved.length() - 1));
			printWriter.flush();
			
			// bufferedReader.close();
			// printWriter.close();
			
		} catch (IOException e) {e.printStackTrace();}
	}
}