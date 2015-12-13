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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import map.Layer;
import device.DeviceList;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public class DeviceParametersWindow extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	public static JTextField textField_Id;
	public static JTextField textField_My;
	public static JTextField textField_Ch;
	public static JTextField textField_NId;
	public static JTextField tf_latitude;
	public static JTextField tf_longitude;
	public static JTextField tf_radius;
	public static JTextField tf_radioRadius;
	public static JTextField tf_suRadius;
	public static JComboBox<String> gpsPathNameComboBox;
	public static JComboBox<String> scriptComboBox;
	public static JTextField tf_eMax;
	public static JTextField tf_eTx;
	public static JTextField tf_eRx;
	public static JTextField tf_eSlp;
	public static JTextField tf_eL;
	public static JTextField tf_eS;		
	public static JTextField tf_beta;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DeviceParametersWindow frame = new DeviceParametersWindow();
					frame.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DeviceParametersWindow() {
		setResizable(true);
		setMaximizable(true);
		setClosable(true);
		setIconifiable(true);
		setTitle("Node Parameters");
		setBounds(10, 10, 460, 641);

		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("Apply");
		btnNewButton.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH
				+ "loopnone-1.png"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.updateFromMap(tf_latitude.getText(),
						tf_longitude.getText(), tf_radius.getText(),
						tf_radioRadius.getText(), tf_suRadius.getText(),
						gpsPathNameComboBox.getSelectedItem()+"",
								tf_eMax.getText(),
								tf_eTx.getText(),
								tf_eRx.getText(),
								tf_eS.getText(),
								tf_beta.getText(), "" , tf_eSlp.getText(), tf_eL.getText()
					);
			}
		});
		panel_2.add(btnNewButton);

		JPanel panel_8 = new JPanel();
		getContentPane().add(panel_8, BorderLayout.CENTER);
		panel_8.setLayout(new BorderLayout(0, 0));

		JPanel panel_9 = new JPanel();
		panel_8.add(panel_9, BorderLayout.WEST);
		panel_9.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblscript = new JLabel("Script File");
		lblscript.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblscript);

		JLabel lblGpsFile = new JLabel("GPS File");
		lblGpsFile.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblGpsFile);

		JLabel labelId = new JLabel("Id");
		labelId.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(labelId);
		
		JLabel labelMy = new JLabel("My");
		labelMy.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(labelMy);
		
		JLabel labelCh = new JLabel("Ch");
		labelCh.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(labelCh);
		
		JLabel labelNId = new JLabel("Network Id");
		labelNId.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(labelNId);
		
		
		JLabel label = new JLabel("Latitude");
		label.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(label);

		JLabel label_1 = new JLabel("Longitude");
		label_1.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(label_1);

		JLabel label_2 = new JLabel("Radius");
		label_2.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(label_2);

		JLabel label_3 = new JLabel("Radio Radius");
		label_3.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(label_3);

		JLabel lblSensorUnitRadius = new JLabel("Sensor Unit Radius");
		lblSensorUnitRadius.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblSensorUnitRadius);
		
		JLabel lblNewLabel = new JLabel("Energy Max");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblNewLabel);
		
		JLabel lblEnergyTx = new JLabel("Energy Tx");
		lblEnergyTx.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblEnergyTx);
		
		JLabel lblEnergyRx = new JLabel("Energy Rx");
		lblEnergyRx.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblEnergyRx);
		
		JLabel lbl_sens = new JLabel("Sensing Energy");
		lbl_sens.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lbl_sens);
		
		JLabel lblSleepingEnergy = new JLabel("Sleeping Energy");
		lblSleepingEnergy.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblSleepingEnergy);
		
		JLabel lblListeningEnergy = new JLabel("Listening Energy");
		lblListeningEnergy.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblListeningEnergy);
		
		JLabel lblBeta = new JLabel("Beta");
		lblBeta.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblBeta);

		JPanel panel_10 = new JPanel();
		panel_8.add(panel_10, BorderLayout.CENTER);
		panel_10.setLayout(new GridLayout(0, 1, 0, 0));

		scriptComboBox = new JComboBox<String>();
		panel_10.add(scriptComboBox);

		gpsPathNameComboBox = new JComboBox<String>();
		panel_10.add(gpsPathNameComboBox);

		textField_Id = new JTextField();
		panel_10.add(textField_Id);
		textField_Id.setColumns(10);
		
		textField_My = new JTextField();
		panel_10.add(textField_My);
		textField_My.setColumns(10);
		
		textField_Ch = new JTextField();
		panel_10.add(textField_Ch);
		textField_Ch.setColumns(10);
		
		textField_NId = new JTextField();
		panel_10.add(textField_NId);
		textField_NId.setColumns(10);
		
		tf_latitude = new JTextField();
		panel_10.add(tf_latitude);
		tf_latitude.setColumns(10);

		tf_longitude = new JTextField();
		panel_10.add(tf_longitude);
		tf_longitude.setColumns(10);

		tf_radius = new JTextField();
		panel_10.add(tf_radius);
		tf_radius.setColumns(10);

		tf_radioRadius = new JTextField();
		panel_10.add(tf_radioRadius);
		tf_radioRadius.setColumns(10);

		tf_suRadius = new JTextField();
		panel_10.add(tf_suRadius);
		tf_suRadius.setColumns(10);
		
		tf_eMax = new JTextField();
		panel_10.add(tf_eMax);
		tf_eMax.setColumns(10);
		
		tf_eTx = new JTextField();
		tf_eTx.setColumns(10);
		panel_10.add(tf_eTx);
		
		tf_eRx = new JTextField();
		tf_eRx.setColumns(10);
		panel_10.add(tf_eRx);
		
		tf_eS = new JTextField();
		tf_eS.setColumns(10);
		panel_10.add(tf_eS);
		
		tf_eSlp = new JTextField();
		tf_eSlp.setColumns(10);
		panel_10.add(tf_eSlp);
		
		tf_eL = new JTextField();
		tf_eL.setColumns(10);
		panel_10.add(tf_eL);
		
		tf_beta = new JTextField();
		tf_beta.setColumns(10);
		panel_10.add(tf_beta);

		JPanel panel_11 = new JPanel();
		panel_8.add(panel_11, BorderLayout.EAST);
		panel_11.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel_1_1 = new JPanel();
		FlowLayout fl_panel_1_1 = (FlowLayout) panel_1_1.getLayout();
		fl_panel_1_1.setVgap(0);
		fl_panel_1_1.setHgap(0);
		panel_11.add(panel_1_1);

		JButton button_ = new JButton("");
		button_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileFilter ff = new FileFilter() {
					public boolean accept(File f) {
						if (f.isDirectory())
							return true;
						else if (f.getName().endsWith(".scr"))
							return true;
						else
							return false;
					}

					public String getDescription() {
						return "Script files";
					}
				};

				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(ff);
				int val = fc.showDialog(fc, "Open Script File");
				if (val == 0) {
					// textField.setText(fc.getSelectedFile().toString());
				}
			}
		});

		JButton button_1_ = new JButton("");
		button_1_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setScriptFileName(scriptComboBox.getSelectedItem()+"");
				Layer.getMapViewer().repaint();
			}
		});
		button_1_.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel_1_1.add(button_1_);
		button_.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "Ouvrir.png"));
		panel_1_1.add(button_);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_6 = (FlowLayout) panel_1.getLayout();
		flowLayout_6.setVgap(0);
		flowLayout_6.setHgap(0);
		panel_11.add(panel_1);

		JButton button = new JButton("");
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
				int val = fc.showDialog(fc, "Open GPS File");
				if (val == 0) {
					// textField.setText(fc.getSelectedFile().toString());
				}
			}
		});

		JButton button_1 = new JButton("");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setGpsFileName(
					gpsPathNameComboBox.getSelectedItem()+"");
			}
		});
		button_1.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel_1.add(button_1);
		button.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "Ouvrir.png"));
		panel_1.add(button);

		JPanel panel_Id = new JPanel();
		FlowLayout flowLayoutId = (FlowLayout) panel_Id.getLayout();
		flowLayoutId.setVgap(0);
		flowLayoutId.setHgap(0);
		panel_11.add(panel_Id);
		
		JPanel panel_My = new JPanel();
		FlowLayout flowLayoutMy = (FlowLayout) panel_My.getLayout();
		flowLayoutMy.setVgap(0);
		flowLayoutMy.setHgap(0);
		panel_11.add(panel_My);
		
		JPanel panel_Ch = new JPanel();
		FlowLayout flowLayoutCh = (FlowLayout) panel_Ch.getLayout();
		flowLayoutCh.setVgap(0);
		flowLayoutCh.setHgap(0);
		panel_11.add(panel_Ch);
		
		JPanel panel_NId = new JPanel();
		FlowLayout flowLayoutNId = (FlowLayout) panel_NId.getLayout();
		flowLayoutNId.setVgap(0);
		flowLayoutNId.setHgap(0);
		panel_11.add(panel_NId);
		
		JButton button_Id = new JButton("");
		button_Id.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DeviceList.setId(textField_Id.getText());
			}
		});
		button_Id.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel_Id.add(button_Id);
		
		
		JButton button_My = new JButton("");
		button_My.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DeviceList.setMy(textField_My.getText());
			}
		});
		button_My.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel_My.add(button_My);
		
		
		JButton button_Ch = new JButton("");
		button_Ch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DeviceList.setCh(textField_Ch.getText());
			}
		});
		button_Ch.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel_Ch.add(button_Ch);
		
		JButton button_NId = new JButton("");
		button_NId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DeviceList.setNId(textField_NId.getText());
			}
		});
		button_NId.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel_NId.add(button_NId);
		
		
		JPanel panel_12 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_12.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		panel_11.add(panel_12);
		
		JButton button_2 = new JButton("");
		button_2.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setLatitude(tf_latitude.getText());
			}
		});
		panel_12.add(button_2);

		JPanel panel_13 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_13.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setHgap(0);
		panel_11.add(panel_13);
		
		JButton button_3 = new JButton("");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setLongitude(tf_longitude.getText());
			}
		});
		button_3.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel_13.add(button_3);

		JPanel panel_14 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_14.getLayout();
		flowLayout_2.setVgap(0);
		flowLayout_2.setHgap(0);
		panel_11.add(panel_14);
		
		JButton button_4 = new JButton("");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setRadius(tf_radius.getText());
			}
		});
		button_4.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel_14.add(button_4);

		JPanel panel_15 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_15.getLayout();
		flowLayout_3.setVgap(0);
		flowLayout_3.setHgap(0);
		panel_11.add(panel_15);
		
		JButton button_5 = new JButton("");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setRadioRadius(tf_radioRadius.getText());
			}
		});
		button_5.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel_15.add(button_5);

		JPanel panel_16 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_16.getLayout();
		flowLayout_4.setVgap(0);
		flowLayout_4.setHgap(0);
		panel_11.add(panel_16);
		
		JButton button_6 = new JButton("");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setSensorUnitRadius(tf_suRadius.getText());
			}
		});
		button_6.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel_16.add(button_6);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout_7 = (FlowLayout) panel.getLayout();
		flowLayout_7.setVgap(0);
		flowLayout_7.setHgap(0);
		panel_11.add(panel);
		
		JButton button_7 = new JButton("");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setEMax(tf_eMax.getText());
			}
		});
		button_7.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel.add(button_7);

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_8 = (FlowLayout) panel_3.getLayout();
		flowLayout_8.setVgap(0);
		flowLayout_8.setHgap(0);
		panel_11.add(panel_3);
		
		JButton button_8 = new JButton("");
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setTx(tf_eTx.getText());
			}
		});
		button_8.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel_3.add(button_8);
		
		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_9 = (FlowLayout) panel_4.getLayout();
		flowLayout_9.setVgap(0);
		flowLayout_9.setHgap(0);
		panel_11.add(panel_4);
		
		JButton button_9 = new JButton("");
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setRx(tf_eRx.getText());
			}
		});
		button_9.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel_4.add(button_9);

		JPanel panel_5 = new JPanel();
		FlowLayout flowLayout_10 = (FlowLayout) panel_5.getLayout();
		flowLayout_10.setVgap(0);
		flowLayout_10.setHgap(0);
		panel_11.add(panel_5);
		
		JPanel panel_Slp = new JPanel();
		FlowLayout flowLayout_Slp = (FlowLayout) panel_Slp.getLayout();
		flowLayout_Slp.setVgap(0);
		flowLayout_Slp.setHgap(0);
		panel_11.add(panel_Slp);
		
		JPanel panel_L = new JPanel();
		FlowLayout flowLayout_L = (FlowLayout) panel_L.getLayout();
		flowLayout_L.setVgap(0);
		flowLayout_L.setHgap(0);
		panel_11.add(panel_L);
		
		JButton button_10 = new JButton("");
		button_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setSensingEnergy(tf_eS.getText());
			}
		});
		button_10.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel_5.add(button_10);
		
		
		
		
		JButton button_Slp = new JButton("");
		button_Slp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setESlp(tf_eSlp.getText());
			}
		});
		button_Slp.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel_Slp.add(button_Slp);
		
		JButton button_L = new JButton("");
		button_L.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setEL(tf_eL.getText());
			}
		});
		button_L.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel_L.add(button_L);
		
		
		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout_11 = (FlowLayout) panel_6.getLayout();
		flowLayout_11.setVgap(0);
		flowLayout_11.setHgap(0);
		panel_11.add(panel_6);
		
		JButton button_11 = new JButton("");
		button_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//DeviceList.setBeta(tf_beta.getText());
			}
		});
		button_11.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone-1.png"));
		panel_6.add(button_11);
	}

}
