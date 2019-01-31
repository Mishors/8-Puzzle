package algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Eight_queens_solver {
	
	private static List<String> BFSstates = new ArrayList<>();
	private static List<String> DFSstates = new ArrayList<>();
	
	public List<String> getBFSstates(){
		return BFSstates;
	};
	
	public List<String> getDFSstates(){
		return DFSstates;
	};
	
	public static int bfsCost() {
		return BFSstates.size();
	}
	
	public static int dfsCost() {
		return DFSstates.size();
	}

	public static void main(String[] args) {
		String goal = "012345678";
		String state = "724506831";
		Eight_queens_solver solver = new Eight_queens_solver();
		// dfs
		long startTime = System.currentTimeMillis();
		System.out.println(solver.DFS(state, goal));
		System.out.println("Cost of dfs = " + dfsCost());
	    long stopTime = System.currentTimeMillis();
	    System.out.println("Time of dfs in milli seconds : " + (stopTime - startTime) + " ms.");
		// bfs
	    startTime = System.currentTimeMillis();
		System.out.println(solver.BFS(state, goal));
		System.out.println("Cost of bfs = " + bfsCost());
	    stopTime = System.currentTimeMillis();
	    System.out.println("Time of bfs in milli seconds : " + (stopTime - startTime) + " ms.");		
	}
	
	/**
	 * Swaps two characters in a string
	 * @param str
	 * @param i
	 * @param j
	 * @return swapped string
	 */
	private String swap(String str, int i, int j) 
    { 
        StringBuilder sb = new StringBuilder(str); 
        sb.setCharAt(i, str.charAt(j)); 
        sb.setCharAt(j, str.charAt(i)); 
        return sb.toString(); 
    }		
	
	private boolean isGoalState(String currentState, String goalState){
		return currentState.equals(goalState);
	}
	
	private String getChildState(int goalPos, int targetPos, String currentState){
		return swap(currentState, goalPos, targetPos);
	}
	
	public boolean DFS (String initialState, String goalState){
		Stack<String> frontier = new Stack<>();
		frontier.push(initialState);
		HashSet<String> explored = new HashSet<>();
		
		while(!frontier.isEmpty()){
			String currentState = frontier.pop();	
			DFSstates.add(currentState);
			System.out.println(currentState);
			explored.add(currentState);
			
			if(isGoalState(currentState, goalState)) return true;
			
			int goalPos = 0;
			
			for(int i=0; i<9; i++){
				if(currentState.charAt(i) == '0')
					{
						goalPos = i;
					    break;
					}
			}
						 
			//Looping on neighbors
			int childPos[][] = {{3, 1,-1,-1},
								{4, 2, 0,-1},
								{5, 4, 1,-1},
								{6, 4, 0,-1}, 
								{7, 5, 3, 1}, 
								{8, 4, 2,-1}, 
								{7, 3,-1,-1}, 
								{8, 6, 4,-1},
								{7, 5,-1,-1}};
			
			for(int i=0; i<4; i++){
				int targetPos = childPos[goalPos][i];				
				//If not valid
				if(targetPos == -1) continue;					
				
				String neighbor = getChildState(goalPos,targetPos,currentState);
		        if(!frontier.contains(neighbor) && !explored.contains(neighbor)){
		        	frontier.push(neighbor);
		        }		
			}
		}
		
		return false;
	}
	
	public boolean BFS (String initialState, String goalState){
		LinkedList<String> frontier = new LinkedList<>();
		frontier.add(initialState);
		HashSet<String> explored = new HashSet<>();
		
		while(!frontier.isEmpty()){
			String currentState = frontier.remove();
			BFSstates.add(currentState);
			System.out.println(currentState);
			explored.add(currentState);
			
			if(isGoalState(currentState, goalState)) return true;
			
			int goalPos = 0;
			
			for(int i=0; i<9; i++){
				if(currentState.charAt(i) == '0')
					{
						goalPos = i;
					    break;
					}
			}
						 
			//Looping on neighbors
			int childPos[][] = {{1, 3,-1,-1},
								{0, 2, 4,-1},
								{1, 4, 5,-1},
								{0, 4, 6,-1}, 
								{1, 3, 5, 7}, 
								{2, 4, 8,-1}, 
								{3, 7,-1,-1}, 
								{4, 6, 8,-1},
								{5, 7,-1,-1}};
			
			for(int i=0; i<4; i++){
				int targetPos = childPos[goalPos][i];				
				//If not valid
				if(targetPos == -1) continue;					
				
				String neighbor = getChildState(goalPos,targetPos,currentState);
		        if(!frontier.contains(neighbor) && !explored.contains(neighbor)){
		        	frontier.add(neighbor);
		        }		
			}
		}
		return false;
	}
}
