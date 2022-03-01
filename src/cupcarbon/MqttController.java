package cupcarbon;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

import org.eclipse.paho.client.mqttv3.MqttException;

import device.DeviceList;
import device.IoTMqttModule;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MqttController implements Initializable{
		
	@FXML
	private Button closeButton;
	
	@FXML
	private TextField txtBroker;
	
	@FXML
	private TextField txtPort;
	
	@FXML
	private TextField txtUser;
	
	@FXML
	private TextField txtPassword;
	
	@FXML
	private TextField txtRNTopic;
	
	@FXML
	public void generate() {
		txtRNTopic.setText("cupcarbon_sim/"+CupCarbon.generateCode(30));
	}
	
	@FXML
	public void ok() {
		IoTMqttModule.broker = txtBroker.getText();
		IoTMqttModule.port = txtPort.getText();
		IoTMqttModule.user = txtUser.getText().isEmpty()?"-":txtUser.getText();
		IoTMqttModule.password = txtPassword.getText().isEmpty()?"-":txtPassword.getText();
		
		try {
			DeviceList.initBrokers();
		} catch (MqttException e1) {
			e1.printStackTrace();
		}
		
		try {
			FileOutputStream fos = new FileOutputStream("mqtt_code.par");
			IoTMqttModule.com_real_node_topic = txtRNTopic.getText();			
			PrintStream ps = new PrintStream(fos);
			ps.println(txtRNTopic.getText());
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Stage stage = (Stage) closeButton.getScene().getWindow();
	    stage.close();
	}

	@FXML
	public void annuler() {
		Stage stage = (Stage) closeButton.getScene().getWindow();
	    stage.close();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtBroker.setText(IoTMqttModule.broker);
		txtPort.setText(IoTMqttModule.port);
		txtUser.setText(IoTMqttModule.user.equals("-")?"":IoTMqttModule.user);
		txtPassword.setText(IoTMqttModule.password.equals("-")?"":IoTMqttModule.password);
		txtRNTopic.setText(IoTMqttModule.com_real_node_topic);
	}
}
