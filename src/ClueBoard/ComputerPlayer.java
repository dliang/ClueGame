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
	ArrayList<Card> myUnseenCards;
	//private ArrayList<Card> deck;

	
	
	public ComputerPlayer(String name, String color, int location, BoardCell cell,Board board) {
		super(name, color, location);
		this.board = board;
		myUnseenCards = new ArrayList<Card>();
		// TODO Auto-generated constructor stub
	}
	
	public BoardCell pickLocation() {
		long seed = System.nanoTime();		
		ArrayList<BoardCell> doorInRange = new ArrayList<BoardCell>();
		ArrayList<BoardCell> tempTargets = new ArrayList<BoardCell>(board.getTargets());
		Collections.shuffle(tempTargets);
		for(BoardCell  b : tempTargets){
			
			if(b.isDoorway()){
				if(b.getRoomInitial() != lastRoomVisited){
					doorInRange.add(b);
				}				
			}
		}
		if(doorInRange.size() > 0){
			Collections.shuffle(doorInRange);
			return doorInRange.get(0);
		}else{		
			return tempTargets.get(0);
		}	
	}
	public int getLocation() {
		return location;
	}
	
	public ArrayList<Card> createAccusation() {
		ArrayList<Card> suggestion = new ArrayList<Card>();	
		ArrayList<Card> deck = myUnseenCards;
		long seed = System.nanoTime();		
		Collections.shuffle(deck, new Random(seed));
		
		
		//put answer into solution, remove solution cards from deck 
		int a = 0, b = 0, c = 0;
		for (int i = 0; i < deck.size(); i++) {
			if (deck.get(i).getCardtype() == Card.CardType.PERSON && a == 0) {
				System.out.println(deck.get(i).getCardName());
				suggestion.add(deck.get(i));				
				a++;
			} else if (deck.get(i).getCardtype() == Card.CardType.ROOM && b == 0) {
				System.out.println(deck.get(i).getCardName());
				suggestion.add(deck.get(i));
				b++;
			} else if (deck.get(i).getCardtype() == Card.CardType.WEAPON && c == 0) {
				System.out.println(deck.get(i).getCardName());
				suggestion.add(deck.get(i));
				c++;
			} 
		}
		
		return suggestion;
	}
	public ArrayList<Card> createSuggestion() {
		ArrayList<Card> suggestion = new ArrayList<Card>();	
		ArrayList<Card> deck = new ArrayList<Card>(myUnseenCards);
		
		long seed = System.nanoTime();		
		Collections.shuffle(deck, new Random(seed));
		
		
		//put answer into solution, remove solution cards from deck 
		int a = 0, b = 0, c = 0;
		for (int i = 0; i < deck.size(); i++) {
			if (deck.get(i).getCardtype() == Card.CardType.PERSON && a == 0) {
				//System.out.println(deck.get(i).getCardName());
				suggestion.add(deck.get(i));
				for(Player p : board.getPlayerList()){
					if(p.getName().equals(deck.get(i).getCardName())){
						p.move(board,board.getCellAt(location));
						p.lastRoomVisited = lastRoomVisited;
						break;
					}
				}
				a++;
			}else if (deck.get(i).getCardtype() == Card.CardType.WEAPON && c == 0) {
				//System.out.println(deck.get(i).getCardName());
				suggestion.add(deck.get(i));
				c++;
			} 			
		}		
		suggestion.add(new Card(board.getRooms().get(lastRoomVisited),CardType.ROOM));
		for(Card ca : suggestion){
			System.out.println(ca.getCardName());
		}
	//	System.out.println(suggestion.size());
		return suggestion;
	}
	
	public void takeTurn(){
		myUnseenCards = new ArrayList<Card>(board.getUnseenCards());
		for(Card asd : myCards){
			myUnseenCards.remove(asd);
		}
		Random randomGen = new Random();
		boolean jAccuse = 729.0/Math.pow(myUnseenCards.size(),6) >= randomGen.nextDouble();
		if(jAccuse){			
			board.winner = board.checkAccusation(createAccusation());
		}else if(enteredRoom){
			board.handleSuggestion(createSuggestion(),this);
			enteredRoom = false;
		}else{
			board.calcTargets(location, board.getDiceRoll());
			move(board,pickLocation());
			
		}
		board.repaint();
	}
}
