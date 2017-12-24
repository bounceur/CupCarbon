package cupcarbon;
import java.io.IOException;
import java.io.OutputStream;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class CupCarbonConsole extends OutputStream {
    
	private TextArea textArea ;
	
	public CupCarbonConsole(TextArea textArea) {
		this.textArea = textArea;
	}
	
    @Override
    public void write(int v) throws IOException {
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
				textArea.appendText(String.valueOf((char) v));
			}
    	});
    }

}