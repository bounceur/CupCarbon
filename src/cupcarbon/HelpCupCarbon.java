/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013 Ahcene Bounceur
 * ----------------------------------------------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------*/

package cupcarbon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @version 1.0
 */
public class HelpCupCarbon extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HelpCupCarbon frame = new HelpCupCarbon();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HelpCupCarbon() {
		setClosable(true);
		setBounds(100, 100, 532, 520);
		JTextArea txtrAMenu = new JTextArea();
		txtrAMenu.setBackground(Color.BLACK);
		txtrAMenu.setForeground(Color.ORANGE);
		JScrollPane scrollPane = new JScrollPane(txtrAMenu);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		
		//getContentPane().add(txtrAMenu, BorderLayout.NORTH);
		txtrAMenu.setFont(new Font("Courier New", Font.BOLD, 12));
		txtrAMenu.setEditable(false);
		txtrAMenu.setColumns(30);
		txtrAMenu.setWrapStyleWord(true);
		txtrAMenu.setLineWrap(true);
		txtrAMenu.setRows(10);
		txtrAMenu
				.setText("Please, visit our web page www.cupcarbon.com");

	}

}
