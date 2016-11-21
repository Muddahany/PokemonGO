
public class TreeNode {
	TreeNode parent;
	Operator operator;
	int depth;
	int pathCost;
	int x;
	int y;

	public TreeNode(TreeNode parent, Operator op, int depth, int pathCost,int x ,int y) {
		this.parent = parent;
		this.operator = op;
		this.depth = depth;
		this.pathCost = pathCost;
		this.x=x;
		this.y=y;
	}

}
