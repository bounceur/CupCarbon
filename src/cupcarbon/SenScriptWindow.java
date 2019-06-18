package cupcarbon;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SenScriptWindow {

	public SenScriptWindow() throws IOException {
		Stage stage = new Stage();
		stage.setTitle("SenScript");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(SenScriptWindow.class.getResource("senscript.fxml"));
		BorderPane panneau = (BorderPane) loader.load();
		Scene scene = new Scene(panneau);
		stage.setScene(scene);		
		//stage.initOwner(CupCarbon.stage);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

}
