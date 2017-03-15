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
 * This class allows deleting components (sensors, devices or markers) from the map layer
 * 
 * Command examples:
 * delete all
 * -> It deletes all objects (sensors, devices and markers) from the map layer
 * delete all sensors
 * -> It deletes all sensors (sensors, bas stations and media sensors) from the map layer
 * delete all events
 * -> It deletes all events (gases) from the map layer
 * delete all mobiles
 * -> It deletes all mobiles from the map layer
 * delete all markers
 * -> It deletes all markers from the map layer
 * delete all sensors mobiles
 * -> It deletes all defined objects (collection of objects) from the map layer
 * delete selected
 * -> It deletes all selected objects from the map layer
 * delete objects S1 S2 A3
 * -> It deletes all defined nodes (sensors and devices by their names) from the map layer
 * delete markers 0 1
 * -> It deletes all defined markers (by their indices) from the map layer
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
import device.DeviceList;
import map.MapLayer;
import markers.MarkerList;


public class CupCommand_DELETE extends CupCommand {

	protected List<String> objects;
	protected String option = "";
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_DELETE(CupScript script, String option, List<String> objects) {
		this.script = script ;
		this.objects = objects;
		this.option = option;
	}
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_DELETE(CupScript script, String option) {
		this.script = script ;
		this.option = option;	
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute delete
	// First it checks the type of the object(s) to be deleted from the map layer
	// Either it is all objects (nodes and markers)
	// or all the defined object type
	// or only the selected objects
	// or only the defined objects (by their names)
	// or only the defined markers (by their indices)
	// Then, depending on the type of the object, it will delete it from the DeviceList (markerList) and from the map layer as well
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
					DeviceList.deleteAll();
					MarkerList.deleteAll();
					rep = "000 All Objects are Deleted";
					currentExecution = true;
				}
				else {
					for(int i=0; i<sObjects.size();i++) {
						switch (sObjects.get(i)) {
						case ("sensors") :
							DeviceList.deleteAllSensors();
							rep = rep + " 000 All Devices are Deleted";
							currentExecution = true;
							break;
						case ("events") :
							DeviceList.deleteAllNaturalEvents();
							rep = rep + " 000 All Natural Events are Deleted";
							currentExecution = true;
							break;
						case ("mobiles") :
							DeviceList.deleteAllMobiles();
							rep = rep + " 000 All mobiles are Deleted";
							currentExecution = true;
							break;
						case ("markers") :
							MarkerList.deleteAll();
							rep = rep + " 000 All markers are Deleted";
							currentExecution = true;
							break;
						default :
							rep = rep + " 001 [ERROR] error in Deletion";
							currentExecution = false;
						}
					}
				}
				break;
			case ("selected") :
				MapLayer.nodeList.deleteIfSelected();
				MapLayer.markerList.deleteIfSelected();
				MapLayer.buildingList.deleteIfSelected();
				rep = rep + " 000 The Seleted Objects: are Deleted";
				currentExecution = true;
				break;
			case ("objects") : 
				for(int i=0; i<sObjects.size();i++) {
					DeviceList.deleteNodeByName(sObjects.get(i));
					rep = rep + " 000 The Object: " + sObjects.get(i) + " is Deleted";
				}
				currentExecution = true;
				break;
			case ("markers") : 
				for(int i=0; i<sObjects.size();i++) {
					MarkerList.delete(Integer.parseInt(sObjects.get(i)));
					for (int j = i+1; j< sObjects.size(); j++)
						sObjects.set(j, "" + (Integer.parseInt(sObjects.get(j))-1 ));
				}
				rep = " 000 The markers are Deleted";
				currentExecution = true;
				break;
			default :
				rep = "001 [ERROR] error in Deletion";
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
		return "TDELETE";
	}
}
