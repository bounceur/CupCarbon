package cupcarbon;
import java.io.IOException;
import java.io.OutputStream;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class CupCarbonConsoleStream extends OutputStream {
    
	private TextArea textArea ;
<<<<<<< HEAD
	private int x = 0;
	
	public CupCarbonConsoleStream(TextArea textArea) {
		this.textArea = textArea;
	}
	
    @Override
    public void write(int v) throws IOException {
    	Platform.runLater( () -> {
    			if(v=='\n') {
    				if(x++ > 100) {
        				x=0;
        				textArea.clear();
        			}
    			}
    			
    			textArea.appendText(String.valueOf((char) v));
    		}
    	);
=======
	
	public CupCarbonConsoleStream(TextArea textArea) {
		this.textArea = textArea;
	}
	
    @Override
    public void write(int v) throws IOException {
    	Platform.runLater( () -> 
    		textArea.appendText(String.valueOf((char) v)) 
    			);
>>>>>>> branch 'master' of https://github.com/bounceur/CupCarbon.git
		
    }

}