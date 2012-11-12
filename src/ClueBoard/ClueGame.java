package ClueBoard;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.*;

public class ClueGame extends JFrame {
	//JMenuItem item;
	InfoPanel info;
	int guiCompSize = 25;
	DectiveNotePanel panel;
	public ClueGame() {
		
		Board board = new Board("config.txt", "legend.txt", "players.txt", "cards.txt",guiCompSize);
		panel = new DectiveNotePanel(board);
		JMenuBar menuBar = new JMenuBar();		
		
		setTitle("Clue");
		
		setLayout(new BorderLayout());
		MyCardsPanel myCards = new MyCardsPanel(board.getPlayer(1));
		 info = new InfoPanel(board.getPlayer(0),board);
	
		add(board, BorderLayout.CENTER);
		add(myCards, BorderLayout.EAST);
		add(info, BorderLayout.SOUTH);
		
		System.out.println(myCards.getWidth());
		setSize(new Dimension(guiCompSize*board.getNumColumns()+220 , guiCompSize*board.getNumRows()+200));

		setJMenuBar(menuBar);
		menuBar.add(createMenu(board));	
		addMouseListener(new playerMove(board));
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
		ClueGame game = new ClueGame();
		game.setVisible(true);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	private class playerMove implements MouseListener{
		public Board board;
		public playerMove(Board board){
			this.board = board;
		}
		@Override
		public void mouseClicked(MouseEvent arg0) {
			Point clickedPoint = arg0.getPoint();
			System.out.print(clickedPoint.x);
			System.out.print(",");
			System.out.print(clickedPoint.y);
			System.out.println();
			if(board.playerTurn){
				int cellWhere = board.calcIndex((clickedPoint.y-50)/guiCompSize, (clickedPoint.x-9)/guiCompSize);
				BoardCell attempToMove = board.getCellAt(cellWhere);
				//board.calcTargets(board.getPlayerList().get(0).getLocation(), board.getDiceRoll());
				if(board.getTargets().contains(attempToMove)){
					board.getPlayerList().get(0).move(board,attempToMove );
					board.getTargets().clear();
					board.playerTurn = false;
					board.repaint();
				}				
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
