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
 * This class allows dealing with the object Buildings on the map layer
 * By loading the buildings, delete all buildings select or deselect all buildings 
 * Command examples:
 * buildings load
 * -> It loads all buildings between two markers on the map layer
 * buildings delete
 * -> It deletes all buildings from the map layer
 * buildings select
 * -> It select all building on the map layer
 * buildings deselect
 * -> It deselect all building on the map layer
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

import device.DeviceList;
import buildings.BuildingList;
import map.WorldMap;


public class CupCommand_BUILDINGS extends CupCommand {

	protected String option = "";
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_BUILDINGS(CupScript script, String option) {
		this.script = script ;
		this.option = option;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute buildings
	// First it checks the option to be executed for the building objects
	// Either it will load the buildings between two markers 
	// or delete all the buildings from the map layer
	// or select /deselect the buildings on the map layer
	// ---------------------------------------------------------------------------------------------------------------------
	@Override
	public String execute() {
		currentExecution = false;
		String rep = "";
		String sOption = script.getVariableValue(option);
		if (isExecuted) {
			switch(sOption) {
			case ("load") : 
				BuildingList.loadFromOsm();
				rep = " 000 The Buildings are have been loaded";
				currentExecution = true;
				break;
			case ("delete") :
				DeviceList.deleteAllBuildings();
				rep = " 000 All Buildings are Deleted";
				currentExecution = true;
				break;
			case ("select") :
				WorldMap.setSelectionOfAllBuildings(true, true);
				rep = " 000 All Buildings are Selected";
				currentExecution = true;
				break;
			case ("deselect") :
				WorldMap.setSelectionOfAllBuildings(false, true);
				rep = " 000 All Buildings are Deselected";
				currentExecution = true;
				break;
			default :
				rep = "001 [ERROR]";
				currentExecution = false;
			}
		}
		else {
			rep = "ERROR IN EXECUTING THE PREVIOUS STEP, THE SCRIPT WILL NOT CONTINUE TO EXECUTE";
			script.setBreaked(true);
		}
		
		System.out.println(rep);
		CupScript.slog.println(rep);
		isExecuted = currentExecution;
		return rep;
	}

	@Override
	public String toString() {
		return "BUILDINGS";
	}
}
