package gui;

import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import gui.Node;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class H_A_star {
	private static int[][] goal = {{0 , 1 , 2} , {3 , 4 , 5} , {6 , 7 , 8}};
	private static int[] dx = {-1 , 0 , 0 , 1};
	private static int[] dy = {0 , -1 , 1 , 0};
	private static Set<String> explored = new HashSet<String>();
	private static Set<String> inqueue = new HashSet<String>();
	private static List<String> path = new ArrayList<>();
	private static int visited;
	
	public List<String> getStates() {
		return path;
	}

	
	public static void print(int g[][])
	{
		for(int i = 0 ; i < 3 ; i++)
		{
			for(int j = 0 ; j < 3 ; j++)
				System.out.print(g[i][j] + " ");
			System.out.println();
		}
		System.out.println();
	}
	
	public static double heuristic1(int g[][])
	{
		double sum = 0;
		for(int i = 0 ; i < 3 ; i++)
		{
			for(int j = 0 ; j < 3 ; j++)
			{
				for(int k = 0 ; k < 3 ; k++)
				{
					for(int l = 0 ; l < 3 ; l++)
					{
						if(g[i][j] == goal[k][l] && g[i][j] != 0)
							sum += Math.abs(i - k) + Math.abs(j - l);
					}
				}
			}
		}
		return sum;
	}
	
	public static double heuristic2(int g[][])
	{
		double sum = 0;
		for(int i = 0 ; i < 3 ; i++)
		{
			for(int j = 0 ; j < 3 ; j++)
			{
				for(int k = 0 ; k < 3 ; k++)
				{
					for(int l = 0 ; l < 3 ; l++)
					{
						if(g[i][j] == goal[k][l])
							sum += Math.sqrt((i - k) * (i - k) + (j - l) * (j - l));
					}
				}
			}
		}
		return sum;
	}
	
	public static boolean reached(int g[][])
	{
		for(int i = 0 ; i < 3 ; i++)
		{
			for(int j = 0 ; j < 3 ; j++)
			{
				if(g[i][j] != goal[i][j])
					return false;
			}
		}
		return true;
	}
	
	public static boolean search_in_queue(PriorityQueue<Node> q , Node m)
	{
		Iterator<Node> it = q.iterator();
	    while (it.hasNext()) {
	         Node n = (Node) it.next();
	         if(n.getCur().getX() == m.getCur().getX() && n.getCur().getY() == m.getCur().getY())
	         {
	        	 if(m.getLevel() + m.getH() <= n.getLevel() + n.getH())
	        	 {
	        		 n.setH(m.getH());
	        		 n.setLevel(m.getLevel());
	        		 n.setGrid(m.getGrid());
	        		 return true;
	        	 }
	         }
	    }
	    return false;
	}
	
	public static int []to_1d(int g[][])
	{
		int[] oneDArray = new int[9];
		for(int i = 0; i < 3 ; i ++)
			for(int s = 0; s < 3 ; s ++)
			    oneDArray[(i * 3) + s] = g[i][s];
		return oneDArray;
	}
	
	public static String tostring(int g[][])
	{
		String x = new String();
		for(int i = 0; i < 3 ; i ++)
			for(int s = 0; s < 3 ; s ++)
			    x += g[i][s];
		return x;
	}
	
	public static boolean search1(int g[][] , Point cur)
	{
		PriorityQueue<Node> queue = new PriorityQueue<>();
		queue.add(new Node(cur , null , g , heuristic1(g)));
		inqueue.add(Arrays.toString(to_1d(g)));
		while(!queue.isEmpty())
		{
			Node x = queue.remove();
			explored.add(Arrays.toString(to_1d(x.getGrid())));
			inqueue.remove(Arrays.toString(to_1d(x.getGrid())));
			cur.x = x.getCur().x;
			cur.y = x.getCur().y;
			for(int i = 0 ; i < 3 ; i++)
				for(int j = 0 ; j < 3 ; j++)
					g[i][j] = x.getGrid()[i][j];
			if(reached(g))
			{
				visited = explored.size();
				System.out.println("Moves : " + x.getLevel());
				Stack<Node> s = new Stack<>();
				while(x.parent != null)
				{
					s.push(x);
					x = x.parent;
				}
				while(!s.isEmpty())
				{
					path.add(tostring(s.pop().getGrid()));
					//print(s.pop().getGrid());
				}
				return true;
			}
			for(int i = 0 ; i < 4 ; i++)
			{
				int [][] temp = new int[3][3];
				for(int k = 0 ; k < 3 ; k++)
					for(int l = 0 ; l < 3 ; l++)
						temp[k][l] = g[k][l];
				Point next = new Point(cur.x + dx[i] , cur.y + dy[i]);
				if(next.x >= 0 && next.x < 3 && next.y >= 0 && next.y < 3)
				{
					int d = temp[next.x][next.y];
					temp[next.x][next.y] = temp[cur.x][cur.y];
					temp[cur.x][cur.y] = d;
					Node m = new Node(next , x , temp , heuristic1(temp));
					if(!explored.contains(Arrays.toString(to_1d(temp))) && !inqueue.contains(Arrays.toString(to_1d(temp))))
				    {
						queue.add(m);
						inqueue.add(Arrays.toString(to_1d(temp)));
					}
					else if(inqueue.contains(Arrays.toString(to_1d(temp))))
					search_in_queue(queue, m);
				}
			}	
		}
		return true;
	}
	
	public static boolean search2(int g[][] , Point cur)
	{
		PriorityQueue<Node> queue = new PriorityQueue<>();
		queue.add(new Node(cur , null , g , heuristic2(g)));
		inqueue.add(Arrays.toString(to_1d(g)));
		while(!queue.isEmpty())
		{
			Node x = queue.remove();
			explored.add(Arrays.toString(to_1d(x.getGrid())));
			inqueue.remove(Arrays.toString(to_1d(x.getGrid())));
			cur.x = x.getCur().x;
			cur.y = x.getCur().y;
			for(int i = 0 ; i < 3 ; i++)
				for(int j = 0 ; j < 3 ; j++)
					g[i][j] = x.getGrid()[i][j];
			if(reached(g))
			{
				visited = explored.size();
				System.out.println("Moves : " + x.getLevel());
				Stack<Node> s = new Stack<>();
				while(x.parent != null)
				{
					s.push(x);
					x = x.parent;
				}
				while(!s.isEmpty())
				{
					path.add(tostring(s.pop().getGrid()));
					//print(s.pop().getGrid());
				}
				return true;
			}
			for(int i = 0 ; i < 4 ; i++)
			{
				int [][] temp = new int[3][3];
				for(int k = 0 ; k < 3 ; k++)
					for(int l = 0 ; l < 3 ; l++)
						temp[k][l] = g[k][l];
				Point next = new Point(cur.x + dx[i] , cur.y + dy[i]);
				if(next.x >= 0 && next.x < 3 && next.y >= 0 && next.y < 3)
				{
					int d = temp[next.x][next.y];
					temp[next.x][next.y] = temp[cur.x][cur.y];
					temp[cur.x][cur.y] = d;
					Node m = new Node(next , x , temp , heuristic2(temp));
					if(!explored.contains(Arrays.toString(to_1d(temp))) && !inqueue.contains(Arrays.toString(to_1d(temp))))
				    {
						queue.add(m);
						inqueue.add(Arrays.toString(to_1d(temp)));
					}
					else if(inqueue.contains(Arrays.toString(to_1d(temp))))
					search_in_queue(queue, m);
				}
			}	
		}
		return true;
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		//String s = input.next();
		Point cur = new Point();
		int [][]grid = new int[3][3];
		for(int i = 0 ; i < 3 ; i++)
		{
			for(int j = 0 ; j < 3 ; j++)
			{
				//grid[i][j] = s.charAt(i * 3 + j);
				grid[i][j] = input.nextInt();
				if(grid[i][j] == 0)
				{
					cur.x = i;
					cur.y = j;
				}
			}
		}
		long startTime = System.currentTimeMillis();
		search2(grid , cur);
	    long stopTime = System.currentTimeMillis();
	    System.out.println("Cost of A2 = " + path.size());
	    System.out.println("Time in milli seconds : " + (stopTime - startTime) + " ms.");
	    System.out.println("Expanded nodes : " + visited);
	    //312045678    125340678
	}
}