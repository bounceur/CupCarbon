package cupcarbon;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MqttWindow {

	public MqttWindow() throws IOException {
		Stage stage = new Stage();
		stage.setTitle("MQTT Configuration");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MqttWindow.class.getResource("mqtt.fxml"));
		BorderPane panneau = (BorderPane) loader.load();
		Scene scene = new Scene(panneau);
		stage.setScene(scene);		
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

}
