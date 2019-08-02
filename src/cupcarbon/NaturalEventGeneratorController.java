package cupcarbon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import project.Project;

public class NaturalEventGeneratorController implements Initializable{
	
	@FXML
	private TextArea zone;
		
	@FXML
	private ComboBox<String> txtLoadFileName;
	
	@FXML
	private TextField txtFileName;
	
	@FXML
	private TextField meanField;
	
	@FXML
	private TextField stdField;
	
	@FXML
	private TextField sizeField;
	
	@FXML
	private TextField periodField;
	
	@FXML
	public void load() {
		if (txtLoadFileName.getSelectionModel().getSelectedIndex() > 0) {
			txtFileName.setText(txtLoadFileName.getSelectionModel().getSelectedItem().toString());
			zone.setText("");
			try {
				FileInputStream in = new FileInputStream(new File(
						Project.getNatEventFileFromName(txtLoadFileName.getSelectionModel()
								.getSelectedItem().toString())));
				byte[] bytes = new byte[in.available()];
				in.read(bytes);
				zone.setText(new String(bytes));
				in.close();
			} catch (Exception e1) {}
		} else {
			zone.setText("");
			txtFileName.setText("");
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
				ps = new PrintStream(new FileOutputStream(Project.getNatEventFileFromName(Project.getNatEventFileExtension(fileName))));
				ps.print(zone.getText());
				ps.close();
				zone.setText("");
				txtFileName.setText("");

				File scriptFiles = new File(Project.getProjectNatEventPath());
				String[] c = scriptFiles.list();
				txtLoadFileName.getItems().removeAll(txtLoadFileName.getItems());
				txtLoadFileName.getItems().add("New scenario ...");
				for (int i = 0; i < c.length; i++) {
					txtLoadFileName.getItems().add(c[i]);
				}
				
				CupCarbon.cupCarbonController.initScriptGpsEventComboBoxes();
				
//				Alert alert = new Alert(AlertType.INFORMATION);
//				alert.setTitle("Save Natural Event File!");
//				alert.setHeaderText(null);
//				alert.setContentText("File saved !");
//				alert.showAndWait();
				System.out.println("Natural Event File saved.");
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
		File scriptFiles = new File(Project.getProjectNatEventPath());
		String[] c = scriptFiles.list();
		txtLoadFileName.getItems().removeAll(txtLoadFileName.getItems());
		txtLoadFileName.getItems().add("New script ...");
		if (scriptFiles.isDirectory() && c != null) {
			for (int i = 0; i < c.length; i++) {
				txtLoadFileName.getItems().add(c[i]);
			}
		}
	}
	
	@FXML
	public void generate() {
		Platform.runLater(new Runnable() {
			public void run() {
				Random random = new Random();
				zone.setText("time value\n");
				double period = Double.parseDouble(periodField.getText());
				double mean = Double.parseDouble(meanField.getText());
				double std = Double.parseDouble(stdField.getText());
				double v = random.nextGaussian()*std+mean;
				zone.setText(0+" "+ v +"\n");	
				for(int i=1; i< Integer.parseInt(sizeField.getText()); i++) {
					v = random.nextGaussian()*std+mean;
					zone.setText(zone.getText()+period+" "+ v +"\n");			
				}
			}
		});		
	}
}
