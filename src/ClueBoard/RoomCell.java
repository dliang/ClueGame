package ClueBoard;

import java.awt.Color;
import java.awt.Graphics;

public class RoomCell extends BoardCell {
	public enum DoorDirection {UP, DOWN, LEFT, RIGHT, NONE};
	private int percentFill = 5;
	private DoorDirection doorDirection;
	private char roomInitial;
	private boolean name = false;
	
	public RoomCell (String temp,int index,int numCols){
		row = index/numCols;
		column = index%numCols;
		roomInitial = temp.charAt(0);
		if(temp.length() > 1) {
			char direction = temp.charAt(1);
			if(direction == 'U') doorDirection = DoorDirection.UP;
			if(direction == 'D') doorDirection = DoorDirection.DOWN;
			if(direction == 'L') doorDirection = DoorDirection.LEFT;
			if(direction == 'R') doorDirection = DoorDirection.RIGHT;
			if(direction == 'N') {
				doorDirection = DoorDirection.NONE;
				name = true;
			} 
		}
		else 
			doorDirection=DoorDirection.NONE;
		
	}
	
	public boolean isRoom() {
		return true;
	}
	
	@Override
	public boolean isNameCell() {
		return name;
	}
	
	public boolean isDoorway(){
		if(doorDirection!=DoorDirection.NONE){
			return true;
		}else{
			return false;
		}
	}
	@Override
	public char getRoomInitial() {
		return roomInitial;
	}
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	public void draw(Graphics g,int size,boolean playerTurn) {
		if(playerTurn){
			g.setColor(Color.cyan);
		}else{
			g.setColor(Color.GRAY);
		}
		
		g.fillRect(column*size,row*size,size,size);
		g.setColor(Color.BLUE);
		switch(doorDirection){
		case UP:g.fillRect(column*size,row*size, size, size/percentFill);break;
		case DOWN:g.fillRect(column*size,(row+1)*size-size/percentFill, size, size/percentFill);break;
		case LEFT:g.fillRect(column*size,row*size, size/percentFill, size);break;
		case RIGHT:g.fillRect((column+1)*size-size/percentFill,row*size, size/percentFill, size);break;
		case NONE:break;
		}
	}
}
