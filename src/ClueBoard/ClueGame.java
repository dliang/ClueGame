package ClueBoard;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class ClueGame extends JFrame {
	
	public ClueGame() {		
		JMenuBar menuBar = new JMenuBar();		
		setSize(new Dimension(1000, 1000));
		setTitle("Clue");
		
		
		Board board = new Board("config.txt", "legend.txt", "players.txt", "cards.txt");
		//getContentPane().add(board);		
		board.deal();
		MyCardsPanel myCards = new MyCardsPanel(board.getPlayer(1));
		InfoPanel info = new InfoPanel(board.getPlayer(1));
		
		setLayout(new BorderLayout());
		add(board, BorderLayout.CENTER);
		add(myCards, BorderLayout.EAST);
		add(info, BorderLayout.PAGE_END);
		
		setJMenuBar(menuBar);
		menuBar.add(createMenu(board));
//		panel.setVisible(true);
//		panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	private JMenu createMenu(Board board) {
		JMenu menu = new JMenu("File");
		menu.add(createDetectivePanelItem(board));
		menu.add(createFileExitItem());
		return menu;
	}
	
	private JMenuItem createDetectivePanelItem(final Board board) {
		JMenuItem item = new JMenuItem("Show Dective Panel");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				DectiveNotePanel panel = new DectiveNotePanel(board);
				panel.setVisible(true);
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}
	private JMenuItem createFileExitItem() {
		  JMenuItem item = new JMenuItem("Exit Program");
		  class MenuItemListener implements ActionListener {
		    public void actionPerformed(ActionEvent e)
		    {
		       System.exit(0);
		    }
		  }
		  item.addActionListener(new MenuItemListener());
		  return item;
		}
	
	public static void main(String[] args) throws IOException, BadConfigFormatException {
	//  Board board = new Board("config.txt", "legend.txt", "players.txt", "cards.txt");
	//	board.loadConfigFiles("config.txt", "legend.txt", "players.txt", "cards.txt");
		
		
		ClueGame game = new ClueGame();
		game.setVisible(true);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		game.getContentPane().add(board);
//		game.setVisible(true);
//		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		
//		board.deal();
	}	
}
