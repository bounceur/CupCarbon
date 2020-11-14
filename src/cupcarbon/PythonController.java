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

public class PythonController implements Initializable{
	
	@FXML
	private TextArea zone;	
	
	@FXML
	private void loopCom() {
		zone.replaceSelection("while node.loop():\n");
	}
	
	@FXML
	private void sendCom1() {
		zone.replaceSelection("node.send(\"1\")\n");
	}
	
	@FXML
	private void sendCom2() {
		zone.replaceSelection("node.send(\"1\", 0, 2)\n");
	}
	
	@FXML
	private void sendCom3() {
		zone.replaceSelection("node.send(\"1\", 2)\n");
	}
	
	@FXML
	private void delayCom() {
		zone.replaceSelection("time.sleep(1)\n");
	}
	
	@FXML
	private void pubCB() {
		zone.clear();
		zone.replaceSelection("#Publisher\n\nimport time\n\nwhile node.loop():\n\tnode.publish(\"cupcarbon/sensor\", \"1\")\n\ttime.sleep(2)\n\tnode.publish(\"cupcarbon/sensor\", \"0\")\n\ttime.sleep(2)");
	}
	
	@FXML
	private void subCB() {
		zone.clear();
		zone.replaceSelection("#Subscriber\n\nimport time\n\nnode.subscribe(\"cupcarbon/sensor\")\n\ndef callback(topic, message):\n\tnode.mark(message)\n\nwhile node.loop():\n\ttime.sleep(1)");
	}
	
	@FXML
	private void example1_1Com() {
		zone.clear();
		zone.replaceSelection("#Transmitter\nimport time\nwhile node.loop():\n\tnode.send(\"1\")\n\ttime.sleep(1)\n\tnode.send(\"0\")\n\ttime.sleep(1)");
	}
	
	@FXML
	private void example1_2Com() {
		zone.clear();
		zone.replaceSelection("#Transmitter\nimport time\nwhile node.loop():\n\tnode.send(\"1\", 2)\n\ttime.sleep(1)\n\tnode.send(\"0\", 2)\n\ttime.sleep(1)");
	}
	
	@FXML
	private void example1_3Com() {
		zone.clear();
		zone.replaceSelection("#Transmitter\nimport time\nwhile node.loop():\n\tnode.send(\"1\", 0, 10)\n\ttime.sleep(1)\n\tnode.send(\"0\", 0, 10)\n\ttime.sleep(1)");
	}
	
	@FXML
	private void example3_1Com() {
		zone.clear();
		zone.replaceSelection("#Receiver\nimport time\nwhile node.loop():\n\tn = node.bufferSize()\n\tif n>0:\n\t\tx = node.read()\n\t\tif x==\"1\":\n\t\t\tnode.mark()\n\t\tif x==\"0\":\n\t\t\tnode.unmark()\n\ttime.sleep(0.01)");
	}
	
	@FXML
	private void example3_2Com() {
		zone.clear();
		zone.replaceSelection("#Receiver\nimport time\nwhile node.loop():\n\tn = node.bufferSize()\n\tif n>0:\n\t\tx = node.read()\n\t\tnode.mark(x)\n\ttime.sleep(0.01)");
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
						Project.getPythonFileFromName(txtLoadFileName.getSelectionModel()
								.getSelectedItem().toString())));
				byte[] bytes = new byte[in.available()];
				in.read(bytes);
				zone.setText(new String(bytes));
				in.close();
			} catch (Exception e1) {}
		} else {			
			txtFileName.setText("");
			zone.setText("while node.loop():\n");
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
				ps = new PrintStream(new FileOutputStream(Project.getPythonFileFromName(Project.getPythonFileExtension(fileName))));
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
		zone.setText("while node.loop():\n"); 
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
