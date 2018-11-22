 

import java.awt.Dimension;

import javax.swing.JFrame;

public class ChessGUI {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Chess Game -- AI Design by Dr. Roger Ferguson");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ChessPanel panel = new ChessPanel();
		frame.getContentPane().add(panel);
		
		frame.setResizable(true);
		frame.setPreferredSize(new Dimension(800, 700));
		frame.pack();
		frame.setVisible(true);
	}
}

