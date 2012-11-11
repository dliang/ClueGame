package ClueBoard;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ClueBoard.Card.CardType;
import ClueBoard.RoomCell.DoorDirection;


public class Board extends JPanel{
	private ArrayList<BoardCell> cells;
	private Map<Character, String> rooms;
	private int numRows;
	private int numColumns;
	private int startPosition=-1;
	private int GUI_COMP_SIZE;
	private int curTurn = 0;
	private int diceRoll;
	public boolean winner;
	
	private boolean[] seen;
	private Map<Integer, LinkedList<Integer>> adjMtx = new HashMap<Integer, LinkedList<Integer>>();
	private Stack<Integer> path = new Stack<Integer>();
	private Set<BoardCell> targets = new HashSet<BoardCell>();
	private Stack<Integer> doorsFound = new Stack<Integer>();
	private Stack<Integer> removed = new Stack<Integer>();
	
	//*********************************************************
	private Solution sol;
	private ArrayList<Player> players;
	private ArrayList<Card> allCards;
	private ArrayList<Card> unseenCards;
	private ArrayList<Card> cards;
	private ArrayList<Card> solution = new ArrayList<Card>();
	private Map<Integer, LinkedList<Integer>> adjLST;
	
	public Board(String config, String legend, String people, String deck,int compSize){
		adjLST = new HashMap<Integer, LinkedList<Integer>>();
		GUI_COMP_SIZE = compSize;
		try{
			loadConfigFiles(config,legend,people,deck);
		}catch(Exception e){
			System.out.println(e.getMessage());			
		}
		deal();
		unseenCards = new ArrayList<Card>(allCards);
		setBackground (Color.black); 
		calcAdjacencies();
		String splash = "You are playing as " + players.get(0).getName() + " press next player to begin play";
		JOptionPane.showMessageDialog(null, splash,"Welcome to Clue",JOptionPane.PLAIN_MESSAGE);
		rollDice();
	}	
	
	public void nextTurn(){
	//	System.out.println(players.get(curTurn).getColor());
	//	printTargets();
	//	System.out.println(players.get(curTurn).enteredRoom);
		
		if(!winner){
		players.get(curTurn).takeTurn();
			if(!winner){
				curTurn = (curTurn+1)%players.size();
			}
		rollDice();
		repaint();
		
		}else{
			String win = "the solution was "+ solution.get(0).getCardName() +" in the " + solution.get(1).getCardName() + " with the " +solution.get(2).getCardName() ;
			JOptionPane.showMessageDialog(null, win,players.get(curTurn).getName() + " wins",JOptionPane.PLAIN_MESSAGE);
		}
		System.out.println(unseenCards.size());
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(BoardCell b : cells){
		//	System.out.println("ere");
			b.draw(g,GUI_COMP_SIZE);
		}
		
		for(Player p : players) {
			p.draw(g, GUI_COMP_SIZE, numColumns);
		}
		
		for (BoardCell b : cells) {
			if (b.isNameCell())
				g.drawString(rooms.get(((RoomCell)b).getRoomInitial()), b.getCol() * GUI_COMP_SIZE, b.getRow()*GUI_COMP_SIZE);
			
		}
	}
	public int getCurTurn(){
		return curTurn;
	}
	public int getDiceRoll(){
		return diceRoll;
	}
	public int rollDice(){
		Random randomGen = new Random();
		diceRoll = randomGen.nextInt(6)+1;
		return diceRoll;
	}
	public ArrayList<Card> getAllCards(){
		return allCards;
	}
	public ArrayList<Card> getUnseenCards(){
		return unseenCards;
	}

	public void loadConfigFiles(String config, String legend, String people, String deck) throws IOException, BadConfigFormatException {
		rooms = new TreeMap<Character,String>();
		cells = new ArrayList<BoardCell>();
		
		//Read in Legend
		FileReader reader = new FileReader(legend);
		Scanner in = new Scanner(reader);
		while(in.hasNext()){
			String temp = in.nextLine();
			char roomChar =  temp.substring(0,1).charAt(0);
			String room = temp.substring(3);
			rooms.put(roomChar, room);
		}
		reader.close();
				
		//Read in config
		numRows = 0;
		numColumns = 0;
		reader = new FileReader(config);
		in = new Scanner(reader);
		while(in.hasNext()){
			String[] temp = in.nextLine().split(",");
			numRows++;
			if(numColumns==0){
				numColumns = temp.length;
			}
			if(temp.length != numColumns){
				throw new BadConfigFormatException("Inconsistent Row Lengths");
			}
		}
		reader.close();
		
		reader = new FileReader(config);
		in = new Scanner(reader);
		int counter = 0;
		while(in.hasNext()){
			
			String[] temp = in.nextLine().split(",");
			for(int i=0; i<numColumns; i++){
				if(!rooms.containsKey(temp[i].charAt(0)) && temp[i].length() == 1){
					throw new BadConfigFormatException("Room Character Not Found");
				}
				String doorDirections = "UDLRN";
				if(temp[i].length() == 2 && (!rooms.containsKey(temp[i].charAt(0)) || doorDirections.indexOf(temp[i].charAt(1))==-1 )){
					throw new BadConfigFormatException("Room Character Not Found or Invalid Door Direction");
				}				
				if(temp[i].equals("W")){
					cells.add(new WalkwayCell(counter,numColumns));
				}else{
					cells.add(new RoomCell(temp[i],counter,numColumns));
				}
				counter++;
				
			}
		}
		reader.close();
		
		
		//******************************************************
		//loading in people from file
		int x = 0;
		players = new ArrayList<Player>();
		reader = new FileReader(people);
		in = new Scanner(reader);
		while (in.hasNextLine()) {
			String[] line = in.nextLine().split("\\, ");
			if (x == 0){
				//players.add(new HumanPlayer(line[0], line[1], Integer.parseInt(line[2])));
				players.add(new ComputerPlayer(line[0], line[1], Integer.parseInt(line[2]), getCellAt(Integer.parseInt(line[2])),this));
			}else{
				players.add(new ComputerPlayer(line[0], line[1], Integer.parseInt(line[2]), getCellAt(Integer.parseInt(line[2])),this));
			}
			x++;
		}
		reader.close();
		
		//loading cards from file
		x = 1;
		reader = new FileReader(deck);
		in = new Scanner(reader);
		cards = new ArrayList<Card>();
		
		while (in.hasNextLine()) {
			if (x <= 6) {
				cards.add(new Card(in.nextLine(), Card.CardType.PERSON));
			} else if (x > 6 && x <= 15) {
				cards.add(new Card(in.nextLine(), Card.CardType.ROOM));
			} else if (x > 15) {
				cards.add(new Card(in.nextLine(), Card.CardType.WEAPON));
			}
			x++;
		}
		reader.close();
	}
	
	public void printTest() {
		for (int i = 0; i < cards.size(); i++) {
			System.out.println(cards.get(i).getCardName());
		}
		System.out.println("`````````````````");
		for (int i = 0; i < players.size(); i++) {
			System.out.println(players.get(i).getName());
		}
	}
	
	public int calcIndex(double testRow, double testCol) {
		double index = numColumns*testRow+testCol;
		if( (int) index>= numColumns*numRows || (int) index <0){
			return -1;
		}
		return (int) index;
	}
	
	public RoomCell GetRoomCellAt(int row, int column){
		return (RoomCell) cells.get(calcIndex(row,column));
	}
	
	public Map<Character, String> getRooms() {
		
		return rooms;
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	public BoardCell getCellAt(int cell) {
		return cells.get(cell);
	}

	public void calcAdjacencies() {
		LinkedList<Integer> tempList = new LinkedList<Integer>();
		for (int i=0; i<numRows; i++){
			for (int j=0; j<numColumns; j++){
				if(cells.get(calcIndex(i, j)).isWalkway()) {
					if (i-1>=0){
						if(cells.get(calcIndex(i - 1, j)).isWalkway()) {
							tempList.addLast(this.calcIndex(i-1, j));
						}  else if(cells.get(calcIndex(i -1, j)).isDoorway()){
							 RoomCell a = (RoomCell)cells.get(calcIndex(i - 1, j));
							 if(a.getDoorDirection() == DoorDirection.DOWN) {
								 tempList.addLast(this.calcIndex(i -1, j));
							 }
						 }
					} 
					if (i+1<numRows){
						if(cells.get(calcIndex(i + 1, j)).isWalkway()) {
							tempList.addLast(this.calcIndex(i+1, j));
						} else if(cells.get(calcIndex(i + 1, j)).isDoorway()){
							 RoomCell a = (RoomCell)cells.get(calcIndex(i + 1, j));
							 if(a.getDoorDirection() == DoorDirection.UP) {
								 tempList.addLast(this.calcIndex(i + 1, j));
							 }
						 }
					}
					if (j-1>=0){
						 if(cells.get(calcIndex(i, j - 1)).isWalkway()) {
							tempList.addLast(this.calcIndex(i, j-1));
						 } else if(cells.get(calcIndex(i, j - 1)).isDoorway()){
							 RoomCell a = (RoomCell)cells.get(calcIndex(i, j - 1));
							 if(a.getDoorDirection() == DoorDirection.RIGHT) {
								 tempList.addLast(this.calcIndex(i, j-1));
							 }
						 }
					}
					if (j+1<numColumns){
						 if(cells.get(calcIndex(i, j + 1)).isWalkway()) {
							tempList.addLast(this.calcIndex(i, j+1));
						 } else if(cells.get(calcIndex(i, j + 1)).isDoorway()){
							 RoomCell a = (RoomCell)cells.get(calcIndex(i, j + 1));
							 if(a.getDoorDirection() == DoorDirection.LEFT) {
								 tempList.addLast(this.calcIndex(i, j+1));
							 }
						 }
					}
				} else if((cells.get(calcIndex(i, j)).isRoom()) && (cells.get(calcIndex(i, j)).isDoorway())) {
						RoomCell a = (RoomCell)cells.get(calcIndex(i, j));
						if(cells.get(calcIndex(i, j)).isDoorway()) {
							if(a.getDoorDirection() == DoorDirection.RIGHT) {
								tempList.addLast(this.calcIndex(i, j + 1));
							}
							if(a.getDoorDirection() == DoorDirection.LEFT) {
								tempList.addLast(this.calcIndex(i, j - 1));
							}
							if(a.getDoorDirection() == DoorDirection.UP) {
								tempList.addLast(this.calcIndex(i - 1, j));
							}
							if(a.getDoorDirection() == DoorDirection.DOWN) {
								tempList.addLast(this.calcIndex(i + 1, j));
							}
						}
					} else {
						adjLST.put(this.calcIndex(i,j), tempList);
					}
				for (Integer k : tempList){
						adjLST.put(this.calcIndex(i, j), tempList);
				}
				tempList = new LinkedList<Integer>();
			}
		}
	}
	//***************************
	public void printTargets() {
		for(BoardCell b : targets){
			System.out.println(calcIndex(b.getRow(),b.getCol()));
		}
	}

	
	//clears targets
	public void clearTargets(){
		targets.clear();
	}
	
	
	public void calcTargets(int vertex, int steps) {
		targets = new HashSet<BoardCell>();
		int start = vertex;
		targets = new HashSet<BoardCell>();
		seen = new boolean[numRows*numColumns];
		this.setSeen();
		seen[start]=true;
		LinkedList<Integer> path = new LinkedList<Integer>();
		this.recurseTargets(start, path, steps);
	}

	private void setSeen(){
		for (int i=0; i<numRows*numColumns; i++){
			seen[i]=false;
		}
	}

	private void recurseTargets(int target, LinkedList<Integer> path, int steps){
		LinkedList<Integer> tempAdj=new LinkedList<Integer>();
		ListIterator<Integer> itr = this.getAdjList(target).listIterator();
		while (itr.hasNext()){
			int next=itr.next();
			if (seen[next]==false){
				tempAdj.add(next);
			}
		}
		ListIterator<Integer> itrAdj = tempAdj.listIterator();
		while (itrAdj.hasNext()){
			int nextNode=itrAdj.next();
			seen[nextNode]=true;
			path.push(nextNode);
			if (path.size() == steps){
				targets.add(cells.get(nextNode));
			} else if (cells.get(nextNode).isDoorway()) {
				targets.add(cells.get(nextNode));
			} else {
				recurseTargets(nextNode, path, steps);
			}
			path.removeLast();
			seen[nextNode]=false;
		}
	}
	public Set<BoardCell> getTargets(){
		return targets;
	}
	public LinkedList<Integer> getAdjList(int index) {
		LinkedList<Integer> a = new LinkedList<Integer> (adjLST.get(index));
		return a;
	}

	//returns the row and column of an index contained in an array, where a[0] = column, a[1] = row
	public int[] getRowCol(double testIndex){

		int temp[] = new int[2];
		temp[0] = (int) (testIndex%numColumns);
		temp[1] = (int) (testIndex/numRows);
		return temp;
	}
	
	//***********************************************
	public void selectAnswer() {
		
	}
	
	public void deal(ArrayList<String> cardList) {
		
	}
	
	public void deal() {
		//shuffle player cards
		long seed = System.nanoTime();		
		Collections.shuffle(cards, new Random(seed));
		allCards = new ArrayList<Card>(cards);
		
		//put answer into solution, remove solution cards from deck 
		//int a = 0, b = 0, c = 0;
		
		for (int i = 0; i < cards.size(); i++) {
			if(cards.get(i).cardtype == CardType.PERSON){
				solution.add(cards.get(i));
				cards.remove(cards.get(i));
				break;
			}
		}
		for (int i = 0; i < cards.size(); i++) {
			if(cards.get(i).cardtype == CardType.ROOM){
				solution.add(cards.get(i));
				cards.remove(cards.get(i));
				break;
			}
		}
		for (int i = 0; i < cards.size(); i++) {
			if(cards.get(i).cardtype == CardType.WEAPON){
				solution.add(cards.get(i));
				cards.remove(cards.get(i));
				break;
			}
		}
		for(Card c : solution){
			System.out.println(c.getCardName());
		}
		String person = "";
		String room = "";
		String weapon = "";
		for (int i = 0; i < 3; i++) {
			if (solution.get(i).getCardtype() == Card.CardType.PERSON)
				person = solution.get(i).getCardName();
			else if (solution.get(i).getCardtype() == Card.CardType.ROOM)
				room = solution.get(i).getCardName();
			else if (solution.get(i).getCardtype() == Card.CardType.WEAPON)
				person = solution.get(i).getCardName();
		}
		sol = new Solution(person, room, weapon);
		
		//deal player cards
		int x = 0, z = 0;		
		for (int i = 0; i < cards.size(); i += players.size()) {
			x=0;
			while (x < players.size()) {
				if (z == cards.size())
					break;
				players.get(x).addCard(cards.get(z));
				x++;
				z++;
			}
		}
	}
	
	public void setSolution(String person, String room, String weapon) {
		sol.setPerson(person);
		sol.setRoom(room);
		sol.setWeapon(weapon);
	}
	
	public void printPlayerCards() {
		for (int i = 0; i < players.size(); i++) {
			System.out.println("[ " + players.get(i).getName() + " ]");
			for (int j = 0; j < players.get(i).getMyCards().size(); j++) {
				System.out.println(players.get(i).getMyCards().get(j).getCardName());
			}
			System.out.println("---------------------------");
		}
	}
	
	public boolean checkAccusation(String person, String room, String weapon){
		if (sol.getPerson().equalsIgnoreCase(person) && sol.getRoom().equalsIgnoreCase(room) && sol.getWeapon().equalsIgnoreCase(weapon))
			return true;
		return false;
	}
	public boolean checkAccusation(ArrayList<Card> accusation){
		for(Card c : accusation){
			if(!solution.contains(c)){
				return false;
			}
		}
		return true;
	}
	
	
	public Card handleSuggestion(ArrayList<Card> suggestion,Player suggester) {
		
		
		ArrayList<Player> tempPlayers = new ArrayList<Player>(players);
		ArrayList<Card> possibleReturns = new ArrayList<Card>();
		tempPlayers.remove(suggester);
		Collections.shuffle(tempPlayers);
		for(Player p : tempPlayers){
			if(p.getMyCards().contains(suggestion.get(0))){
				possibleReturns.add(suggestion.get(0));
			}
			if(p.getMyCards().contains(suggestion.get(1))){
				possibleReturns.add(suggestion.get(1));
			}
			if(p.getMyCards().contains(suggestion.get(2))){
				possibleReturns.add(suggestion.get(2));
			}
			if(possibleReturns.size() >0){
				Collections.shuffle(possibleReturns);
				unseenCards.remove(possibleReturns.get(0));
				return possibleReturns.get(0);
			}
		}
		return null;
	}
	
	public void setPlayers(ArrayList<Player> player) {
		players = player;
	}
	
	public Player getPlayer(int i) {
		return players.get(i);
	}
	
	public ArrayList<BoardCell> getCellList() {
		return cells;
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public ArrayList<Player> getPlayerList() {
		return players;
	}
	

}







