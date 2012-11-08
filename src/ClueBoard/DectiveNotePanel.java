package ClueBoard;
import java.awt.*;

import javax.swing.*;

public class DectiveNotePanel extends JFrame {
	
	
	public DectiveNotePanel(Board board) {
		setSize(new Dimension(600, 600));
		setTitle("Dective Panel");
	//	setLayout(new BorderLayout());
		
		
		CheckBoxPanel checkbox = new CheckBoxPanel(board);
		DropBoxPanel dropbox = new DropBoxPanel(board);
		
		setLayout(new GridLayout(0, 2));
		add(checkbox);
		add(dropbox);
	}

}
