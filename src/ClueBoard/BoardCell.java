package ClueBoard;

import java.awt.Graphics;

public abstract class BoardCell {
	protected int row;
	protected int column;
	

	public char getRoomInitial(){
		return ';';
	}
	public boolean isWalkway() {
		return false;
	}
	
	public boolean isRoom() {
		return false;
	}
	
	public boolean isDoorway() {
		return false;
	}
	public boolean isNameCell() {
		return false;
	}
	public BoardCell getBoardCell() {
		return this;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return column;
	}
	
	public void setRow(int r){
		row = r;
	}
	
	public void setCol(int c){
		column = c;
	}
	
	public abstract void draw(Graphics g, int size);
	
}
