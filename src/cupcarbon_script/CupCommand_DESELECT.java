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
 * This class allows deselecting the components (sensors, devices or markers) on the map layer
 * 
 * Command examples:
 * deselect all
 * -> It deselects all objects (sensors, devices and markers) on the map layer
 * deselect all sensors
 * -> It deselects all sensors (sensors, bas stations and directional sensors) on the map layer
 * deselect all events
 * -> It deselects all events (gases) on the map layer
 * deselect all mobiles
 * -> It deselects all mobiles on the map layer
 * deselect all markers
 * -> It deselects all markers on the map layer
 * deselect devices S1 S2
 * -> It deselects all defined nodes (sensors and devices by their names) on the map layer
 * deselect markers 0 1 2
 *  * -> It deselects all defined markers (by their indices) on the map layer
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;
import device.Device;
import device.DeviceList;
import map.WorldMap;
import markers.MarkerList;


public class CupCommand_DESELECT extends CupCommand {

	protected List<String> objects;
	protected String option = "";
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_DESELECT(CupScript script, String option, List<String>objects) {
		this.script = script ;
		this.objects = objects;
		this.option = option;
	}

	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_DESELECT(CupScript script, String option) {
		this.script = script ;
		this.option = option;	
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute deselect
	// First it checks the type of the object(s) to be deselected on the map layer
	// Either it is all objects (nodes and markers)
	// or all the defined object type
	// or only the defined objects (by their names)
	// or only the defined markers (by their indices)
	// Then, depending on the type of the object, it will deselect it on the map layer
	// ---------------------------------------------------------------------------------------------------------------------
	@Override
	public String execute() {
		currentExecution = false;
		String rep = "";
		String sOption = script.getVariableValue(option);
		List<String> sObjects = new ArrayList<String>();
		if (objects != null)
			for(int i=0; i<objects.size(); i++)
				sObjects.add(script.getVariableValue(objects.get(i)));
		if (isExecuted) {
			switch(sOption) {
			case ("all") :
				if (sObjects.size() == 0) {
					WorldMap.setSelectionOfAllNodes(false, -1, false);
					WorldMap.setSelectionOfAllMarkers(false, true);
					WorldMap.setSelectionOfAllBuildings(false, false);
					rep = "000 All Objects are Deselected";
					currentExecution = true;
				}
				else {
					for(int i=0; i<sObjects.size();i++) {
						switch (sObjects.get(i)) {
						case ("sensors") :
							WorldMap.setSelectionOfAllNodes(false, Device.SENSOR, true);
							WorldMap.setSelectionOfAllNodes(false, Device.DIRECTIONAL_SENSOR, true);
							WorldMap.setSelectionOfAllNodes(false, Device.BASE_STATION, true);
							rep = rep + " 000 All Sensors are Deselected";
							currentExecution = true;
							break;
						case ("mobiles") :
							WorldMap.setSelectionOfAllNodes(false, Device.MOBILE, true);
							rep = rep + " 000 All Mobiles are Deselected";
							currentExecution = true;
							break;
						case ("events") :
							WorldMap.setSelectionOfAllNodes(false, Device.GAS, true);
							rep = rep + " 000 All Natural Events are Deselected";
							currentExecution = true;
							break;
						case ("markers") :
							WorldMap.setSelectionOfAllMarkers(false, true);
							rep = rep + " 000 All markers are Deselected";
							currentExecution = true;
							break;
						default :
							rep = rep + " 001 [ERROR] error in Deselection";
							currentExecution = false;
						}
					}
				}
				break;
			case ("devices") : 
				for(int i=0; i<sObjects.size();i++) {
					DeviceList.getNodeByName(sObjects.get(i)).setSelected(false);
					rep = rep + " 000 The Device : " + sObjects.get(i) + " is deselected";
				}
				currentExecution = true;
				break;
			case ("markers") :
				for (int i=0; i<sObjects.size();i++) {
					MarkerList.get(Integer.parseInt(sObjects.get(i))).setSelected(false);
					rep = rep + " 000 The marker: " + sObjects.get(i) + " is deselected";
				}
				currentExecution = true;
				break;
			default :
				rep = "001 [ERROR] error in Deselection";
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
		return "DESELECT";
	}
}
