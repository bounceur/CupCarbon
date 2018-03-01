package cupcarbon;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class MonPanneau extends JComponent {

	private static final long serialVersionUID = 1L;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.RED);
		g.fillOval(0, 0, 500, 500);
	}
	
}
