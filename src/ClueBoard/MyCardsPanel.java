package ClueBoard;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;

public class MyCardsPanel extends JPanel{
	
	public MyCardsPanel(Player player) {
		JLabel label = new JLabel("My Cards");
		ArrayList<JLabel> cards = new ArrayList<JLabel>();
		
		JPanel peoplePanel = new JPanel();
		JPanel roomPanel = new JPanel();
		JPanel weaponPanel = new JPanel();
		peoplePanel.setLayout(new GridLayout(3, 1));
		roomPanel.setLayout(new GridLayout(3, 1));
		weaponPanel.setLayout(new GridLayout(3 , 1));
		for (Card c : player.getMyCards()) {
			if (c.getCardtype() == Card.CardType.PERSON)
				peoplePanel.add(new JTextField(c.getCardName()));
			if (c.getCardtype() == Card.CardType.ROOM)
				roomPanel.add(new JTextField(c.getCardName()));
			if (c.getCardtype() == Card.CardType.WEAPON)
				weaponPanel.add(new JTextField(c.getCardName()));
		}
		
		peoplePanel.setBorder(BorderFactory.createTitledBorder("People"));
		roomPanel.setBorder(BorderFactory.createTitledBorder("Rooms"));
		weaponPanel.setBorder(BorderFactory.createTitledBorder("Weapons"));
		
		setLayout(new GridLayout(9, 1));
		add(label);
		add(peoplePanel);
		add(roomPanel);
		add(weaponPanel);
		
	}


}
