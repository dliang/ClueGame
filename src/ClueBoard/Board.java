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
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ClueBoard.RoomCell.DoorDirection;


public class Board extends JPanel{
	private ArrayList<BoardCell> cells;
	private Map<Character, String> rooms;
	private int numRows;
	private int numColumns;
	private int startPosition=-1;
	private int GUI_COMP_SIZE = 30;

	
	private Map<Integer, LinkedList<Integer>> adjMtx = new HashMap<Integer, LinkedList<Integer>>();
	private Stack<Integer> path = new Stack<Integer>();
	private Set<Integer> targets = new HashSet<Integer>();
	private Stack<Integer> doorsFound = new Stack<Integer>();
	private Stack<Integer> removed = new Stack<Integer>();
	
	//*********************************************************
	private Solution sol;
	private ArrayList<Player> players;
	private ArrayList<Card> cards;
	private ArrayList<Card> solution = new ArrayList<Card>();

	public Board(String config, String legend, String people, String deck){
		try{
			loadConfigFiles(config,legend,people,deck);
		}catch(Exception e){
			System.out.println(e.getMessage());			
		}
		setBackground (Color.black); 
		
		String splash = "You are playing as " + players.get(0).getName() + " press next player to begin play";
		JOptionPane.showMessageDialog(null, splash,"Welcome to Clue",JOptionPane.PLAIN_MESSAGE);
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
			if (x == 0)
				players.add(new HumanPlayer(line[0], line[1], Integer.parseInt(line[2])));
			else
				players.add(new ComputerPlayer(line[0], line[1], Integer.parseInt(line[2]), getCellAt(Integer.parseInt(line[2]))));
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
	
	//calculate adjacency list for a specific cell
	public void calcAdjacencies(int row, int col){
	
		LinkedList<Integer> list = new LinkedList<Integer>();
		adjMtx.put(calcIndex(row,col), list);
		for(int i = 0; i <= 1; i++){
			double testRow = row + java.lang.Math.pow(-1.0, i);
			double testIndex = calcIndex(testRow, col);
			if(testIndex != -1 && testIndex != startPosition && !path.contains(testIndex) && (cells.get((int) testIndex).isRoom() != true || cells.get((int) testIndex).isDoorway() == true)){
				adjMtx.get(calcIndex(row,col)).add((int) testIndex);
			}
			
		}
		for(int j=0; j <=1 ; j++){
			double testCol = col+ java.lang.Math.pow(-1.0, j);
			if(testCol > col && col == numColumns - 1){
				continue;
			}else if(testCol < col && col == 0){
				continue;
			}
			double testIndex = calcIndex(row, testCol);
			if((testIndex != -1 && testIndex != startPosition && !path.contains(testIndex) && (cells.get((int) testIndex).isRoom() != true) || cells.get((int) testIndex).isDoorway() == true)){
		
				adjMtx.get(calcIndex(row,col)).add((int) testIndex);
			}
		}
		
		for(int r=0; r<list.size(); r++){
			if(cells.get(calcIndex(row,col)).isDoorway()){
				if(( (RoomCell) cells.get(calcIndex (row,col) )).getDoorDirection() == DoorDirection.UP){
					if( getRowCol(list.get(r))[0] != col){
						
						list.remove(list.get(r));
						r--;
					}
					
				}
				if(( (RoomCell) cells.get(calcIndex (row,col) )).getDoorDirection() == DoorDirection.LEFT){
					if( getRowCol(list.get(r))[0] != col-1){
						
						list.remove(list.get(r));
						r--;
					}
					
				}
				if(( (RoomCell) cells.get(calcIndex (row,col) )).getDoorDirection() == DoorDirection.RIGHT){
					if( getRowCol(list.get(r))[0] != col-1){
				
						list.remove(list.get(r));
						r--;
					}
					
				}
				if(( (RoomCell) cells.get(calcIndex (row,col) )).getDoorDirection() == DoorDirection.DOWN){
					if( getRowCol(list.get(r))[0] != col){
						
						list.remove(list.get(r));
						r--;
					}
					
				}
			}
		}
	}
	
	//***************************
	public void printTargets() {
		Set<BoardCell> test = new HashSet<BoardCell>();
		calcTargets(calcIndex(6, 0), 2);
		
		for (Integer key : targets)
			System.out.println(key);
	}

	//calculate targets for given cell index and number of steps.
	public void calcTargets(int position, int steps){

		
		path.push(position);
		
		//Record first position to test against starting on a door 
		if(startPosition==-1){
			startPosition=position;
		}
		
		//Calc adjacencies first so that we can getAdjecencies
		calcAdjacencies(getRowCol(position)[1], getRowCol(position)[0]);
	
		
	
		if (steps == 0) {
			targets.add(position);
			path.pop();
			return;
		}	
		
		
		for (int i : adjMtx.get(position)) {
			//If a doorway is found add it to targets
			if (cells.get(i).isDoorway() && i!= startPosition) {
				targets.add(i);
			}
			if (!cells.get(i).isRoom() && i != position && !path.contains(i) && i != path.elementAt(0)) {
				calcTargets(i, steps - 1);
			}
		}
		
		path.pop();
			
	}
	
	//clears targets
	public void clearTargets(){
		targets.clear();
	}
	
	public HashSet<BoardCell> getTargets(){
		startPosition=-1;
		Set<BoardCell> temp = new HashSet<BoardCell>();
		//path.clear();
		

		
		startPosition=-1;
		for(int i:doorsFound){
			targets.add(i);
		}
		
		removed.clear();
		doorsFound.clear();
		for(int i:targets){
			if(!cells.get(i).isRoom() || cells.get(i).isDoorway()){
				temp.add(cells.get(i));
			}
		}
		
		
		path.clear();
		targets.clear();
		return (HashSet<BoardCell>) temp;
	}
	
	//gets the adjacency list for a cell
	public LinkedList<Integer> getAdjList(int location){
		//Check if tile is in a room
		if(!cells.get(location).isDoorway() && cells.get(location).isRoom()){
			LinkedList<Integer> temp = new LinkedList<Integer>();
			return temp;
		}
		calcAdjacencies(getRowCol(location)[1],getRowCol(location)[0]);
		return adjMtx.get(location);
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
		
		
		//put answer into solution, remove solution cards from deck 
		int a = 0, b = 0, c = 0;
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).getCardtype() == Card.CardType.PERSON && a == 0) {
				System.out.println(cards.get(i).getCardName());
				solution.add(cards.get(i));
				cards.remove(i);
				a++;
			} else if (cards.get(i).getCardtype() == Card.CardType.ROOM && b == 0) {
				System.out.println(cards.get(i).getCardName());
				solution.add(cards.get(i));
				cards.remove(i);
				b++;
			} else if (cards.get(i).getCardtype() == Card.CardType.WEAPON && c == 0) {
				System.out.println(cards.get(i).getCardName());
				solution.add(cards.get(i));
				cards.remove(i);
				c++;
			} 
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
	
	public void handleSuggestion(String person, String room, String weapon) {
		
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
	
/*	public static void main(String[] args) throws IOException, BadConfigFormatException {
		Board board = new Board("config.txt", "legend.txt", "players.txt", "cards.txt");

		board.deal();
	//	board.printPlayerCards();
		board.printTargets();
		
		ComputerPlayer test_player = (ComputerPlayer) board.getPlayer(1);
		board.calcTargets(board.calcIndex(6, 0), 2);
		int loc_5_1Tot = 0;
		int loc_4_0Tot = 0;
		int loc_6_2Tot = 0;
		// Run the test 100 times
		for (int i=0; i<100; i++) {
			BoardCell selected = (BoardCell) test_player.pickLocation(board.getTargets(), board.getCellList());

			if (selected == board.getCellAt(board.calcIndex(4, 0)))
				loc_4_0Tot++;
			else if (selected == board.getCellAt(board.calcIndex(5, 1)))
				loc_5_1Tot++;
			else if	(selected == board.getCellAt(board.calcIndex(6, 2)))
				loc_6_2Tot++;
			else
				fail("Invalid target selected");
		}
		
		System.out.println("o, 3: " + loc_5_1Tot);
		System.out.println("4, 0: " + loc_4_0Tot);
		System.out.println("6, 2: " + loc_6_2Tot);
	}*/
}







