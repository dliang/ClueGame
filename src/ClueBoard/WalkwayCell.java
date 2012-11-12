package ClueBoard;

import java.awt.Color;
import java.awt.Graphics;

public class WalkwayCell extends BoardCell {
	public WalkwayCell(int index,int numCols){
		row = index/numCols;
		column = index%numCols;
	}
	public boolean isWalkway() {
		return true;
	}
	public void draw(Graphics g,int size,boolean playerTurn) {
		if(playerTurn){
			g.setColor(Color.cyan);
		}else{
			g.setColor(Color.YELLOW);
		}
		g.fillRect(column*size, row*size, size-1, size-1);
	}
}
