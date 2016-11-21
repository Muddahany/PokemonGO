import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class GottaCatchemAll {

	private int n; // dimension of maze
	private boolean[][] north; // is there a wall to north of cell i, j
	private boolean[][] east;
	private boolean[][] south;
	private boolean[][] west;
	private boolean[][] visited;
	private boolean[][] pokemons;
	int collected = 0;
	// private boolean done = false;
	private int xloc;
	private int yloc;
	int eggTime;
	TreeNode root;
	ArrayList<TreeNode> queue;
	ArrayList<TreeNode> nodes;
	private int goalX;
	private int goalY;
	private int score;
	private int deep;
	private int pokes;
	private static PrintWriter writer;

	public GottaCatchemAll(int n) throws FileNotFoundException, UnsupportedEncodingException {
		pokes = 0;
		goalX = (int) (Math.random() * n);
		if (goalX == 0)
			goalX++;
		goalY = (int) (Math.random() * n);
		if (goalY == 0)
			goalY++;
		score = 0;
		queue = new ArrayList<TreeNode>();
		nodes = new ArrayList<TreeNode>();
		xloc = (int) (Math.random() * n);
		if (xloc == 0)
			xloc++;
		yloc = (int) (Math.random() * n);
		if (yloc == 0)
			yloc++;
		this.n = n;
		pokemons = new boolean[n + 1][n + 1];
		StdDraw.setXscale(0, n + 2);
		StdDraw.setYscale(0, n + 2);
		init();
		eggTime = (int) (Math.random() * n * n);
		GenMaze();
		root = new TreeNode(null, null, 0, 0, xloc, yloc);
		for (int i = 0; i < visited.length; i++)
			for (int j = 0; j < visited[i].length; j++)
				visited[i][j] = false;
		createSearchTree(root);
		resetVisitation();
		queue.add(root);
		writer.println("at(" + (xloc) + "," + (yloc) + "," + eggTime + "," + pokes + ",s0,nil,20).");
		writer.println("goal(" + (goalX) + "," + (goalY) + "," + 0 + "," + 0 + ").");
	}

	private void resetVisitation() {
		for (int i = 0; i < visited.length; i++)
			for (int j = 0; j < visited[i].length; j++)
				visited[i][j] = false;
	}

	private void createSearchTree(TreeNode root) {
		if (north[root.x][root.y] && east[root.x][root.y] && west[root.x][root.y] && south[root.x][root.y])
			return;
		visited[root.x][root.y] = true;
		nodes.add(root);
		if (!north[root.x][root.y] && !visited[root.x][root.y + 1]) {
			createSearchTree(
					new TreeNode(root, Operator.Forward, root.depth + 1, root.pathCost + 1, root.x, root.y + 1));

		}
		if (!east[root.x][root.y] && !visited[root.x + 1][root.y]) {
			createSearchTree(new TreeNode(root, Operator.Right, root.depth + 1, root.pathCost + 1, root.x + 1, root.y));
		}
		if (!west[root.x][root.y] && !visited[root.x - 1][root.y]) {
			createSearchTree(new TreeNode(root, Operator.Left, root.depth + 1, root.pathCost + 1, root.x - 1, root.y));
		}
		if (!south[root.x][root.y] && !visited[root.x][root.y - 1]) {
			createSearchTree(new TreeNode(root, Operator.Back, root.depth + 1, root.pathCost + 1, root.x, root.y - 1));
		}
	}

	private void init() {
		// initialize border cells as already visited
		visited = new boolean[n + 2][n + 2];
		for (int x = 0; x < n + 2; x++) {
			visited[x][0] = true;
			visited[x][n + 1] = true;
		}
		for (int y = 0; y < n + 2; y++) {
			visited[0][y] = true;
			visited[n + 1][y] = true;
		}

		// initialze all walls as present
		north = new boolean[n + 2][n + 2];
		east = new boolean[n + 2][n + 2];
		south = new boolean[n + 2][n + 2];
		west = new boolean[n + 2][n + 2];
		for (int x = 0; x < n + 2; x++) {
			for (int y = 0; y < n + 2; y++) {
				north[x][y] = true;
				east[x][y] = true;
				south[x][y] = true;
				west[x][y] = true;
			}
		}
		// initialize Pokemon location
		for (int i = 1; i < n + 1; i++) {
			for (int j = 1; j < n + 1; j++) {
				if (Math.random() < 0.25) {
					this.pokemons[i][j] = true;
					pokes++;
					writer.println("poky(" + (i) + "," + (j) +  ",s0).");
					writer.println("collected(" + (i) + "," + (j) +  ",s0).");
				} else {
					this.pokemons[i][j] = false;
				}
			}
		}
	}

	// generate the maze
	private void generate(int x, int y) {
		visited[x][y] = true;

		// while there is an unvisited neighbor
		while (!visited[x][y + 1] || !visited[x + 1][y] || !visited[x][y - 1] || !visited[x - 1][y]) {

			// pick random neighbor (could use Knuth's trick instead)
			while (true) {
				double r = StdRandom.uniform(4);
				if (r == 0 && !visited[x][y + 1]) {
					north[x][y] = false;
					south[x][y + 1] = false;
					generate(x, y + 1);
					break;
				} else if (r == 1 && !visited[x + 1][y]) {
					east[x][y] = false;
					west[x + 1][y] = false;
					generate(x + 1, y);
					break;
				} else if (r == 2 && !visited[x][y - 1]) {
					south[x][y] = false;
					north[x][y - 1] = false;
					generate(x, y - 1);
					break;
				} else if (r == 3 && !visited[x - 1][y]) {
					west[x][y] = false;
					east[x - 1][y] = false;
					generate(x - 1, y);
					break;
				}
			}
		}
	}

	// generate the maze starting from lower left
	private void GenMaze() {
		generate(1, 1);

		// delete some random walls
		for (int i = 0; i < n; i++) {
			int x = 1 + StdRandom.uniform(n - 1);
			int y = 1 + StdRandom.uniform(n - 1);
			north[x][y] = south[x][y + 1] = false;
		}

		// add some random walls
		// for (int i = 0; i < 5; i++) {
		// int x = n / 2 + StdRandom.uniform(n / 2);
		// int y = n / 2 + StdRandom.uniform(n / 2);
		// east[x][y] = west[x + 1][y] = true;
		// }

	}

	public void Search(String strategy, boolean visualize) {
		char c = strategy.charAt(0);
		switch (c) {
		case 'D':
			searchD(root);
			break;
		case 'B':
			searchB(root);
			break;
		case 'I':
			for (int i = 0; i < n; i++) {
				searchI(root, i);
				resetVisitation();
				deep = 0;
			}
			break;
		case 'U':// same as BFS since I assumed the path cost is the same for
					// every node
			searchU();
			break;
		case 'G':
			searchG();
			break;
		case 'A':
			searchA();
			break;
		default:
			break;
		}
	}

	private void searchD() {

	}

	private void searchB(TreeNode root) {
		if (visited[root.x][root.y]) {
			queue.remove(0);
			if (!queue.isEmpty()) {
				searchB(queue.get(0));
			} else {
				return;
			}
		}
		visited[root.x][root.y] = true;
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.filledCircle(root.x + 0.5, root.y + 0.5, 0.25);
		if (this.pokemons[root.x][root.y]) {
			score++;
			StdDraw.setPenColor(StdDraw.YELLOW);
			StdDraw.filledCircle(root.x + 0.5, root.y + 0.5, 0.15);
		}
		this.eggTime--;
		StdDraw.show();
		StdDraw.pause(120);
		if (!queue.isEmpty())
			queue.remove(0);
		if (!south[root.x][root.y] && !visited[root.x][root.y - 1]) {
			queue.add(search(root.x, root.y - 1));
		}
		if (!west[root.x][root.y] && !visited[root.x - 1][root.y]) {
			queue.add(search(root.x - 1, root.y));
		}
		if (!east[root.x][root.y] && !visited[root.x + 1][root.y]) {
			queue.add(search(root.x + 1, root.y));
		}
		if (!north[root.x][root.y] && !visited[root.x][root.y + 1]) {
			queue.add(search(root.x, root.y + 1));
		}
		for (int i = 0; i < queue.size(); i++) {
			System.out.println(queue.get(i).x + ", " + queue.get(i).y);
		}
		System.out.println("");
		if (!queue.isEmpty())
			searchB(queue.get(0));

	}

	private void searchA() {
		// TODO Auto-generated method stub

	}

	private void searchG() {
		// TODO Auto-generated method stub

	}

	private void searchU() {
		// TODO Auto-generated method stub

	}

	private void searchD(TreeNode root) {
		if (visited[root.x][root.y]) {
			queue.remove(0);
			if (!queue.isEmpty()) {
				searchD(queue.get(0));
			} else {
				return;
			}
		}
		visited[root.x][root.y] = true;
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.filledCircle(root.x + 0.5, root.y + 0.5, 0.25);
		if (this.pokemons[root.x][root.y]) {
			score++;
			StdDraw.setPenColor(StdDraw.YELLOW);
			StdDraw.filledCircle(root.x + 0.5, root.y + 0.5, 0.15);
		}
		this.eggTime--;
		StdDraw.show();
		StdDraw.pause(30);

		if (!queue.isEmpty())
			queue.remove(0);
		if (!south[root.x][root.y] && !visited[root.x][root.y - 1]) {
			queue.add(0, search(root.x, root.y - 1));
		}
		if (!west[root.x][root.y] && !visited[root.x - 1][root.y]) {
			queue.add(0, search(root.x - 1, root.y));
		}
		if (!east[root.x][root.y] && !visited[root.x + 1][root.y]) {
			queue.add(0, search(root.x + 1, root.y));
		}
		if (!north[root.x][root.y] && !visited[root.x][root.y + 1]) {
			queue.add(0, search(root.x, root.y + 1));
		}
		System.out.println("");
		if (!queue.isEmpty())
			searchD(queue.get(0));

	}

	// couldnt cause the tree created created using bfs
	private void searchI(TreeNode root, int limit) {
		if (queue.isEmpty() || limit < root.depth)
			return;
		if (visited[root.x][root.y]) {
			queue.remove(0);
			if (!queue.isEmpty()) {
				searchI(queue.get(0), limit);
			} else {
				return;
			}
		}
		visited[root.x][root.y] = true;
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.filledCircle(root.x + 0.5, root.y + 0.5, 0.25);
		if (this.pokemons[root.x][root.y]) {
			score++;
			StdDraw.setPenColor(StdDraw.YELLOW);
			StdDraw.filledCircle(root.x + 0.5, root.y + 0.5, 0.15);
		}
		StdDraw.show();
		StdDraw.pause(30);
		this.eggTime--;
		if (!queue.isEmpty())
			queue.remove(0);
		if (!south[root.x][root.y] && !visited[root.x][root.y - 1]) {
			queue.add(0, search(root.x, root.y - 1));
		}
		if (!west[root.x][root.y] && !visited[root.x - 1][root.y]) {
			queue.add(0, search(root.x - 1, root.y));
		}
		if (!east[root.x][root.y] && !visited[root.x + 1][root.y]) {
			queue.add(0, search(root.x + 1, root.y));
		}
		if (!north[root.x][root.y] && !visited[root.x][root.y + 1]) {
			queue.add(0, search(root.x, root.y + 1));
		}
		System.out.println("");
		if (!queue.isEmpty())
			searchI(queue.get(0), limit);
	}

	private TreeNode search(int x, int y) {
		for (int i = 0; i < this.nodes.size(); i++)
			if (nodes.get(i).x == x && this.nodes.get(i).y == y)
				return nodes.get(i);
		return null;

	}

	public void draw() throws FileNotFoundException {
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.filledCircle(goalX + 0.5, goalY + 0.5, 0.375);
		StdDraw.filledCircle(xloc + 0.5, yloc + 0.5, 0.375);

		StdDraw.setPenColor(StdDraw.BLACK);

		for (int x = 1; x <= n; x++) {
			for (int y = 1; y <= n; y++) {
				if (south[x][y]) {
					StdDraw.line(x, y, x + 1, y);
					writer.println("wall(" + (x) + "," + (y) + "," + (x) + "," + (y - 1) + ").");
				}
				if (north[x][y]) {
					StdDraw.line(x, y + 1, x + 1, y + 1);
					writer.println("wall(" + (x) + "," + (y) + "," + (x) + "," + (y + 1) + ").");
				}
				if (west[x][y]) {
					StdDraw.line(x, y, x, y + 1);
					writer.println("wall(" + (x) + "," + (y) + "," + (x - 1) + "," + (y) + ").");
				}
				if (east[x][y]) {
					StdDraw.line(x + 1, y, x + 1, y + 1);
					writer.println("wall(" + (x) + "," + (y) + "," + (x + 1) + "," + (y) + ").");
				}
			}

		}

		StdDraw.show();
		StdDraw.pause(1000);
	}

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		writer = new PrintWriter("mini.pl");
		GottaCatchemAll maze = new GottaCatchemAll(3);

		StdDraw.enableDoubleBuffering();
		maze.draw();
		maze.Search("D", true);
		System.out.println(maze.score);
		// maze.solve();
		writer.close();
	}
}