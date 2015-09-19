package cupcarbon;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import solver.SolverProxyParams;

public class TrackerProxyParamsWindow extends JInternalFrame {
	
	private static final long serialVersionUID = 1L;
	private JTextField hostTextEdit;
	private JTextField portTextEdit;
	private static TrackerProxyParamsWindow frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new TrackerProxyParamsWindow();
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
	public TrackerProxyParamsWindow() {
		setClosable(true);
		setBounds(100, 100, 450, 133);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel = new JLabel("URL");
		panel.add(lblNewLabel);
		
		hostTextEdit = new JTextField();
		hostTextEdit.setText("localhost");
		hostTextEdit.setFont(new Font("Courier New", Font.BOLD, 12));
		hostTextEdit.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(hostTextEdit);
		hostTextEdit.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_1 = new JLabel("Port");
		panel_1.add(lblNewLabel_1);
		
		portTextEdit = new JTextField();
		portTextEdit.setText("8080");
		portTextEdit.setFont(new Font("Courier New", Font.BOLD, 12));
		portTextEdit.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(portTextEdit);
		portTextEdit.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		getContentPane().add(panel_3);
		panel_3.setLayout(new GridLayout(1, 2, 0, 0));
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				
				System.out.println(SolverProxyParams.port);
			}
		});
		panel_3.add(btnOk);

	}

}
