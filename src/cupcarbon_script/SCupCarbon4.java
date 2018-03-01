package cupcarbon_script;

import java.io.IOException;

import map.MapLayer;
import map.WorldMap;

public class SCupCarbon4 {
	
	public static void main(String[] args) throws IOException {	
		CupScript script = new CupScript();
		
		new WorldMap();
		CupScript.slog.println("Init Worls Map");
		
		MapLayer.initLists();
		CupScript.slog.println("Init Object lists");
		
		CupScriptAddCommand.addCommand(script, "for i 0 4");
		script.nextAndExecute();

		CupScriptAddCommand.addCommand(script, "println $i");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "endfor");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "stop");
		script.nextAndExecute();
		
		String st = "";
		script.next();
		while(!(st.startsWith("111"))) {
			st=script.executeCommand();
			script.next();
		}

		System.out.println("BYE");
		CupScript.close();

	}

}
