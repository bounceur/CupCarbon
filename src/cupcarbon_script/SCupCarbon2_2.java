package cupcarbon_script;

import java.io.IOException;

import map.MapLayer;
import map.WorldMap;

public class SCupCarbon2_2 {
	
	public static void main(String[] args) throws IOException {
		CupScript script = new CupScript();
		CupScript.slog.println("Script");
		
		new WorldMap();
		CupScript.slog.println("Init Worls Map");
		
		MapLayer.initLists();
		CupScript.slog.println("Init Object lists");		
		
		CupScriptAddCommand.addCommand(script, "project open test1 /Users/bounceur/ahcene");		
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "add stdsensor -4.485016035079956 48.39052932411496 ");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "add stdsensor -4.484016035079955 48.39052932411496 ");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "add stdsensor -4.483016035079955 48.39052932411496 ");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "add stdsensor -4.482016035079955 48.39052932411496 ");
		script.nextAndExecute();
				
		CupScriptAddCommand.addCommand(script, "save");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "stop");
		script.nextAndExecute();

		System.out.println("BYE");
		CupScript.close();
	}

}
