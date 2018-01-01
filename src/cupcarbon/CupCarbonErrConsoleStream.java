package cupcarbon;
import java.io.IOException;
import java.io.OutputStream;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class CupCarbonErrConsoleStream extends OutputStream {
    
	private TextArea textArea ;
	
	public CupCarbonErrConsoleStream(TextArea textArea) {
		this.textArea = textArea;
	}
	
    @Override
    public void write(int v) throws IOException {
    	Platform.runLater( () -> { 
    			CupCarbon.cupCarbonController.stopSimulation();
    			textArea.appendText(String.valueOf((char) v));
    		}
    	);
    	//Platform.runLater(new Runnable() {
			//@Override
			//public void run() {				
//				CupCarbon.cupCarbonController.stopSimulation();
			//	CupCarbon.cupCarbonController.displayShortErrMessage("ERROR!");
//				textArea.appendText(String.valueOf((char) v));
			//}
    	//});
    }

}