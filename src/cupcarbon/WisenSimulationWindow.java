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
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import map.MapLayer;
import wisen_simulation.SimulationInputs;
import wisen_simulation.WisenSimulation;

public class WisenSimulationWindow extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private static WisenSimulation wisenSimulation;

	private JPanel panel;
	private JTextField simulationTimeTextField;
	private static JProgressBar progressBar;
	private static JLabel stateLabel;
	private JCheckBox cboxMobility;
	private JTextField vdTextField;

	private JCheckBox chckbxGenerateResults;
	private JCheckBox chckbxGenerateLog;
	private JCheckBox chckbxShowInConsole;
	private JCheckBox chckbxAck;
	private JTextField ackProbaCB;
	private JComboBox<String> protocolCBox;
	private JComboBox<String> ackTypeCB;
	private JCheckBox checkBoxAck;
	private JCheckBox chbxSymRadio;
	private JCheckBox chckbxCpuDrift;
	private JTextField tf_arrowsSpeed;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WisenSimulationWindow frame = new WisenSimulationWindow();
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
	public WisenSimulationWindow() {
		setTitle("Simulation Parameters");
		setIconifiable(true);
		setClosable(true);
		setBounds(100, 100, 416, 471);
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

		JLabel lblSimulationTime = new JLabel(" Simulation time   ");
		panel_8.add(lblSimulationTime);
		lblSimulationTime.setFont(new Font("Arial", Font.PLAIN, 12));

		simulationTimeTextField = new JTextField();
		panel_8.add(simulationTimeTextField);
		simulationTimeTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		simulationTimeTextField.setText(""+SimulationInputs.simulationTime);
		simulationTimeTextField.setColumns(10);
		
		JLabel lblSeconds = new JLabel("  second(s)  ");
		lblSeconds.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_8.add(lblSeconds);

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

		JPanel panel_18 = new JPanel();
		panel_18.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_3.add(panel_18);
		panel_18.setLayout(new GridLayout(1, 3, 0, 0));
		
		chckbxGenerateResults = new JCheckBox("Results");
		chckbxGenerateResults.setFont(new Font("Arial", Font.PLAIN, 12));
		chckbxGenerateResults.setSelected(true);
		panel_18.add(chckbxGenerateResults);
		
		chckbxGenerateLog = new JCheckBox("Log");
		chckbxGenerateLog.setFont(new Font("Arial", Font.PLAIN, 12));
		chckbxGenerateLog.setSelected(true);
		panel_18.add(chckbxGenerateLog);
		
		chckbxShowInConsole = new JCheckBox("Console");
		chckbxShowInConsole.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_18.add(chckbxShowInConsole);

		JPanel panel_17 = new JPanel();
		panel_3.add(panel_17);
		panel_17.setLayout(new GridLayout(1, 2, 0, 0));
		
		chbxSymRadio = new JCheckBox("Symmetrical Radio Links");
		chbxSymRadio.setToolTipText("Symmetrical Radio Links");
		chbxSymRadio.setSelected(true);
		chbxSymRadio.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_17.add(chbxSymRadio);
		
		chckbxCpuDrift = new JCheckBox("Drift (sigma)");
		panel_17.add(chckbxCpuDrift);
		chckbxCpuDrift.setFont(new Font("Arial", Font.PLAIN, 12));

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.add(panel_7);
		panel_7.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel_13 = new JPanel();
		panel_13.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_7.add(panel_13);
		panel_13.setLayout(new GridLayout(4, 1, 0, 0));

		JPanel panel_16 = new JPanel();
		panel_16.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_13.add(panel_16);
		panel_16.setLayout(new BoxLayout(panel_16, BoxLayout.X_AXIS));

		JLabel lblSpeed = new JLabel("  Simulation Speed  ");
		lblSpeed.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_16.add(lblSpeed);

		vdTextField = new JTextField();
		panel_16.add(vdTextField);
		vdTextField.setToolTipText("Visual Delay");
		vdTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		vdTextField.setText("10");
		vdTextField.setColumns(10);

		JLabel lblMs = new JLabel("  ms / Arrows  ");
		lblMs.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_16.add(lblMs);
		
		tf_arrowsSpeed = new JTextField();
		tf_arrowsSpeed.setFont(new Font("Arial", Font.PLAIN, 12));
		tf_arrowsSpeed.setText("50");
		panel_16.add(tf_arrowsSpeed);
		tf_arrowsSpeed.setColumns(10);
		
		JLabel label = new JLabel("  ms   ");
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_16.add(label);
		
		JPanel panel_19 = new JPanel();
		panel_19.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_13.add(panel_19);
				panel_19.setLayout(new GridLayout(0, 2, 0, 0));
				
						cboxMobility = new JCheckBox("Mobility / Sensor Events");
						panel_19.add(cboxMobility);
						cboxMobility.setFont(new Font("Arial", Font.PLAIN, 12));
						
						JPanel panel_2 = new JPanel();
						panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
						panel_13.add(panel_2);
						panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
						
						JLabel lblProtocol = new JLabel(" Protocol ");
						lblProtocol.setFont(new Font("Arial", Font.PLAIN, 12));
						panel_2.add(lblProtocol);
						
						protocolCBox = new JComboBox<String>();
						protocolCBox.setToolTipText("Protocol");
						protocolCBox.setModel(new DefaultComboBoxModel<String>(new String[] {"CSMA"}));
						protocolCBox.setSelectedIndex(0);
						protocolCBox.setFont(new Font("Arial", Font.PLAIN, 12));
						panel_2.add(protocolCBox);
						
						JPanel panel_9 = new JPanel();
						panel_9.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
						panel_13.add(panel_9);
						panel_9.setLayout(new BoxLayout(panel_9, BoxLayout.X_AXIS));
						
						checkBoxAck = new JCheckBox("ACK");
						checkBoxAck.setFont(new Font("Arial", Font.PLAIN, 12));
						panel_9.add(checkBoxAck);
						
						ackTypeCB = new JComboBox<String>();
						ackTypeCB.setToolTipText("ACK");
						ackTypeCB.setModel(new DefaultComboBoxModel<String>(new String[] {"Probability", "Stable-distribution"}));
						ackTypeCB.setSelectedIndex(0);
						ackTypeCB.setFont(new Font("Arial", Font.PLAIN, 12));
						panel_9.add(ackTypeCB);
						
						ackProbaCB = new JTextField();
						ackProbaCB.setToolTipText("ACK Probability");
						ackProbaCB.setText(""+SimulationInputs.ackProba);
						ackProbaCB.setFont(new Font("Arial", Font.PLAIN, 12));
						ackProbaCB.setColumns(10);
						panel_9.add(ackProbaCB);
						
						chckbxAck = new JCheckBox("Show ACK");
						chckbxAck.setFont(new Font("Arial", Font.PLAIN, 12));
						panel_9.add(chckbxAck);

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
				apply();
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
		apply();
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

	public void apply() {
		SimulationInputs.simulationTime = Double.parseDouble(simulationTimeTextField.getText());
		SimulationInputs.mobility = cboxMobility.isSelected();
		SimulationInputs.visualDelay = Integer.parseInt(vdTextField.getText());
		SimulationInputs.arrowsDelay = Integer.parseInt(tf_arrowsSpeed.getText());
		SimulationInputs.displayLog = chckbxGenerateLog.isSelected();
		SimulationInputs.displayResults = chckbxGenerateResults.isSelected();
		SimulationInputs.showInConsole = chckbxShowInConsole.isSelected();
		SimulationInputs.protocol = protocolCBox.getSelectedIndex();
		SimulationInputs.ackType = ackTypeCB.getSelectedIndex();
		double proba = Double.parseDouble(ackProbaCB.getText());
		SimulationInputs.ackProba = (proba>1)?1:proba;
		SimulationInputs.showAckLinks = chckbxAck.isSelected();
		SimulationInputs.ack = checkBoxAck.isSelected();
		SimulationInputs.symmetricalLinks = chbxSymRadio.isSelected();
		SimulationInputs.cpuDrift = chckbxCpuDrift.isSelected();
		MapLayer.getMapViewer().repaint();
	}
	
}
