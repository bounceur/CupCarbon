/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: A Smart City & IoT Wireless Sensor Network Simulator
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013-2017 CupCarbon
 * ----------------------------------------------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------
 * CupCarbon U-One is part of the research project PERSEPTEUR supported by the 
 * French Agence Nationale de la Recherche ANR 
 * under the reference ANR-14-CE24-0017-01. 
 * ----------------------------------------------------------------------------------------------------------------
 **/
 
/** Definition:
 * This class allows executing a cupCarbonScript file to execute the list of defined cupSarbonScript commands
 * 
 * Command examples:
 * file open test1 /Users/ahcenebounceur/Desktop
 * -> by defining the name of the file and its path
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import map.MapLayer;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */
public class CupCommand_FILE extends CupCommand {

	protected String path = "";
	protected String name = "";
	protected String option = "";
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_FILE(CupScript script, String option, String name, String path) {
		this.script = script ;
		this.path = path ;
		if(!path.endsWith(File.separator))
			path += File.separator;
		this.path = path + name;
		this.name = name ;
		this.option = option;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute file
	// First it looks for the file name and its path
	//	Then, it will load the file and execute the included commands
	// ---------------------------------------------------------------------------------------------------------------------
	@Override
	public String execute() {
		currentExecution = false;
		String rep = "";
		String sPath = script.getVariableValue(path);
		String sName = script.getVariableValue(name);
		String sOption = script.getVariableValue(option);
		CupScript.slog.println("Script");
		switch(sOption) {
		case ("open") : 
			if (!sName.endsWith(".scc")) 
				sName+=".scc";
	    loadAndExecute(script, sPath, sName);
			rep = "000 Script File opened";
			currentExecution = true;
			break;
		default :
			rep = "001 [ERROR] No file or Directory";
			currentExecution = false;
		}
		System.out.println(rep);
		CupScript.slog.println(rep);
		isExecuted = currentExecution;
		return rep;
	}
	
	public static void loadAndExecute(CupScript script, String path, String fileName) {
		CupScript.slog.println("Script");
		
		MapLayer.initLists();
		CupScript.slog.println("Init Object lists");		
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String s = "";
			while ((s = br.readLine()) != null) {	
				CupScriptAddCommand.addCommand(script, s.trim());
				script.nextAndExecute();
			}
			script.next();
			String st = "";
			while(!(st.startsWith("111"))) {
				st=script.executeCommand();
				script.next();
				MapLayer.repaint();
			}
			br.close();
			System.out.println("BYE");
			CupScript.close();
		} catch (Exception e) {e.printStackTrace();}
		
	}


	@Override
	public String toString() {
		return "FILE";
	}
}
