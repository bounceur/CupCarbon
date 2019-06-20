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
 * This class allows selecting the components (sensors, devices or markers) on the map layer
 * 
 * Command examples:
 * select all
 * -> It selects all objects (sensors, devices and markers) on the map layer
 * select all sensors
 * -> It selects all sensors (sensors, bas stations and directional sensors) on the map layer
 * select all events
 * -> It selects all events (gases) on the map layer
 * select all mobiles
 * -> It selects all mobiles on the map layer
 * select all markers
 * -> It selects all markers on the map layer
 * select all sensorswithoutgps
 * -> It selects all sensors without gps file on the map layer
 * select all sensorswithoutscript
 * -> It selects all sensors without script file on the map layer
 * select inv
 * -> It inverses the selection, i.e. it deselect the selected objects and select the deselected objects
 * select devices S1 S2
 * -> It selects all defined nodes (sensors and devices by their names) on the map layer
 * select markers 0 1 2
 * -> It selects all defined markers (by their indices) on the map layer
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


public class CupCommand_SELECT extends CupCommand {

	protected List<String> objects;
	protected String option = "";
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_SELECT(CupScript script, String option, List<String> objects) {
		this.script = script ;
		this.objects = objects;
		this.option = option;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_SELECT(CupScript script, String option) {
		this.script = script ;
		this.option = option;	
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute select
	// First it checks the type of the object(s) to be selected on the map layer
	// Either it is all objects (nodes and markers)
	// or all the defined object type
	// or inverse the selection
	// or only the defined objects (by their names)
	// or only the defined markers (by their indices)
	// Then, depending on the type of the object, it will select it on the map layer
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
					WorldMap.setSelectionOfAllNodes(true, -1, true);
					WorldMap.setSelectionOfAllMarkers(true, true);
					WorldMap.setSelectionOfAllBuildings(true, true);
					rep = "000 All Objects are Selected";
					currentExecution = true;
				}
				else {
					for(int i=0; i<sObjects.size();i++) {
						switch (sObjects.get(i)) {
						case ("sensors") :
							DeviceList.deselectAllObjects();
							WorldMap.setSelectionOfAllNodes(true, Device.SENSOR, true);
							WorldMap.setSelectionOfAllNodes(true, Device.DIRECTIONAL_SENSOR, true);
							WorldMap.setSelectionOfAllNodes(true, Device.BASE_STATION, true);
							rep = rep + " 000 All Sensors are Selected";
							currentExecution = true;
							break;
						case ("mobiles") :
							DeviceList.deselectAllObjects();
							WorldMap.setSelectionOfAllNodes(true, Device.MOBILE, true);
							rep = rep + " 000 All Mobiles are Selected";
							currentExecution = true;
							break;
						case ("events") :
							DeviceList.deselectAllObjects();
							WorldMap.setSelectionOfAllNodes(true, Device.GAS, true);
							rep = rep + " 000 All Natural Events are Selected";
							currentExecution = true;
							break;
						case ("markers") :
							DeviceList.deselectAllObjects();
							for (int j=0; j< MarkerList.size(); j++)
								WorldMap.setSelectionOfAllMarkers(true, true);
							rep = rep + " 000 All markers are Selected";
							currentExecution = true;
							break;
						case ("sensorswithoutgps") :
							DeviceList.deselectAllObjects();
							DeviceList.selectWitoutGps();
							rep = rep + " 000 All Sensors Without GPS are Selected";
							currentExecution = true;
							break;
						case ("sensorswithoutscript") :
							DeviceList.deselectAllObjects();
							DeviceList.selectWitoutScript();
							rep = rep + " 000 All Sensors Without Script are Selected";
							currentExecution = true;
							break;
						default :
							rep = rep + " 001 [ERROR] error in Selection";
							currentExecution = false;
						}
					}
				}
				break;
			case ("inv") :
				WorldMap.invertSelection();
				rep = "000 The Selection is Inverted";
				currentExecution = true;
				break;
			case ("devices") : 
				DeviceList.deselectAllObjects();
				for(int i=0; i<sObjects.size();i++) {
					DeviceList.getNodeByName(sObjects.get(i)).setSelected(true);
					rep = rep + " 000 The Device: " + sObjects.get(i) + " is selected";
				}
				currentExecution = true;
				break;
			case ("markers") :
				DeviceList.deselectAllObjects();
				for (int i=0; i<sObjects.size();i++) {
					MarkerList.get(Integer.parseInt(sObjects.get(i))).setSelected(true);
					rep = rep + " 000 The marker: " + sObjects.get(i) + " is selected";
				}
				currentExecution = true;
				break;
			default :
				rep = "001 [ERROR] error in Selection";
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
		return "SELECT";
	}
}
