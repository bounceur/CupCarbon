package cupcarbon;

import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class ConsoleController implements Initializable {

	public static ConsoleController controller;
	
	@FXML
	public TextArea textOut;
	
	@FXML
	public TextArea textErr;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		CupCarbonConsoleStream consoleOut = new CupCarbonConsoleStream(textOut);
		PrintStream ps1 = new PrintStream(consoleOut, true);
		System.setOut(ps1);
		
		CupCarbonErrConsoleStream consoleErr = new CupCarbonErrConsoleStream(textErr);
		PrintStream ps2 = new PrintStream(consoleErr, true);
		System.setErr(ps2);

		controller = this;
		
	}
	
	public void toFront() {
		this.toFront();
	}
	
	public void clear() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				textOut.clear();
				textErr.clear();	
			}
		});
	}

}
