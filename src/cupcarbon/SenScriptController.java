package cupcarbon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import project.Project;

public class SenScriptController implements Initializable{
	
	@FXML
	private TextArea zone;	
	
	@FXML
	private void loopCom() {
		zone.replaceSelection("loop\n");
	}
	
	@FXML
	private void setCom() {
		zone.replaceSelection("set x 0\n");
	}
	
	@FXML
	private void sendCom() {
		zone.replaceSelection("send 1\n");
	}
	
	@FXML
	private void delayCom() {
		zone.replaceSelection("delay 1000\n");
	}
	
	@FXML
	private void example1_1Com() {
		zone.clear();
		zone.replaceSelection("//Transmitter\nloop\nsend 1\ndelay 1000\nsend 0\ndelay 1000");
	}
	
	@FXML
	private void example1_2Com() {
		zone.clear();
		zone.replaceSelection("//Transmitter\natget id id\nloop\ndata p $id A\nsend $p\ndelay 1000\ndata p $id B\nsend $p\ndelay 1000");
	}
	
	@FXML
	private void example2_1Com() {
		zone.clear();
		zone.replaceSelection("//Router\nloop\nwait\nread v\nsend $v 2");
	}
	
	@FXML
	private void example2_2Com() {
		zone.clear();
		zone.replaceSelection("//Router\natget id id\nloop\nwait\nread rp\nrdata $rp rid v\ndata p $id $v\nsend $p * $rid");
	}
	
	@FXML
	private void example3_1Com() {
		zone.clear();
		zone.replaceSelection("//Receiver\nloop\nwait\nread v\nmark $v");
	}
	
	@FXML
	private void example3_2Com() {
		zone.clear();
		zone.replaceSelection("//Receiver\nloop\nwait\nread rp\nrdata $rp rid v\nif($v==A)\n  mark 1\nelse\n  mark 0\nend");
	}
	
	@FXML
	private void ifthenelseCom() {
		zone.replaceSelection("if()\n\nthen\n\nelse\n\nend\n");
	}
		
	@FXML
	private void waitCom() {
		zone.replaceSelection("wait\n");
	}
	
	@FXML
	private void readCom() {
		zone.replaceSelection("read x\n");
	}
	
	@FXML
	private void forCom() {
		zone.replaceSelection("for i 0 9\n\nend\n");
	}
	
	@FXML
	private void incCom() {
		zone.replaceSelection("inc x\n");
	}
	
	@FXML
	private ComboBox<String> txtLoadFileName;
	
	@FXML
	private TextField txtFileName;
	
	@FXML
	public void load() {
		if (txtLoadFileName.getSelectionModel().getSelectedIndex() > 0) {
			txtFileName.setText(txtLoadFileName.getSelectionModel().getSelectedItem().toString());
			zone.setText("");
			try {
				FileInputStream in = new FileInputStream(new File(
						Project.getScriptFileFromName(txtLoadFileName.getSelectionModel()
								.getSelectedItem().toString())));
				byte[] bytes = new byte[in.available()];
				in.read(bytes);
				zone.setText(new String(bytes));
				in.close();
			} catch (Exception e1) {}
		} else {			
			txtFileName.setText("");
			zone.setText("loop\n");
			zone.requestFocus();
		}
	}
	
	@FXML
	public void save() {
		if(!txtFileName.getText().equals("")) {
			try {
				String fileName = txtFileName.getText();
				fileName = fileName.trim();
				fileName = fileName.replaceAll(" ", "");
				PrintStream ps;
				ps = new PrintStream(new FileOutputStream(Project
						.getScriptFileFromName(Project
								.getScriptFileExtension(fileName))));
				ps.print(zone.getText());
				ps.close();
				zone.setText("");
				txtFileName.setText("");

				File scriptFiles = new File(Project.getProjectScriptPath());
				String[] c = scriptFiles.list();
				txtLoadFileName.getItems().removeAll(txtLoadFileName.getItems());
				txtLoadFileName.getItems().add("New scenario ...");
				for (int i = 0; i < c.length; i++) {
					txtLoadFileName.getItems().add(c[i]);
				}
				
				CupCarbon.cupCarbonController.initScriptGpsEventComboBoxes();
				CupCarbon.cupCarbonController.createContextMenu();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initComboBox();
	}

	public void initComboBox() {
		File scriptFiles = new File(Project.getProjectScriptPath());
		String[] c = scriptFiles.list();
		txtLoadFileName.getItems().removeAll(txtLoadFileName.getItems());
		txtLoadFileName.getItems().add("New script ...");
		if (scriptFiles.isDirectory() && c != null) {
			for (int i = 0; i < c.length; i++) {
				txtLoadFileName.getItems().add(c[i]);
			}
		}
	}
}
