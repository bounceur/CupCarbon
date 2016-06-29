/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2016 Ahcene Bounceur
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

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import device.DeviceList;
import map.MapLayer;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @author Nabil Kadjouh
 * @version 1.0
 */
public class DeviceParametersWindow extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	public static JTextField textField_Id;
	public static JTextField tf_longitude;
	public static JTextField tf_latitude;
	public static JTextField tf_elevation;
	public static JTextField tf_radius;
	public static JTextField tf_suRadius;

	// ------------------------------------------------------------------------------------------KADJOUH
	public static JComboBox<String> batteryModelComboBox;
	// ------------------------------------------------------------------------------------------KADJOUH

	public static JComboBox<String> gpsPathNameComboBox;
	public static JComboBox<String> scriptComboBox;
	public static JTextField tf_eMax;
	public static JTextField tf_eS;
	public static JComboBox<String> cb_uartDataRate;
	private JTextField tf_drift;

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
		setBounds(10, 10, 460, 389);

		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.SOUTH);

		JPanel panel_8 = new JPanel();
		getContentPane().add(panel_8, BorderLayout.CENTER);
		panel_8.setLayout(new BorderLayout(0, 0));

		JPanel panel_9 = new JPanel();
		panel_8.add(panel_9, BorderLayout.WEST);
		panel_9.setLayout(new GridLayout(0, 1, 0, 0));

		// ------------------------------------------------------------------------------------------KADJOUH
		JLabel lblBatteryModel = new JLabel("Battery model file");
		lblBatteryModel.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblBatteryModel);
		// ------------------------------------------------------------------------------------------KADJOUH

		JLabel lblscript = new JLabel("Script File");
		lblscript.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblscript);

		JLabel lblGpsFile = new JLabel("GPS File");
		lblGpsFile.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblGpsFile);

		JLabel labelId = new JLabel("Id");
		labelId.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(labelId);

		JLabel lblLongitude = new JLabel("Longitude");
		lblLongitude.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblLongitude);

		JLabel label = new JLabel("Latitude");
		label.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(label);

		JLabel lblElevation = new JLabel("Elevation");
		lblElevation.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblElevation);

		JLabel label_2 = new JLabel("Radius");
		label_2.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(label_2);

		JLabel lblSensorUnitRadius = new JLabel("Sensor Unit Radius");
		lblSensorUnitRadius.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblSensorUnitRadius);

		JLabel lblNewLabel = new JLabel("Energy Max");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblNewLabel);

		JLabel lbl_sens = new JLabel("Sensing Consumption   ");
		lbl_sens.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lbl_sens);

		JLabel lblUartRate = new JLabel("UART Data Rate (baud)");
		lblUartRate.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblUartRate);

		JLabel lblCpuDrifftsigma = new JLabel("CPU Drifft (Sigma)");
		lblCpuDrifftsigma.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblCpuDrifftsigma);

		JPanel panel_10 = new JPanel();
		panel_8.add(panel_10, BorderLayout.CENTER);
		panel_10.setLayout(new GridLayout(0, 1, 0, 0));

		// ------------------------------------------------------------------------------------------KADJOUH
		batteryModelComboBox = new JComboBox<String>();
		panel_10.add(batteryModelComboBox);
		// ------------------------------------------------------------------------------------------KADJOUH

		scriptComboBox = new JComboBox<String>();
		panel_10.add(scriptComboBox);

		gpsPathNameComboBox = new JComboBox<String>();
		panel_10.add(gpsPathNameComboBox);

		textField_Id = new JTextField();
		panel_10.add(textField_Id);
		textField_Id.setColumns(10);

		tf_longitude = new JTextField();
		tf_longitude.setColumns(10);
		panel_10.add(tf_longitude);

		tf_latitude = new JTextField();
		panel_10.add(tf_latitude);
		tf_latitude.setColumns(10);

		tf_elevation = new JTextField();
		panel_10.add(tf_elevation);
		tf_elevation.setColumns(10);

		tf_radius = new JTextField();
		panel_10.add(tf_radius);
		tf_radius.setColumns(10);

		tf_suRadius = new JTextField();
		panel_10.add(tf_suRadius);
		tf_suRadius.setColumns(10);

		tf_eMax = new JTextField();
		panel_10.add(tf_eMax);
		tf_eMax.setColumns(10);

		tf_eS = new JTextField();
		tf_eS.setColumns(10);
		panel_10.add(tf_eS);

		cb_uartDataRate = new JComboBox<String>();
		cb_uartDataRate.setEditable(true);
		cb_uartDataRate.setModel(new DefaultComboBoxModel<String>(
				new String[] { "-", "2400", "3600", "4800", "9600", "38400", "115200" }));
		cb_uartDataRate.setSelectedIndex(4);
		panel_10.add(cb_uartDataRate);

		tf_drift = new JTextField();
		tf_drift.setText("0.00003");
		tf_drift.setColumns(10);
		panel_10.add(tf_drift);

		JPanel panel_11 = new JPanel();
		panel_8.add(panel_11, BorderLayout.EAST);
		panel_11.setLayout(new GridLayout(0, 1, 0, 0));

		// ------------------------------------------------------------------------------------------KADJOUH
		JPanel panel_1_0 = new JPanel();
		FlowLayout fl_panel_1_0 = (FlowLayout) panel_1_0.getLayout();
		fl_panel_1_0.setVgap(0);
		fl_panel_1_0.setHgap(0);
		panel_11.add(panel_1_0);

		JButton button_0_ = new JButton("");
		button_0_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setBatteryModelFileName(batteryModelComboBox.getSelectedItem() + "");

				JOptionPane.showMessageDialog(null, batteryModelComboBox.getSelectedItem() + "", "CupCarbon",
						JOptionPane.INFORMATION_MESSAGE);

				MapLayer.getMapViewer().repaint();
			}
		});
		button_0_.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_1_0.add(button_0_);
		// ------------------------------------------------------------------------------------------KADJOUH

		JPanel panel_1_1 = new JPanel();
		FlowLayout fl_panel_1_1 = (FlowLayout) panel_1_1.getLayout();
		fl_panel_1_1.setVgap(0);
		fl_panel_1_1.setHgap(0);
		panel_11.add(panel_1_1);

		JButton button_1_ = new JButton("");
		button_1_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setScriptFileName(scriptComboBox.getSelectedItem() + "");
				MapLayer.getMapViewer().repaint();
			}
		});
		button_1_.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_1_1.add(button_1_);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_6 = (FlowLayout) panel_1.getLayout();
		flowLayout_6.setVgap(0);
		flowLayout_6.setHgap(0);
		panel_11.add(panel_1);

		JButton button_1 = new JButton("");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setGpsFileName(gpsPathNameComboBox.getSelectedItem() + "");
			}
		});
		button_1.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_1.add(button_1);

		JPanel panel_Id = new JPanel();
		FlowLayout flowLayoutId = (FlowLayout) panel_Id.getLayout();
		flowLayoutId.setVgap(0);
		flowLayoutId.setHgap(0);
		panel_11.add(panel_Id);

		JButton button_Id = new JButton("");
		button_Id.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DeviceList.setId(textField_Id.getText());
			}
		});
		button_Id.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_Id.add(button_Id);

		JPanel panel_12 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_12.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		panel_11.add(panel_12);

		JPanel panel_20 = new JPanel();
		FlowLayout flowLayout_20 = (FlowLayout) panel_20.getLayout();
		flowLayout_20.setVgap(0);
		flowLayout_20.setHgap(0);
		panel_11.add(panel_20);

		JButton button_5 = new JButton("");
		button_5.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setLatitude(tf_latitude.getText());
			}
		});
		panel_20.add(button_5);

		JButton button_2 = new JButton("");
		button_2.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setLongitude(tf_elevation.getText());
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
				DeviceList.setElevation(tf_elevation.getText());
			}
		});
		button_3.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
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
		button_4.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_14.add(button_4);

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
		button_6.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
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
		button_7.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel.add(button_7);

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

		JButton button_10 = new JButton("");
		button_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setSensingEnergy(tf_eS.getText());
			}
		});
		button_10.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_5.add(button_10);

		JPanel panel_Drift = new JPanel();
		FlowLayout flowLayout_Drift = (FlowLayout) panel_Drift.getLayout();
		flowLayout_Drift.setVgap(0);
		flowLayout_Drift.setHgap(0);
		panel_11.add(panel_Drift);

		JButton button_Drift = new JButton("");
		button_Drift.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setSigmaOfDriftTime(Double.parseDouble(tf_drift.getText()));
			}
		});
		button_Drift.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_Drift.add(button_Drift);

		JButton button_cbuartdr = new JButton("");
		button_cbuartdr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setUartDataRate((String) cb_uartDataRate.getSelectedItem());
			}
		});
		button_cbuartdr.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_Slp.add(button_cbuartdr);
	}

}
