package cupcarbon_script;

import java.io.IOException;

import map.MapLayer;
import map.WorldMap;

public class SCupCarbon2_1 {
	
	public static void main(String[] args) throws IOException {
		CupScript script = new CupScript();
		CupScript.slog.println("Script");
		
		new WorldMap();
		CupScript.slog.println("Init Worls Map");
		
		MapLayer.initLists();
		CupScript.slog.println("Init Object lists");		
		
		CupScriptAddCommand.addCommand(script, "project new ahcene10 /Users/ahcenebounceur/Desktop");		
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "add stdsensor -4.485016035079956 48.39052932411496");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "add stdsensor -4.484016035079955 48.39052932411496");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "add stdsensor -4.483016035079955 48.39052932411496");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "add gas -4.482016035079955 48.39052932411496");
		script.nextAndExecute();
				
		CupScriptAddCommand.addCommand(script, "save");
		script.nextAndExecute();
		
		CupScriptAddCommand.addCommand(script, "stop");
		script.nextAndExecute();

		System.out.println("BYE");
		CupScript.close();
	}

}



//project open ahcene10 /Users/ahcenebounceur/Desktop

//project new ahcene10 /Users/ahcenebounceur/Desktop		
//add stdsensor -4.485016035079956 48.39052932411496
//add stdsensor -4.484016035079955 48.39052932411496
//add stdsensor -4.483016035079955 48.39052932411496
//add gas -4.482016035079955 48.39052932411496"
//save