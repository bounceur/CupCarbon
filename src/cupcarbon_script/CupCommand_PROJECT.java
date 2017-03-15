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
 * This class allows dealing with the project
 * by creating new project, opening a project or print out the current project parameters
 * 
 * Command examples:
 * project new test1 /Users/ahcenebounceur/Desktop
 * -> by defining the name of the new project and its path
 * project open test2 /Users/ahcenebounceur/Desktop
 * -> by defining the name of the project and its path
 * project list
 * -> by printing out the parameters of the current project
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

import java.io.File;

import project.Project;


public class CupCommand_PROJECT extends CupCommand {

	protected String path = "";
	protected String name = "";
	protected String option = "";
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_PROJECT(CupScript script, String option, String name, String path) {
		this.script = script ;
		this.path = path ;
		if(!path.endsWith(File.separator))
			path += File.separator;
		this.path = path + name;
		this.name = name ;
		this.option = option;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_PROJECT(CupScript script, String option) {
		this.script = script ;	
		this.option = option;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute project
	// First it looks for option, the file name and its path
	//	Then, it will either create new project, open an existing project or list the current project parameters
	// ---------------------------------------------------------------------------------------------------------------------
	@Override
	public String execute() {
		currentExecution = false;
		String rep = "";
		String sPath = script.getVariableValue(path);
		String sName = script.getVariableValue(name);
		String sOption = script.getVariableValue(option);
		switch(option) {
		case ("new") :
			Project.newProject(sPath, sName, true);
			rep = "000 New project is created";
			currentExecution = true;
			break;
		case ("open") : 
			if(!sName.endsWith(".cup")) sName+=".cup";
			Project.openProject(sPath, sName);
			rep = "000 Project opened";
			currentExecution = true;
			break;
		case ("list"):
			Project.listParameters();
			rep = "000 Project Paramaters Have been Printed Out";
			currentExecution = true;
			break;
		default :
			rep = "001 [ERROR] Project option error: "+sOption;
			currentExecution = false;
		}
		System.out.println(rep);
		CupScript.slog.println(rep);
		isExecuted = currentExecution;
		return rep;
	}

	@Override
	public String toString() {
		return "PROJECT";
	}
}
