import java.util.ArrayList;

public abstract class GenericProblem {
	ArrayList<Operator> operators;
	TreeNode initialState;
	//ArrayList<TreeNode> stateSpace;
	TreeNode goalTest;
	boolean done=false;
	int pathCost=0;
	
	public GenericProblem(){
		
	}
	public int pathCost(){
		if(done){
			return pathCost;
		}else return 0;
		
		
	}
}
