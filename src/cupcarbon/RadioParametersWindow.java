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

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import device.DeviceList;
import radio_module.Standard;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public class RadioParametersWindow extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	public static JTextField textField_My;
	public static JTextField textField_Ch;
	public static JTextField textField_NId;
	public static JTextField tf_radioRadius;
	public static JTextField tf_eTx;
	public static JTextField tf_eRx;
	public static JTextField tf_eSlp;
	public static JTextField tf_eL;
	public static JTextField tf_radioDataRate;
	public static JComboBox<String> stdComboBox;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RadioParametersWindow frame = new RadioParametersWindow();
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
	public RadioParametersWindow() {
		setResizable(true);
		setMaximizable(true);
		setClosable(true);
		setIconifiable(true);
		setTitle("Radio Parameters");
		setBounds(10, 10, 460, 356);

		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("Apply");
		btnNewButton.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH
				+ "loopnone2.png"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.updateRadioParaFromMap(
						tf_radioRadius.getText(),						
								tf_eTx.getText(),
								tf_eRx.getText(),
								tf_radioDataRate.getText(), tf_eSlp.getText(), tf_eL.getText()
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
		
		JLabel lblStandard = new JLabel("Standard");
		lblStandard.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblStandard);
		
		JLabel labelMy = new JLabel("My");
		labelMy.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(labelMy);
		
		JLabel labelCh = new JLabel("Ch");
		labelCh.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(labelCh);
		
		JLabel labelNId = new JLabel("Network Id");
		labelNId.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(labelNId);

		JLabel label_3 = new JLabel("Radio Radius");
		label_3.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(label_3);
		
		JLabel lblEnergyTx = new JLabel("Energy Tx");
		lblEnergyTx.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblEnergyTx);
		
		JLabel lblEnergyRx = new JLabel("Energy Rx");
		lblEnergyRx.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblEnergyRx);
		
		JLabel lblSleepingEnergy = new JLabel("Sleeping Energy");
		lblSleepingEnergy.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblSleepingEnergy);
		
		JLabel lblListeningEnergy = new JLabel("Listening Energy");
		lblListeningEnergy.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblListeningEnergy);
		
		JLabel lblRadioRate = new JLabel("Radio Data Rate (bit/sec)");
		lblRadioRate.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblRadioRate);

		JPanel panel_10 = new JPanel();
		panel_8.add(panel_10, BorderLayout.CENTER);
		panel_10.setLayout(new GridLayout(0, 1, 0, 0));
		
		stdComboBox = new JComboBox<String>();
		stdComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"NONE", "802.15.4", "WIFI 802.11", "LoRa"}));
		stdComboBox.setSelectedIndex(1);
		panel_10.add(stdComboBox);
		
		
		
		textField_My = new JTextField();
		panel_10.add(textField_My);
		textField_My.setColumns(10);
		
		textField_Ch = new JTextField();
		panel_10.add(textField_Ch);
		textField_Ch.setColumns(10);
		
		textField_NId = new JTextField();
		panel_10.add(textField_NId);
		textField_NId.setColumns(10);

		tf_radioRadius = new JTextField();
		panel_10.add(tf_radioRadius);
		tf_radioRadius.setColumns(10);
		
		tf_eTx = new JTextField();
		tf_eTx.setColumns(10);
		panel_10.add(tf_eTx);
		
		tf_eRx = new JTextField();
		tf_eRx.setColumns(10);
		panel_10.add(tf_eRx);
		
		tf_eSlp = new JTextField();
		tf_eSlp.setColumns(10);
		panel_10.add(tf_eSlp);
		
		tf_eL = new JTextField();
		tf_eL.setColumns(10);
		panel_10.add(tf_eL);
		
		tf_radioDataRate = new JTextField();
		tf_radioDataRate.setText("250000");
		tf_radioDataRate.setColumns(10);
		panel_10.add(tf_radioDataRate);

		JPanel panel_11 = new JPanel();
		panel_8.add(panel_11, BorderLayout.EAST);
		panel_11.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel_Std = new JPanel();
		FlowLayout flowLayoutStd = (FlowLayout) panel_Std.getLayout();
		flowLayoutStd.setVgap(0);
		flowLayoutStd.setHgap(0);
		panel_11.add(panel_Std);
		
		JButton button_Std = new JButton("");
		button_Std.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DeviceList.setStd((String) stdComboBox.getSelectedItem());
				tf_radioDataRate.setText(Standard.getDataRate((String) stdComboBox.getSelectedItem()));
			}
		});
		button_Std.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_Std.add(button_Std);
		
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
		
		
		JButton button_My = new JButton("");
		button_My.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DeviceList.setMy(textField_My.getText());
			}
		});
		button_My.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_My.add(button_My);
		
		
		JButton button_Ch = new JButton("");
		button_Ch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DeviceList.setCh(textField_Ch.getText());
			}
		});
		button_Ch.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_Ch.add(button_Ch);
		
		JButton button_NId = new JButton("");
		button_NId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DeviceList.setNId(textField_NId.getText());
			}
		});
		button_NId.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_NId.add(button_NId);

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
		button_5.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_15.add(button_5);

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
		button_8.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
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
		button_9.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_4.add(button_9);
		
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
		
		
		
		
		JButton button_Slp = new JButton("");
		button_Slp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setESlp(tf_eSlp.getText());
			}
		});
		button_Slp.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_Slp.add(button_Slp);
		
		JButton button_L = new JButton("");
		button_L.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setEL(tf_eL.getText());
			}
		});
		button_L.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_L.add(button_L);
		
		
		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout_11 = (FlowLayout) panel_6.getLayout();
		flowLayout_11.setVgap(0);
		flowLayout_11.setHgap(0);
		panel_11.add(panel_6);
		
		JButton button_11 = new JButton("");
		button_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setRadioDataRate((String) tf_radioDataRate.getText());
			}
		});
		button_11.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "loopnone2.png"));
		panel_6.add(button_11);
	}

}
