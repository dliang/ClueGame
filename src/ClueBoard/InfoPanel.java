package ClueBoard;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class InfoPanel extends JPanel{

	public InfoPanel(Player player,Board board) {
		JButton nextPlayer = new JButton("Next Player");
		nextPlayer.addActionListener(new nextTurnAction(board));
		JButton makeAccusation = new JButton("Make and Accusation");
		JLabel turnLabel = new JLabel("Whose turn?");
		JLabel rollLabel = new JLabel("Roll");
		JLabel rollValue = new JLabel(String.valueOf(board.getDiceRoll()));
		JLabel guessLabel = new JLabel("Guess");
		JLabel guess = new JLabel ("o");
		JLabel responseLabel = new JLabel("Response");
		JLabel response = new JLabel("s");
		
		JTextField field = new JTextField("player 1");
		
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
		
	}private class nextTurnAction implements ActionListener{
		public Board board;
		public nextTurnAction(Board board){
			this.board = board;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			board.nextTurn();			
		}		
	}
	
}
