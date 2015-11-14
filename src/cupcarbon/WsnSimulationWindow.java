package cupcarbon;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import wisen_simulation.SimulationInputs;
import wisen_simulation.WisenSimulation;
import device.DataInfo;

public class WsnSimulationWindow extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private static WisenSimulation wisenSimulation;

	private JPanel panel;
	private JTextField iterNumberTextField;
	private JComboBox<String> freqComboBox;
	private static JProgressBar progressBar;
	private static JLabel stateLabel;
	private JCheckBox cboxMobility;
	private JTextField vdTextField;
	private JTextField textField;

	private JCheckBox chckbxGenerateResults;
	private JCheckBox chckbxGenerateLog;
	private JCheckBox chckbxShowInConsole;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WsnSimulationWindow frame = new WsnSimulationWindow();
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
	public WsnSimulationWindow() {
		setTitle("Simulation Parameters");
		setIconifiable(true);
		setClosable(true);
		setBounds(100, 100, 387, 400);
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(7, 7, 7, 7));
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel panel_12 = new JPanel();
		panel.add(panel_12);
		panel_12.setLayout(new GridLayout(1, 1, 5, 5));

		JPanel panel_8 = new JPanel();
		panel_12.add(panel_8);
		panel_8.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_8.setLayout(new BoxLayout(panel_8, BoxLayout.X_AXIS));

		JLabel lblNumberOfIterations = new JLabel("Number of Iterations");
		panel_8.add(lblNumberOfIterations);
		lblNumberOfIterations.setFont(new Font("Arial", Font.PLAIN, 12));

		iterNumberTextField = new JTextField();
		panel_8.add(iterNumberTextField);
		iterNumberTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		iterNumberTextField.setText("10000");
		iterNumberTextField.setColumns(10);

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel_3.add(panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));

		JLabel lblSerialFrequency = new JLabel("UART Data Rate ");
		lblSerialFrequency.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_4.add(lblSerialFrequency);

		freqComboBox = new JComboBox<String>();
		freqComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_4.add(freqComboBox);
		freqComboBox.setToolTipText("UART Data Rate");
		freqComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"-", "2400", "3600", "4800", "9600","38400","115200"}));
		freqComboBox.setSelectedIndex(4);

		JLabel lblBaudRate = new JLabel(" baud");
		lblBaudRate.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_4.add(lblBaudRate);

		JPanel panel_18 = new JPanel();
		panel_18.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_3.add(panel_18);
		panel_18.setLayout(new GridLayout(1, 3, 0, 0));
		
		chckbxGenerateResults = new JCheckBox("Generate Results");
		chckbxGenerateResults.setFont(new Font("Arial", Font.PLAIN, 12));
		chckbxGenerateResults.setSelected(true);
		panel_18.add(chckbxGenerateResults);
		
		chckbxGenerateLog = new JCheckBox("Generate Log");
		chckbxGenerateLog.setFont(new Font("Arial", Font.PLAIN, 12));
		chckbxGenerateLog.setSelected(true);
		panel_18.add(chckbxGenerateLog);
		
		chckbxShowInConsole = new JCheckBox("Show In Console");
		chckbxShowInConsole.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_18.add(chckbxShowInConsole);

		JPanel panel_17 = new JPanel();
		panel_3.add(panel_17);
		panel_17.setLayout(new GridLayout(1, 2, 0, 0));

		JLabel lblCommunication = new JLabel("  Failure Probability   ");
		lblCommunication.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_17.add(lblCommunication);

		textField = new JTextField();
		textField.setFont(new Font("Arial", Font.PLAIN, 12));
		textField.setText("0");
		panel_17.add(textField);
		textField.setColumns(10);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_1.add(panel_7);
		panel_7.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel_13 = new JPanel();
		panel_13.setBorder(new EmptyBorder(0, 0, 0, 0));
		panel_7.add(panel_13);
		panel_13.setLayout(new GridLayout(2, 1, 0, 0));

		JPanel panel_16 = new JPanel();
		panel_13.add(panel_16);
		panel_16.setLayout(new BoxLayout(panel_16, BoxLayout.X_AXIS));

		JLabel lblSpeed = new JLabel(" Speed  ");
		lblSpeed.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_16.add(lblSpeed);

		vdTextField = new JTextField();
		panel_16.add(vdTextField);
		vdTextField.setToolTipText("Visual Delay");
		vdTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		vdTextField.setText("10");
		vdTextField.setColumns(10);

		JLabel lblMs = new JLabel("  ms   ");
		lblMs.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_16.add(lblMs);
		
		JPanel panel_19 = new JPanel();
		panel_13.add(panel_19);
				panel_19.setLayout(new GridLayout(0, 2, 0, 0));
				
						cboxMobility = new JCheckBox("Mobility / Sensor Events");
						panel_19.add(cboxMobility);
						cboxMobility.setFont(new Font("Arial", Font.PLAIN, 12));

		JSeparator separator = new JSeparator();
		panel_1.add(separator);

		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5);
		panel_5.setLayout(new GridLayout(0, 1, 0, 0));
				
		JPanel panel_20 = new JPanel();
		panel_5.add(panel_20);
		panel_20.setLayout(new GridLayout(2, 2, 0, 0));
		
		JButton btnNewButton_1 = new JButton("Check");
		panel_20.add(btnNewButton_1);
		btnNewButton_1.setIcon(new ImageIcon("images/check.png"));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WisenSimulation.check();
			}
		});
		btnNewButton_1.setFont(new Font("Arial", Font.PLAIN, 12));
								
		JButton btnApply = new JButton("Apply");
		btnApply.setIcon(new ImageIcon("images/stylo.png"));
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (freqComboBox.getSelectedItem().equals("-"))
					DataInfo.UartDataRate = 0;
				else
					DataInfo.UartDataRate = Integer.parseInt((String) freqComboBox.getSelectedItem());
				SimulationInputs.iterNumber = Integer.parseInt(iterNumberTextField.getText());
				//SimulationInputs.energyMax = Integer.parseInt(energyMaxTextField.getText());
				SimulationInputs.mobility = cboxMobility.isSelected();
				SimulationInputs.visualDelay = Integer.parseInt(vdTextField.getText());
				SimulationInputs.displayLog = chckbxGenerateLog.isSelected();
				SimulationInputs.displayResults = chckbxGenerateResults.isSelected();
				SimulationInputs.showInConsole = chckbxShowInConsole.isSelected(); 
			}
		});
		btnApply.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_20.add(btnApply);

		JButton button = new JButton("Run simulation ");
		panel_20.add(button);
		button.setIcon(new ImageIcon("images/run2.png"));
		button.setFont(new Font("Arial", Font.PLAIN, 12));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulateCallBack();
			}
		});

		JButton btnNewButton_2 = new JButton("Stop Simulation");
		btnNewButton_2.setIcon(new ImageIcon("images/supprimer.png"));
		panel_20.add(btnNewButton_2);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stopSimulation();
				/*if (rdbtnCpuSimulation.isSelected()) {
					stopSimulation(1);
				}
				if (rdbtnGpuSimulation.isSelected()) {
					stopSimulation(2);
				}*/
			}
		});
		btnNewButton_2.setFont(new Font("Arial", Font.PLAIN, 12));

		JSeparator separator_2 = new JSeparator();
		panel_1.add(separator_2);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel_1.add(panel_6);
		panel_6.setLayout(new GridLayout(2, 1, 0, 0));

		stateLabel = new JLabel("State");
		stateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		stateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_6.add(stateLabel);

		progressBar = new JProgressBar();
		progressBar.setMaximum(1000);
		progressBar.setFont(new Font("Arial", Font.PLAIN, 12));
		progressBar.setStringPainted(true);
		panel_6.add(progressBar);

		JSeparator separator_4 = new JSeparator();
		panel_1.add(separator_4);

	}

	public static void setProgress(int v) {
		progressBar.setValue(v);
	}

	public static void setState(String s) {
		stateLabel.setText(s);
	}

	public void simulateCallBack() {
		if (freqComboBox.getSelectedItem().equals("-"))
			DataInfo.UartDataRate = 0;
		else
			DataInfo.UartDataRate = Integer.parseInt((String) freqComboBox.getSelectedItem());
		SimulationInputs.iterNumber = Integer.parseInt(iterNumberTextField.getText());
		//SimulationInputs.energyMax = Integer.parseInt(energyMaxTextField.getText());
		SimulationInputs.mobility = cboxMobility.isSelected();
		SimulationInputs.visualDelay = Integer.parseInt(vdTextField.getText());
		SimulationInputs.displayLog = chckbxGenerateLog.isSelected();
		SimulationInputs.displayResults = chckbxGenerateResults.isSelected();
		SimulationInputs.showInConsole = chckbxShowInConsole.isSelected(); 
		wisenSimulation = new WisenSimulation();
		wisenSimulation.start();
	}
	
	public static void quickRun() {
		wisenSimulation = new WisenSimulation();
		wisenSimulation.start();
	}

	public static void stopSimulation() {
		wisenSimulation.stopSimulation();
	}

}
