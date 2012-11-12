package ClueBoard;

public class HumanPlayer extends Player{
	Board board;
	public HumanPlayer(String name, String color, int location,Board board) {
		super(name, color, location);
		this.board = board;
	
		// TODO Auto-generated constructor stub
	}

	@Override
	public void takeTurn() {
		board.calcTargets(location, board.getDiceRoll());
		board.playerTurn = true;
		board.repaint();
		// TODO Auto-generated method stub
		
	}

//	public Card disproveSuggestion(Card person, Card room, Card weapon) {
//		return new Card("", Card.CardType.WEAPON);
//	}
	
}
