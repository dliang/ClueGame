package intBoard;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;


public class IntBoard {

	static int ROW=4;
	static int COLS=4;
	static int GRID_PIECES=16;
	private Map<Integer, LinkedList<Integer>> adjMtx = new HashMap<Integer, LinkedList<Integer>>();
	private Stack<Integer> path = new Stack<Integer>();
	private Set<Integer> targets = new HashSet<Integer>();
	
	

	
	public void calcAdjacencies(int row, int col){
		LinkedList<Integer> list = new LinkedList<Integer>();
		adjMtx.put(calcIndex(row,col), list);
		for(int i=0; i<=1; i++){
			double testRow=row+java.lang.Math.pow(-1.0,i);
			double testIndex= calcIndex(testRow,col);
			if(testIndex!=-1){
				adjMtx.get(calcIndex(row,col)).add((int) testIndex);
			}
			
		}
		for(int j=0; j<=1; j++){
			double testCol=col+ java.lang.Math.pow(-1.0,j);
			if(testCol>col && col==COLS-1){
				continue;
			}else if(testCol<col && col==0){
				continue;
			}
			double testIndex= calcIndex(row,testCol);
			if(testIndex!=-1){
				adjMtx.get(calcIndex(row,col)).add((int) testIndex);
			}
		}
	}
	
	public void calcTargets(int position, int steps){
		//Record positions
		path.push(position);
		
		//Create set to hold available positions to move to
		Set<Integer> available = new HashSet<Integer>();

		//Calculate adjacencies for the selected position
		calcAdjacencies(getRowCol(position)[1],getRowCol(position)[0]);
		
		//Add adjacent positions to the available moves
		for(int i:adjMtx.get(position)){
			available.add(i);
		}
		
		//When all steps have been taken, move all available moves to targets
		//then quit
		if(steps==1){
			for(int i:available){
				targets.add(i);
			}
			return;
		}
		
		//Call calcTargets for all available positions you can move to
		for(int i:available){
			calcTargets(i,steps-1);
		}
		
		//Remove original position from targets
		if (targets.contains(path.firstElement())) {
			targets.remove(path.firstElement());
		}
		
	}
	
	public HashSet<Integer> getTargets(){
		return (HashSet<Integer>) targets;
	}
	
	public LinkedList<Integer> getAdjList(int row, int col){
		calcAdjacencies(row,col);
		int location = calcIndex(row,col);
		return adjMtx.get(location);
	}
	
	public int calcIndex(double row, double col){
		if( (int) (COLS*row+col)>= GRID_PIECES || (int) (COLS*row+col) <0){
			return -1;
		}
		return (int) (COLS*row+col);
	}

	public int[] getRowCol(int position){
		int temp[] = new int[2];
		temp[0]=(position%COLS);
		temp[1]=(position/ROW);
		return temp;
	}
	
}
