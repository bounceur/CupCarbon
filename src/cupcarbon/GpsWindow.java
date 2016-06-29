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
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;

import markers.MarkerList;
import project.Project;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public class GpsWindow extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txtFileName;
	private JTextField txtTitle;
	private JTextField txtFrom;
	private JTextField txtTo;
	private static GpsWindow frame;
	private JTextField delayTextField;
	private JCheckBox cbLoop;
	private JTextField nLoopTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new GpsWindow();
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
	public GpsWindow() {
		super("GPS File Name");
		setRootPaneCheckingEnabled(false);

		this.setName("GPS Coords");
		setIconifiable(true);
		setClosable(true);
		setBounds(6, 95, 447, 308);
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BorderLayout(0, 0));

		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4, BorderLayout.WEST);

		JLabel lblFileName = new JLabel("File Name");
		lblFileName.setFont(new Font("Arial", Font.PLAIN, 12));
		lblFileName.setDisplayedMnemonic('a');
		panel_4.add(lblFileName);

		JPanel panel_7 = new JPanel();
		panel_2.add(panel_7, BorderLayout.CENTER);
		panel_7.setLayout(new BorderLayout(0, 0));

		txtFileName = new JTextField();
		txtFileName.setText("route1.gps");
		txtFileName.setFont(new Font("Arial", Font.PLAIN, 12));
		txtFileName.setColumns(15);
		panel_7.add(txtFileName);

		JPanel panel_8 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_8.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		panel_7.add(panel_8, BorderLayout.EAST);

		JButton button_1 = new JButton("");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtFileName.setText(Project.getGpsFileExtension(txtFileName
						.getText()));
			}
		});
		button_1.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_8.add(button_1);

		JButton button = new JButton("");
		panel_8.add(button);
		button.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "Ouvrir.png"));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				FileFilter ff = new FileFilter() {
					public boolean accept(File f) {
						if (f.isDirectory())
							return true;
						else if (f.getName().endsWith(".gps"))
							return true;
						else
							return false;
					}

					public String getDescription() {
						return "GPS files";
					}
				};

				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(ff);
				int val = fc.showDialog(fc, "Save");
				if (val == 0)
					txtFileName.setText(fc.getSelectedFile().toString());
			}
		});

		JSeparator separator = new JSeparator();
		panel_2.add(separator, BorderLayout.SOUTH);

		JSeparator separator_1 = new JSeparator();
		panel_2.add(separator_1, BorderLayout.NORTH);

		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));

		JSeparator separator_2 = new JSeparator();
		panel_3.add(separator_2, BorderLayout.NORTH);

		JPanel panel_9 = new JPanel();
		panel_3.add(panel_9, BorderLayout.CENTER);

		JButton btnLoad = new JButton("Load");
		btnLoad.setFont(new Font("Arial", Font.PLAIN, 12));
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(Project.getProjectGpsPath()+File.separator+txtFileName.getText());
				MarkerList.open(Project.getProjectGpsPath()+File.separator+txtFileName.getText());
			}
		});
		btnLoad.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_9.add(btnLoad);

		JButton btnNewButton = new JButton("Save");
		btnNewButton.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_9.add(btnNewButton);
		btnNewButton.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "stylo.png"));

		JPanel panel_6 = new JPanel();
		panel.add(panel_6, BorderLayout.CENTER);
		panel_6.setLayout(new GridLayout(5, 1, 2, 2));

		JPanel panel_10 = new JPanel();
		panel_6.add(panel_10);
		panel_10.setLayout(new BoxLayout(panel_10, BoxLayout.X_AXIS));

		JLabel lblNewLabel = new JLabel("   Title");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_10.add(lblNewLabel);

		txtTitle = new JTextField();
		txtTitle.setText("Route name");
		txtTitle.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_10.add(txtTitle);
		txtTitle.setColumns(20);

		JPanel panel_11 = new JPanel();
		panel_6.add(panel_11);
		panel_11.setLayout(new BoxLayout(panel_11, BoxLayout.X_AXIS));

		JLabel lblNewLabel_1 = new JLabel("   From");
		lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_11.add(lblNewLabel_1);

		txtFrom = new JTextField();
		txtFrom.setText("City one");
		txtFrom.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_11.add(txtFrom);
		txtFrom.setColumns(20);

		JPanel panel_12 = new JPanel();
		panel_6.add(panel_12);
		panel_12.setLayout(new BoxLayout(panel_12, BoxLayout.X_AXIS));

		JLabel lblNewLabel_2 = new JLabel("   To");
		lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_12.add(lblNewLabel_2);

		txtTo = new JTextField();
		txtTo.setText("City two");
		txtTo.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_12.add(txtTo);
		txtTo.setColumns(20);

		JPanel panel_13 = new JPanel();
		panel_13.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_6.add(panel_13);
		panel_13.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(3, 3, 3, 3));
		panel_13.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));

		cbLoop = new JCheckBox("Loop after");
		panel_1.add(cbLoop);
		cbLoop.setFont(new Font("Arial", Font.PLAIN, 12));

		delayTextField = new JTextField();
		panel_1.add(delayTextField);
		delayTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		delayTextField.setText("60");
		delayTextField.setColumns(10);

		JLabel lblSeconds = new JLabel("Seconds");
		panel_1.add(lblSeconds);
		lblSeconds.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JPanel panel_5 = new JPanel();
		panel_6.add(panel_5);
		panel_5.setLayout(new GridLayout(1, 2, 0, 0));
		
		JPanel panel_14 = new JPanel();
		panel_5.add(panel_14);
		panel_14.setLayout(new BoxLayout(panel_14, BoxLayout.X_AXIS));
		
		JLabel lblNumberOfLoops = new JLabel("Number of loops");
		lblNumberOfLoops.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_14.add(lblNumberOfLoops);
		
		nLoopTextField = new JTextField();
		nLoopTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		nLoopTextField.setText("10");
		panel_14.add(nLoopTextField);
		nLoopTextField.setColumns(10);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtFileName.setText(Project.getGpsFileExtension(txtFileName
						.getText()));
				MarkerList.saveGpsCoords(txtFileName.getText(),
						txtTitle.getText(), txtFrom.getText(), txtTo.getText(),
						cbLoop.isSelected(), Integer.parseInt(delayTextField.getText()),
						Integer.parseInt(nLoopTextField.getText()));
			}
		});
		setVisible(false);
	}

}
