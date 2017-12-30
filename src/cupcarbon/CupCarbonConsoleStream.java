package cupcarbon;
import java.io.IOException;
import java.io.OutputStream;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class CupCarbonConsoleStream extends OutputStream {
    
	private TextArea textArea ;
	
	public CupCarbonConsoleStream(TextArea textArea) {
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