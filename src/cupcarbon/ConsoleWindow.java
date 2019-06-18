package cupcarbon;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConsoleWindow {
	
	Stage stage;
	
	public ConsoleWindow() throws IOException {
		stage = new Stage();
		stage.setTitle("Console");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(SenScriptWindow.class.getResource("console.fxml"));
		AnchorPane panneau = (AnchorPane) loader.load();
		Scene scene = new Scene(panneau);
		stage.setScene(scene);		
		stage.initOwner(CupCarbon.stage);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
		stage.setX(0);
		stage.setY(0);
		//stage.toFront();
		//stage.showAndWait();
	}
	
	public void toFront() {
		stage.toFront();
	}
	
	
}