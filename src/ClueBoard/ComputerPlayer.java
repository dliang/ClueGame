package ClueBoard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import ClueBoard.Card.CardType;

public class ComputerPlayer extends Player{
	
	private int lastRoom = 0;
	private Board board;
	//private ArrayList<Card> deck;
	ArrayList<Card>deck;
	
	
	public ComputerPlayer(String name, String color, int location, BoardCell cell,Board board) {
		super(name, color, location);
		this.board = board;
		 deck = this.board.getAllCards();
		// TODO Auto-generated constructor stub
	}
	
	public BoardCell pickLocation() {
		long seed = System.nanoTime();		
		ArrayList<BoardCell> doorInRange = new ArrayList<BoardCell>();
		ArrayList<BoardCell> tempTargets = new ArrayList<BoardCell>(board.getTargets());
		for(BoardCell b : tempTargets){
			if(b.isDoorway() && ((RoomCell)b).getRoomInitial() != lastRoomVisited){
				doorInRange.add(b);
			}
		}
		if(doorInRange.size() > 0){
			
			Collections.shuffle(doorInRange, new Random(seed));
			return doorInRange.get(0);
		}else{
			Collections.shuffle(tempTargets, new Random(seed));
			return tempTargets.get(0);
		}
	
	}
	public int getLocation() {
		return location;
	}
	
	public ArrayList<Card> createSuggestion() {
		ArrayList<Card> suggestion = new ArrayList<Card>();	
		
		long seed = System.nanoTime();		
		Collections.shuffle(deck, new Random(seed));
		
		
		//put answer into solution, remove solution cards from deck 
		int a = 0, b = 0, c = 0;
		for (int i = 0; i < deck.size(); i++) {
			if (deck.get(i).getCardtype() == Card.CardType.PERSON && a == 0 && board.getUnseenCards().contains(deck.get(i))) {
				System.out.println(deck.get(i).getCardName());
				suggestion.add(deck.get(i));				
				a++;
			} else if (deck.get(i).getCardtype() == Card.CardType.ROOM && b == 0&&board.getUnseenCards().contains(deck.get(i))) {
				System.out.println(deck.get(i).getCardName());
				suggestion.add(deck.get(i));
				b++;
			} else if (deck.get(i).getCardtype() == Card.CardType.WEAPON && c == 0&&board.getUnseenCards().contains(deck.get(i))) {
				System.out.println(deck.get(i).getCardName());
				suggestion.add(deck.get(i));
				c++;
			} 
		}
		
		return suggestion;
	}
	
	/*
	public void updateSeen() {
		for (Card i : myCards) {
			if (!seenCards.contains(i)) {
				seenCards.add(i);
				deck.remove(i);
			}
		}		
	}
	*/
	
	public void takeTurn(){
		Random randomGen = new Random();
		boolean jAccuse = 9.0/Math.pow(board.getUnseenCards().size(),2)>= randomGen.nextDouble();
		if(jAccuse){			
			
			
		}else if(enteredRoom){
			board.handleSuggestion(createSuggestion(),this);
			enteredRoom = false;
		}else{
			board.calcTargets(location, board.rollDice());
			move(board,pickLocation());
		}		
	}
}
