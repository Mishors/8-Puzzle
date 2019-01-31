package gui;

import java.awt.Point;

public class Node implements Comparable<Node> {
	private Point cur;
	public Node parent;
	private int [][] g = new int[3][3];
	private double level , h;
	
	public Node(Point cur , Node parent , int g[][] , double h) {
        this.cur = cur;
        this.parent = parent;
        this.g = g;
        if(this.parent == null)
        	this.level = 0;
        else
            this.level = parent.getLevel() + 1;
        this.h = h;
    }
	
	public Point getCur() {
        return cur;
    }

    public void setCur(Point cur) {
        this.cur = cur;
    }

    public int[][] getGrid() {
        return g;
    }

    public void setGrid(int g[][]) {
    	this.g = g;
    }
    
    public double getLevel() {
        return level;
    }
    
    public void setLevel(double l) {
		this.level = l;
	}

    public void setH(double h) {
        this.h = h;
    }
    
    public double getH()
    {
    	return h;
    }
    
    public Node getParent()
    {
    	return parent;
    }
    
    public void setParent(Node parent)
    {
    	this.parent = parent;
    }
	
    public int compareTo(Node node) {
        if(this.h + this.level > node.getLevel() + node.getH()) {
            return 1;
        } else if (this.h + this.level < node.getLevel() + node.getH()) {
            return -1;
        } else {
            return 0;
        }
    }
}
