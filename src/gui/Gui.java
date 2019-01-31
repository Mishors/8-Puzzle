package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import algorithms.Eight_queens_solver;

@SuppressWarnings("serial")
public class Gui extends JPanel {

	private int size; // size of the game
	private int nTiles; // number of tiles
	private int dim; // grid ui dimension
	private static final Color FOREGROUND_COLOR = new Color(7, 105, 226); // foreground
																			// color
//	private static final Random ran = new Random();
	private int[] tiles; // array of sorted tiles
	private int tileSize; // ui size tile
	private int blankPos; // blank square position
	private int margin; // margin of the grid
	private int gridSize; // size of the grid
	private boolean gameOver; // indicates ending of the game
	private Eight_queens_solver bfs_dfs;
	private H_A_star a;
	private List<String> tilesList;
	private String goal;
	private String state;
	private int count;
	private int index;

	@SuppressWarnings("resource")
	public Gui(int size, int dim, int margin) {
		this.size = size;
		this.dim = dim;
		this.margin = margin;

		bfs_dfs = new Eight_queens_solver();
		a = new H_A_star();
		tilesList = new ArrayList<>();
		
		// input
		goal = "012345678";
		state = "";
		
		
		count = 0;
		index = 0;
		Scanner input = new Scanner(System.in);
		Point cur = new Point();
		int [][]grid = new int[3][3];
		for(int i = 0 ; i < 3 ; i++)
		{
			for(int j = 0 ; j < 3 ; j++)
			{
				grid[i][j] = input.nextInt();
				state += grid[i][j];
				if(grid[i][j] == 0)
				{
					cur.x = i;
					cur.y = j;
				}
			}
		}
		

		// initiate tiles
		nTiles = size * size - 1; // -1 because of blank tile
		tiles = new int[size * size];

		// calculate grid size and tile size
		gridSize = this.dim - 2 * margin;
		tileSize = 150;

		setPreferredSize(new Dimension(dim, dim + margin));
		setBackground(Color.WHITE);
		setForeground(FOREGROUND_COLOR);
		setFont(new Font("SansSerif", Font.BOLD, 30));

		gameOver = true;

		JButton bfs = new JButton("BFS");
		bfs.setBackground(Color.PINK);

		JButton dfs = new JButton("DFS");
		dfs.setBackground(Color.CYAN);

		JButton a1 = new JButton("A*1");
		a1.setBackground(Color.GREEN);

		JButton a2 = new JButton("A*2");
		a2.setBackground(Color.YELLOW);

		JButton shuffle = new JButton("Shuffle");
		shuffle.setBackground(Color.RED);

		JButton next = new JButton("-->");
		next.setBackground(Color.RED);
		
		bfs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				count = 0;
				reset();
				repaint();
				System.out.println(bfs_dfs.BFS(state, goal));
				tilesList = bfs_dfs.getBFSstates();
				index = tilesList.size();
				System.out.println("bfs");
			}
		});

		dfs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				count = 0;
				reset();
				repaint();
				System.out.println(bfs_dfs.DFS(state, goal));
				tilesList = bfs_dfs.getDFSstates();
				index = tilesList.size();
				System.out.println("dfs");
			}
		});

		a1.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				count = 0;
				reset();
				repaint();
				System.out.println(a.search1(grid, cur));
				tilesList = a.getStates();
				index = tilesList.size() ;
				System.out.println("a1");
			}
		});

		a2.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				count = 0;
				reset();
				repaint();
				System.out.println(a.search2(grid, cur));;
				tilesList = a.getStates();
				index = tilesList.size();
				System.out.println("a2");
			}
		});

//		shuffle.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				shuffle();
//			}
//		});

		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(count < index) {
					solve(tilesList, count);
					count ++;
				}
			}
		});

		// used to let user to interact on the grid by clicking
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (gameOver) {
					newGame();
				} else {
					// get position of the click
					int ex = e.getX() - margin;
					int ey = e.getY() - margin;

					// click in the grid ?
					if (ex < 0 || ex > gridSize || ey < 0 || ey > gridSize) {
						return;
					}
					// get position in the grid
					int c1 = ex / tileSize;
					int r1 = ey / tileSize;

					// get position of the blank cell
					int c2 = blankPos % size;
					int r2 = blankPos / size;

					// convert in 1D coords
					int clickPos = r1 * size + c1;
					int dir = 0;

					// search direction for multiple move at once
					if (c1 == c2 && Math.abs(r1 - r2) > 0) {
						dir = (r1 - r2) > 0 ? size : -size;
					} else if (r1 == r2 && Math.abs(c1 - c2) > 0) {
						dir = (c1 - c2) > 0 ? 1 : -1;
					}
					if (dir != 0) {
						// move tiles in the direction
						do {
							int newBlankPos = blankPos + dir;
							tiles[blankPos] = tiles[newBlankPos];
							blankPos = newBlankPos;
						} while (blankPos != clickPos);

						tiles[blankPos] = 0;
					}

					// check if game is solved
					gameOver = isSolved();
				}

				// repaint panel
				repaint();
			}
		});

		// add solve buttons
		add(bfs, BorderLayout.EAST);
		add(dfs, BorderLayout.EAST);
		add(a1, BorderLayout.EAST);
		add(a2, BorderLayout.EAST);
//		add(shuffle, BorderLayout.EAST);
		add(next, BorderLayout.EAST);

		// start new game
		newGame();
	}

	private void newGame() {
//		do {
//			reset(); // reset in initial state
//			// shuffle(); // shuffle
//		} while (isSolvable()); // make it until grid be solvable
		reset();
		gameOver = false;
	}

	private void reset() {
//		tiles[0] = 1;
//		tiles[1] = 2;
//		tiles[2] = 5;
//		tiles[3] = 3;
//		tiles[4] = 4;
//		tiles[5] = 0;
//		tiles[6] = 6;
//		tiles[7] = 7;
//		tiles[8] = 8;
//		blankPos = 5;
		for(int i = 0; i< tiles.length ; i++) {
			tiles[i] = Integer.valueOf((state.charAt(i))) - 48;
			if(tiles[i] == 0){
				blankPos = i;
			}
		}
	}

	
	private void solve(List<String> tilesStates, int i) {
		for (int j = 0; j < tilesStates.get(i).length(); j++) {
			tiles[j] = Character.getNumericValue(tilesStates.get(i).charAt(j));
		}
		repaint();
	}

//	private void shuffle() {
//		int n = nTiles;
//		while (n > 0) {
//			int r = ran.nextInt(n--);
//			int temp = tiles[r];
//			tiles[r] = tiles[n];
//			tiles[n] = temp;
//		}
//	}

	// only half permutations of the puzzle are solvable
	// whenever a tile is preceded by a tile with higher value it counts
	// as an inversion. in our case, with the blank tile in the solved position,
	// the number of inversions must be even for the puzzle to be solvable
//	private boolean isSolvable() {
//		int countInversions = 0;
//		for (int i = 0; i < nTiles; i++) {
//			for (int j = 0; j < i; j++) {
//				if (tiles[j] > tiles[i]) {
//					countInversions++;
//				}
//			}
//		}
//		return countInversions % 2 == 0;
//	}

	private boolean isSolved() {
		// if (tiles[0] != 0) { // if blank is not in the solved position ==>
		// not solved
		// return false;
		// }
		for (int i = 0; i < 9; i++) {
			if (tiles[i] != i) {
				return false;
			}
		}
		return true;
	}

	private void drawGrid(Graphics2D g) {
		// convert 1D coords to 2D coords given the size of the 2D array
		for (int i = 0; i < tiles.length; i++) {
			int r = i / size;
			int c = i % size;
			// convert in coords on the UI
			int x = margin + c * tileSize;
			int y = margin + r * tileSize;
			// check special case for blank tile
			if (tiles[i] == 0) {
				if (gameOver) {
					g.setColor(FOREGROUND_COLOR);
					drawCenteredString(g, "You Win", x, y);
				}
				continue;
			}
			// for other tiles
			// fill color
			g.setColor(getForeground());
			g.fillRoundRect(x, y, tileSize, tileSize, 25, 25);
			// borders
			g.setColor(Color.BLACK);
			g.drawRoundRect(x, y, tileSize, tileSize, 25, 25);
			// font color
			g.setColor(Color.WHITE);
			// draw number
			drawCenteredString(g, String.valueOf(tiles[i]), x, y);
		}
	}


	private void drawCenteredString(Graphics2D g, String s, int x, int y) {
		// center string s for the given tile
		FontMetrics fm = g.getFontMetrics();
		int asc = fm.getAscent();
		int desc = fm.getDescent();
		g.drawString(s, x + (tileSize - fm.stringWidth(s)) / 2, y + (asc + (tileSize - (asc - desc)) / 2));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawGrid(g2D);
		// drawStartMessage(g2D);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setTitle("8-puzzle");
			frame.setResizable(false);
			frame.add(new Gui(3, 550, 30), BorderLayout.CENTER);
			frame.pack();
			// center on the screen
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}
}
