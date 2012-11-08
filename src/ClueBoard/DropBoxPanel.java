package ClueBoard;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;

public class DropBoxPanel extends JPanel {
	
	private JComboBox personBox;
	private JComboBox roomBox;
	private JComboBox weaponBox;
	
	public DropBoxPanel(Board board) {
		personBox = new JComboBox();
		roomBox = new JComboBox();
		weaponBox = new JComboBox();
		
		JPanel peoplePanel = new JPanel();
		JPanel roomPanel = new JPanel();
		JPanel weaponPanel = new JPanel();
		
		for (int i = 0; i < board.getCards().size(); i++) {
			if (board.getCards().get(i).getCardtype() == Card.CardType.PERSON)
				personBox.addItem(board.getCards().get(i).getCardName());
			if (board.getCards().get(i).getCardtype() == Card.CardType.ROOM)
				roomBox.addItem(board.getCards().get(i).getCardName());
			if (board.getCards().get(i).getCardtype() == Card.CardType.WEAPON)
				weaponBox.addItem(board.getCards().get(i).getCardName());
		}
		personBox.addItem("Unsure");
		roomBox.addItem("Unsure");
		weaponBox.addItem("Unsure");
		
		peoplePanel.add(personBox);
		roomPanel.add(roomBox);
		weaponPanel.add(weaponBox);
		
		peoplePanel.setBorder(BorderFactory.createTitledBorder("People Guess"));
		roomPanel.setBorder(BorderFactory.createTitledBorder("Room Guess"));
		weaponPanel.setBorder(BorderFactory.createTitledBorder("Weapon Guess"));
		
		setLayout(new BoxLayout(this, 1));
		add(peoplePanel);
		add(roomPanel);
		add(weaponPanel);
		
	}

}
