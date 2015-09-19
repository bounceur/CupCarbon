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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

import project.Project;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */

public class ScriptWindow extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txtFileName;
	public static JComboBox<String> txtLoadFileName;
	private JTextArea txtArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScriptWindow frame = new ScriptWindow();
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
	public ScriptWindow() {
		super("Script");

		this.setName("Script");
		setIconifiable(true);
		setClosable(true);
		setResizable(true);
		setBounds(10, 10, 409, 550);

		getContentPane().setLayout(new BorderLayout());

		Border loweredetched = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);

		JButton button_1 = new JButton("");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtFileName.setText(Project.getGpsFileExtension(txtFileName
						.getText()));
			}
		});
		button_1.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));

		JButton button = new JButton("");

		button.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "Ouvrir.png"));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				FileFilter ff = new FileFilter() {
					public boolean accept(File f) {
						if (f.isDirectory())
							return true;
						else if (f.getName().endsWith("."))
							return true;
						else
							return false;
					}

					public String getDescription() {
						return "Scripts";
					}
				};

				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(ff);
				int val = fc.showDialog(fc, "Save");
				if (val == 0)
					txtFileName.setText(fc.getSelectedFile().toString());
			}
		});

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(105, 105, 105));
		panel_1.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		txtArea = new JTextArea();
		txtArea.setLineWrap(true);
		panel_1.add(txtArea, BorderLayout.CENTER);
		txtArea.setFont(new Font("Courier New", Font.BOLD, 14));
		txtArea.setForeground(Color.BLACK);
		txtArea.setBackground(Color.WHITE);//new Color(0, 51, 102));
		txtArea.setText("psend 1000\ndelay 500\npsend 1500\ndelay 500");
		txtArea.setBorder(new LineBorder(new Color(0, 0, 0)));

		JScrollPane scrollPane = new JScrollPane(txtArea);
		panel_1.add(scrollPane, BorderLayout.CENTER);

		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));

		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		JLabel label = new JLabel("<html>&nbsp;&nbsp;Laod file<html>");
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_4.add(label, BorderLayout.WEST);

		txtLoadFileName = new JComboBox<String>();
		txtLoadFileName.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_4.add(txtLoadFileName);

		txtLoadFileName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtLoadFileName.getSelectedIndex() > 0) {
					txtFileName.setText(txtLoadFileName.getSelectedItem()
							.toString());
					txtArea.setText("");
					try {
						FileInputStream in = new FileInputStream(new File(
								Project.getScriptFileFromName(txtLoadFileName
										.getSelectedItem().toString())));
						byte[] bytes = new byte[in.available()];
						in.read(bytes);
						txtArea.setText(new String(bytes));
						in.close();
					} catch (Exception e1) {}
				} else {
					txtArea.setText("");
					txtFileName.setText("");
				}
			}
		});

		JPanel panel_5 = new JPanel();
		panel_3.add(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));
		JLabel label_1 = new JLabel("  File Name  ");
		label_1.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_5.add(label_1, BorderLayout.WEST);
		txtFileName = new JTextField();
		txtFileName.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_5.add(txtFileName);
		txtFileName.setBorder(loweredetched);

		JButton save = new JButton(" Save ");
		save.setFont(new Font("Arial", Font.PLAIN, 12));
		panel.add(save, BorderLayout.SOUTH);
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent action) {
				if(!txtFileName.getText().equals("")) {
					try {
						PrintStream ps;
						ps = new PrintStream(new FileOutputStream(Project
								.getScriptFileFromName(Project
										.getScriptFileExtension(txtFileName
												.getText()))));
						ps.print(txtArea.getText());
						ps.close();
						txtArea.setText("");
						txtFileName.setText("");
	
						File scriptFiles = new File(Project.getProjectScriptPath());
						String[] c = scriptFiles.list();
						txtLoadFileName.removeAllItems();
						txtLoadFileName.addItem("New scenario ...");
						for (int i = 0; i < c.length; i++) {
							txtLoadFileName.addItem(c[i]);
						}
	
						JOptionPane.showMessageDialog(null, "File saved !");
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		save.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "Enregistrer.png"));

		setVisible(false);
	}

}
