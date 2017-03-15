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
 * This class allows duplicating components (sensors or devices) on the map layer
 * 
 * Command examples:
 * duplicate selected
 * -> It duplicates the selected objects (sensors or devices) on the map layer
 * duplicate objects S1 MS2 A3
 * -> It duplicates the defined objects (sensors or devices) on the map layer
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import device.Device;
import device.DeviceList;
import device.MapObject;
import device.SensorNode;


public class CupCommand_DUPLICATE extends CupCommand {

	protected List<String> objects;
	protected String option = "";
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_DUPLICATE(CupScript script, String option, List<String> objects) {
		this.script = script ;
		this.objects = objects;
		this.option = option;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_DUPLICATE(CupScript script, String option) {
		this.script = script ;
		this.option = option;	
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute duplicate
	// First it checks the option to be duplicated
	// Either it is the selecetd objects (sensors or devices)
	// or the defined objects (sensors or devices)
	// Then, depending on the option, it will duplicate the objects on the map layer as well
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
			case ("selected") :
				List<SensorNode> addedSensors = new ArrayList<SensorNode>();
				for (SensorNode sensor : DeviceList.sensors) {
					if (sensor.isSelected()) {
						addedSensors.add(sensor);
					}
				}
				SensorNode duplicatedSensor;
				for (Iterator<SensorNode> iterator = addedSensors.iterator(); iterator.hasNext();) {
					duplicatedSensor = iterator.next();
					DeviceList.add((SensorNode) duplicatedSensor.duplicateWithShift(0.0002,0.0002,0));
				}
				List<Device> addedDevices = new ArrayList<Device>();
				for (Device device : DeviceList.devices) {
					if (device.isSelected()) {
						addedDevices.add(device);
					}
				}
				Device duplicatedDevice;
				for (Iterator<Device> iterator = addedDevices.iterator(); iterator.hasNext();) {
					duplicatedDevice = iterator.next();
					DeviceList.add(duplicatedDevice.duplicateWithShift(0.0002,0.0002,0));
				}
				rep = rep + " 000 The Seleted Objects: have been Duplicated";
				currentExecution = true;
				break;
			case ("devices") : 
				for(int i=0; i<sObjects.size();i++) {
					if ((DeviceList.getNodeByName(sObjects.get(i)).getType() == MapObject.GAS) || (DeviceList.getNodeByName(sObjects.get(i)).getType() == MapObject.MOBILE))
						DeviceList.add(DeviceList.getNodeByName(sObjects.get(i)).duplicateWithShift(0.0002,0.0002,0));
					else
						DeviceList.add((SensorNode) DeviceList.getNodeByName(sObjects.get(i)).duplicateWithShift(0.0002,0.0002,0));
					rep = rep + " 000 The Device: " + sObjects.get(i) + " has been Duplicated";
				}
				currentExecution = true;
				break;
			default :
				rep = "001 [ERROR] option error: "+sOption;
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
		return "DUPLICATE";
	}
}
