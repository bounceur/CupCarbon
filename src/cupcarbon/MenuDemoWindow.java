package cupcarbon;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import map.Layer;
import map.WorldMap;
import device.MarkerList;

public class MenuDemoWindow extends JInternalFrame {

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
					MenuDemoWindow frame = new MenuDemoWindow();
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
	public MenuDemoWindow() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBackground(Color.WHITE);
		setClosable(true);
		setTitle("Demo");
		setBounds(100, 100, 335, 359);
		getContentPane().setLayout(new GridLayout(3, 3, 0, 0));
		
		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.addNodeInMap('1');
			}
		});
		button.setIcon(new ImageIcon(CupCarbonParameters.IMGPATHDEMO + "sensor.jpg"));
		button.setBackground(Color.WHITE);
		getContentPane().add(button);
		
		JButton button_2 = new JButton("");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.addNodeInMap('8');
			}
		});
		button_2.setIcon(new ImageIcon(CupCarbonParameters.IMGPATHDEMO + "markers.jpg"));
		button_2.setBackground(Color.WHITE);
		getContentPane().add(button_2);
		
		JButton button_6 = new JButton("");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Layer.initClick();
			}
		});
		button_6.setIcon(new ImageIcon(CupCarbonParameters.IMGPATHDEMO + "world.jpg"));
		button_6.setBackground(Color.WHITE);
		getContentPane().add(button_6);
		
		JButton button_4 = new JButton("");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.simulate();
			}
		});
		button_4.setIcon(new ImageIcon(CupCarbonParameters.IMGPATHDEMO + "simul.jpg"));
		button_4.setBackground(Color.WHITE);
		getContentPane().add(button_4);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.addNodeInMap('5');
			}
		});
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setIcon(new ImageIcon(CupCarbonParameters.IMGPATHDEMO + "basestation.jpg"));
		getContentPane().add(btnNewButton);
		
		JButton button_5 = new JButton("");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//for(Marker marker : MarkerList.getMarkers()) {
					//marker.insertMarker();
					MarkerList.insertMarkers();
				//}
			}
		});
		button_5.setIcon(new ImageIcon(CupCarbonParameters.IMGPATHDEMO + "insert.jpg"));
		button_5.setBackground(Color.WHITE);
		getContentPane().add(button_5);
		
		JButton button_1 = new JButton("");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MarkerList.generateGpxFile();
			}
		});
		button_1.setIcon(new ImageIcon(CupCarbonParameters.IMGPATHDEMO + "move.jpg"));
		button_1.setBackground(Color.WHITE);
		getContentPane().add(button_1);
		
		JButton button_3 = new JButton("");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.simulateAll();
			}
		});
		button_3.setIcon(new ImageIcon(CupCarbonParameters.IMGPATHDEMO + "simulall.jpg"));
		button_3.setBackground(Color.WHITE);
		getContentPane().add(button_3);
		
		JButton button_7 = new JButton("");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.addNodeInMap('3');
			}
		});
		button_7.setIcon(new ImageIcon(CupCarbonParameters.IMGPATHDEMO + "flying.jpg"));
		button_7.setBackground(Color.WHITE);
		getContentPane().add(button_7);

	}

}
