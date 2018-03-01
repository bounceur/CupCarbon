package cupcarbon_script;

import java.io.IOException;

import map.MapLayer;
import map.WorldMap;

public class SCupCarbon3 {
	
	public static void main(String[] args) throws IOException {		
		CupScript script = new CupScript();
		CupScript.slog.println("Script");
		
		new WorldMap();
		CupScript.slog.println("Init Worls Map");
		
		MapLayer.initLists();
		CupScript.slog.println("Init Object lists");		
		
		CupScriptAddCommand.addCommand(script, "project new test1 /Users/bounceur/ahcene");		
		script.executeCommand();
				
		CupScriptAddCommand.addCommand(script, "set x -4.486016035079956");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "set y 48.39052932411496");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "for i 0 4");
		script.nextAndExecute();

		CupScriptAddCommand.addCommand(script, "add stdsensor $x $y");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "plus x $x 0.001");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "println $x $y");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "endfor");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "save");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "stop");
		script.nextAndExecute();
		
		script.next();
		String st = "";
		while(!(st.startsWith("111"))) {
			st=script.executeCommand();
			script.next();
		}

		System.out.println("BYE");
		CupScript.close();
	}

}
