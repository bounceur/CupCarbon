package cupcarbon;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.virtualys.cupcarbon.communication.Communication;
import com.virtualys.cupcarbon.communication.client.socket.ClientCommand;
import com.virtualys.cupcarbon.communication.client.socket.ClientData;

public class VisualisationPorts extends JInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtHost;
	private JTextField txtPortCom;
	private JTextField txtPortData;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VisualisationPorts frame = new VisualisationPorts();
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
	public VisualisationPorts() {
		setTitle("Visualisation");
		setBounds(100, 100, 450, 161);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		txtHost = new JTextField();
		txtHost.setFont(new Font("Courier New", Font.PLAIN, 13));
		txtHost.setText("localhost");
		panel.add(txtHost);
		txtHost.setColumns(10);
		
		txtPortCom = new JTextField();
		txtPortCom.setText("9900");
		txtPortCom.setFont(new Font("Courier New", Font.PLAIN, 13));
		panel.add(txtPortCom);
		txtPortCom.setColumns(10);
		
		txtPortData = new JTextField();
		txtPortData.setText("9901");
		txtPortData.setFont(new Font("Courier New", Font.PLAIN, 13));
		panel.add(txtPortData);
		txtPortData.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new GridLayout(3, 1, 0, 0));
		
		JLabel lblNewLabel = new JLabel(" Host  ");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_1.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel(" Port (Commands) ");
		lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_1.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel(" Port (Data)  ");
		lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_1.add(lblNewLabel_2);
		
		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Run Server");
		panel_2.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Stop Server");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Stop& Server
			}
		});
		panel_2.add(btnNewButton_1);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientCommand.host = txtHost.getText();
				ClientCommand.port = Integer.parseInt(txtPortCom.getText());
				ClientData.host = txtHost.getText();
				ClientData.port = Integer.parseInt(txtPortData.getText());
				Communication.initialize();
				//_OM_Vlys_end
				
			}
		});

	}

}
