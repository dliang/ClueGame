package ClueBoard;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class InfoPanel extends JPanel{
	JButton nextPlayer;

	JButton makeAccusation;
	JLabel turnLabel;
	JLabel rollLabel;
	JLabel rollValue;
	JLabel guessLabel;
	JLabel guess;
	JLabel responseLabel;
	JLabel response;
	JTextField field;
	public InfoPanel(Player player,Board board) {
		nextPlayer = new JButton("Next Player");
		nextPlayer.addActionListener(new nextTurnAction(board));
		makeAccusation = new JButton("Make an Accusation");
		turnLabel = new JLabel("Whose turn?");
		rollLabel = new JLabel("Roll");
		rollValue = new JLabel(String.valueOf(board.getDiceRoll()));
		guessLabel = new JLabel();
		guess = new JLabel ("");
		responseLabel = new JLabel();
		response = new JLabel("");
		
		field = new JTextField(board.getPlayerList().get(0).getName());
		
		JPanel playerPanel = new JPanel();
		JPanel diePanel = new JPanel();
		JPanel guessPanel = new JPanel();
		JPanel resultPanel = new JPanel();
		
		playerPanel.add(turnLabel);
		playerPanel.add(field);
		
		diePanel.add(rollLabel);
		diePanel.add(rollValue);
		
		guessPanel.add(guessLabel);
		guessPanel.add(guess);
		
		resultPanel.add(responseLabel);
		resultPanel.add(response);
		
		diePanel.setBorder(BorderFactory.createTitledBorder("Die"));
		guessPanel.setBorder(BorderFactory.createTitledBorder("Guess"));
		resultPanel.setBorder(BorderFactory.createTitledBorder("Guess Result"));
		
		setLayout(new GridLayout(2, 3));
		add(playerPanel);
		add(nextPlayer);
		add(makeAccusation);
		add(diePanel);
		add(guessPanel);
		add(resultPanel);
		
	}
	private class nextTurnAction implements ActionListener{
		public Board board;
		public nextTurnAction(Board board){
			this.board = board;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(!board.playerTurn){
			board.rollDice();
			board.nextTurn();
			if(board.playerTurn){
				if(board.getPlayerList().get(0).enteredRoom){
					//create suggestion panel here
				}
			}
			field.setText(board.getPlayerList().get(board.getCurTurn()).getName());
			rollValue.setText(String.valueOf(board.getDiceRoll()));
			if(board.getReturnCard() != null){
				response.setText(board.getReturnCard().getCardName());
			}else{
				response.setText("no new clue");
			}
			String sugg = "";
			for(Card c : board.getSuggestion()){
				sugg = sugg + c.getCardName()+", ";
			}
			guess.setText(sugg);
			}else{
				JOptionPane.showMessageDialog(null, "either make an accusation or move","Finish your turn",JOptionPane.PLAIN_MESSAGE);
			}
			//
		}		
	}
	
	
}
