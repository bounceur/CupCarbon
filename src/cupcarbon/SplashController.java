package cupcarbon;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class SplashController implements Initializable {
	
	@FXML
	public ProgressBar progress;
	
	@FXML
	public void close() {
		Stage stage = (Stage) progress.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		Platform.runLater(new Runnable() {
//			public void run() {
//				try {
//					Thread.sleep(3000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				close();
//			}
//		});
	}
}
