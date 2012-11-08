package ClueBoard;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;



public abstract class Player {
	protected String name;
	protected ArrayList<Card> myCards = new ArrayList<Card>();
	protected int location;
	protected String color;
	protected Random rand = new Random();
	protected BoardCell lastRoom;
	
	public Player (String name, String color, int location ) {
		this.name = name;
		this.color = color;
		this.location = location;
	}
	
	public Card disproveSuggestion(Card person, Card room, Card weapon) {
		boolean gogo = false;
		int total = 0;
		ArrayList<Card> index = new ArrayList<Card>();
		for (Card i : myCards) {
			if (i.equals(person) || i.equals(room) || i.equals(weapon)) {
				total++;
				index.add(i);
				gogo = true;
			}					
		}
		if (gogo) {
			int target = rand.nextInt(index.size());
			return index.get(target);
		} else {
			return null;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Card> getMyCards() {
		return myCards;
	}

	public void addCard(Card card) {
		myCards.add(card);
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public BoardCell lastSeen() {
		return new RoomCell("how is this even possible?",0,0);
	}
	
	public void draw(Graphics g, int size, int numCols) {
		Color color;
		try {
		    Field field = Class.forName("java.awt.Color").getField(this.color);
		    color = (Color)field.get(null);
		} catch (Exception e) {
		    color = null; // Not defined
		}
		int row = location/numCols;
		int column = location%numCols;
		g.setColor(color);
		g.fillOval(column*size, row*size, size-1, size-1);
		
	}
}
