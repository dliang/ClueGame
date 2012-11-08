package ClueBoard;

import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.*;

public class CheckBoxPanel extends JPanel {
	
	private ArrayList<JCheckBox> checkbox = new ArrayList<JCheckBox>();
	
	public CheckBoxPanel(Board board) {
		
		for (Card i : board.getCards()) {
			checkbox.add(new JCheckBox(i.getCardName(), false));
		}
		
		// Determining which panel to add the cards to:
		//People Panel
		JPanel peoplePanel = new JPanel();
		JPanel roomPanel = new JPanel();
		JPanel weaponPanel = new JPanel();
		
		for (int i = 0; i < board.getCards().size(); i++) {
			if (board.getCards().get(i).getCardtype() == Card.CardType.PERSON)
				peoplePanel.add(checkbox.get(i));
			if (board.getCards().get(i).getCardtype() == Card.CardType.ROOM)
				roomPanel.add(checkbox.get(i));
			if (board.getCards().get(i).getCardtype() == Card.CardType.WEAPON)
				weaponPanel.add(checkbox.get(i));
		}
		peoplePanel.setBorder(BorderFactory.createTitledBorder("People"));
		roomPanel.setBorder(BorderFactory.createTitledBorder("Rooms"));
		weaponPanel.setBorder(BorderFactory.createTitledBorder("Weapons"));
		
		setLayout(new BoxLayout(this, 1));
		//setLayout(new GridLayout(3, 1));
		add(peoplePanel);
		add(roomPanel);
		add(weaponPanel);		
	}
}
