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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class MtSimulationWindow extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField delayTextField;
	private JComboBox<String> freqComboBox;
	private static JProgressBar progressBar;
	private static JLabel stateLabel;
	private JTextField ldelayTextField;
	private JTextField stepTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MtSimulationWindow frame = new MtSimulationWindow();
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
	public MtSimulationWindow() {
		setTitle("Simulation Parameters (MT)");
		setIconifiable(true);
		setClosable(true);
		setBounds(100, 100, 275, 273);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 1, 5, 5));

		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.add(panel_8);
		panel_8.setLayout(new BorderLayout(0, 0));

		JLabel lblNumberOfIterations = new JLabel("Delay");
		panel_8.add(lblNumberOfIterations, BorderLayout.WEST);
		lblNumberOfIterations.setFont(new Font("Arial", Font.PLAIN, 12));

		delayTextField = new JTextField();
		panel_8.add(delayTextField);
		delayTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		delayTextField.setText("100");
		delayTextField.setColumns(10);

		JLabel lblHours = new JLabel("Hours");
		lblHours.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_8.add(lblHours, BorderLayout.EAST);

		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.add(panel_9);
		panel_9.setLayout(new BorderLayout(0, 0));

		JLabel lblEnergy = new JLabel("Logic Delay");
		panel_9.add(lblEnergy, BorderLayout.WEST);
		lblEnergy.setFont(new Font("Arial", Font.PLAIN, 12));

		ldelayTextField = new JTextField();
		ldelayTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		ldelayTextField.setText("1");
		panel_9.add(ldelayTextField);
		ldelayTextField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Minutes");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_9.add(lblNewLabel, BorderLayout.EAST);

		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.add(panel_10);
		panel_10.setLayout(new BorderLayout(0, 0));

		JLabel lblStep = new JLabel("Step");
		lblStep.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_10.add(lblStep, BorderLayout.WEST);

		stepTextField = new JTextField();
		stepTextField.setText("1");
		stepTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		stepTextField.setColumns(10);
		panel_10.add(stepTextField);

		JLabel lblHours_1 = new JLabel("Hours");
		lblHours_1.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_10.add(lblHours_1, BorderLayout.EAST);

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EmptyBorder(0, 0, 0, 0));
		panel_1.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel_3.add(panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));

		JLabel lblSerialFrequency = new JLabel("Serial Frequency");
		lblSerialFrequency.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_4.add(lblSerialFrequency);

		freqComboBox = new JComboBox<String>();
		freqComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_4.add(freqComboBox);
		freqComboBox.setToolTipText("Baud Rate");
		freqComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "110",
				"300", "1200", "2400", "4800", "9600", "19200", "38400",
				"57600", "115200", "230400", "460800", "921600", "1843200",
				"3686400" }));
		freqComboBox.setSelectedIndex(5);

		JLabel lblBaudRate = new JLabel("Baud rate");
		lblBaudRate.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_4.add(lblBaudRate);

		JSeparator separator_1 = new JSeparator();
		panel_1.add(separator_1);

		JPanel panel_7 = new JPanel();
		panel_1.add(panel_7);
		panel_7.setLayout(new GridLayout(2, 1, 0, 0));

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		panel_2.setLayout(new GridLayout(1, 2, 0, 0));

		JButton button = new JButton("Run simulation ");
		panel_2.add(button);
		button.setFont(new Font("Arial", Font.PLAIN, 12));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//long defaulSimulationDelay = Integer.parseInt(delayTextField.getText()) * 3600000;
				//long defaulSimulationLogicDelay = Integer.parseInt(ldelayTextField.getText()) * 60000;
				//long step = Integer.parseInt(stepTextField.getText()) * 3600000;
				//WorldMap.comSimulate("Standard", "log", defaulSimulationDelay, defaulSimulationLogicDelay, step);
				//// WorldMap.comSimulate("Standard", "log");
			}
		});

		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5);
		panel_5.setLayout(new GridLayout(0, 1, 0, 0));

		JSeparator separator_2 = new JSeparator();
		panel_1.add(separator_2);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel_1.add(panel_6);
		panel_6.setLayout(new BorderLayout(0, 0));

		stateLabel = new JLabel("State");
		stateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		stateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_6.add(stateLabel, BorderLayout.NORTH);

		progressBar = new JProgressBar();
		progressBar.setMaximum(1000);
		progressBar.setFont(new Font("Arial", Font.PLAIN, 12));
		progressBar.setStringPainted(true);
		panel_6.add(progressBar);

	}

	public static void setProgress(int v) {
		progressBar.setValue(v);
	}

	public static void setState(String s) {
		stateLabel.setText(s);
	}

}
