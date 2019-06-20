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
 * This class allows dealing with the radio module of a sensor (sensor node, directional sensor or base station)
 * by adding a radio module to a sensor, selecting a radio to be the current radio for the sensor, or deleting a radio module from a sensor
 * 
 * Command examples:
 * radio add S1 radio2 WIFI
 * -> It adds radio2 with the standard WIFI to the sensor S1
 * radio current S1 radio1
 * -> It makes radio1 for the sensor S1 as the current radio module
 * radio delete S1 radio1
 * -> It deletes the radio1 from the sensor S1
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

import cupcarbon.CupCarbon;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

import device.DeviceList;
import device.SensorNode;
import radio_module.RadioModule;


public class CupCommand_RADIO extends CupCommand {

	protected String sensor ;
	protected String radioName = "";
	protected String standard = "";
	protected String option = "";
	
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_RADIO(CupScript script,String option, String sensor, String radioName, String standard) {
		this.script = script ;
		this.option = option ;
		this.sensor = sensor ;
		this.radioName = radioName ;
		this.standard = standard;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute radio
	// It adds a radio module to a sensor
	// or selects a radio to be the current radio for the sensor
	// or deletes a radio module from a sensor 
	// First it checks the option, either it is add, current or delete
	// In case the option is add
	// It verify if the defined sensor node exists
	// Then to verify the name of the radio if it is correct, not exist before and is between radio1 & radio10
	// Then check the standard if it is predefined
	// Then it execute adding the new radio module with the standard to the defined sensor
	// ---------------------------------------------------------------------------------------------------------------------
	@Override
	public String execute() {
		currentExecution = false;
		String rep = "";
		String sOption = script.getVariableValue(option);
		String sName = script.getVariableValue(radioName);
		String sStandard = script.getVariableValue(standard);
		String sSensor = script.getVariableValue(sensor);
		SensorNode sensor = (SensorNode) DeviceList.getNodeByName(sSensor);
		int number = Integer.parseInt(sName.replaceAll("[^0-9?!\\.]",""));
		if (isExecuted) {
			switch(option) {
			case ("add") :
				if (sensor == null) {
					rep  = "There is no sensor with the name " + sSensor + ", try again!";
					currentExecution = true;
				}
				else if ((sName.substring(0, 5).equals("radio") || sName.substring(0, 5).equals("RADIO")) && (number > 0 && number <= 10) && (RadioModule.getStandardByName(sStandard) > 0) && (!sensor.getRadioModuleList().contains(sensor.getRadioModuleByName(sName)))) {
						sensor.addRadioModule(sName, sStandard);	
						rep = "000 Adding Radio module: "+sName+" Standard: "+sStandard+" to the Device " + sSensor;
						
						if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
							CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
						if (CupCarbon.cupCarbonController != null) {
							CupCarbon.cupCarbonController.updateObjectListView();
							CupCarbon.cupCarbonController.getNodeInformations();
							CupCarbon.cupCarbonController.getRadioInformations();
							CupCarbon.cupCarbonController.updateSelectionInListView();
						}
						DeviceList.deselectAll();
						sensor.setSelected(true);
						currentExecution = true;
				}
				else {
					rep = "[ERROR] Unknown Radio Name or Standard" ;
					currentExecution = false;
				}
				break;
			case ("current") : 
				sensor.selectCurrentRadioModule(sName);
				rep = "000 Radio module "+sName+" is selected for the device " + sSensor;
				currentExecution = true;
				if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
					CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
				if (CupCarbon.cupCarbonController != null) {
					CupCarbon.cupCarbonController.updateObjectListView();
					CupCarbon.cupCarbonController.getNodeInformations();
					CupCarbon.cupCarbonController.getRadioInformations();
					CupCarbon.cupCarbonController.updateSelectionInListView();
				}
				DeviceList.deselectAll();
				sensor.setSelected(true);
				break;
			case ("delete") : 
				sensor.removeRadioModule(sName);
				rep = "000 Radio module "+sName+" is deleted from the device " + sSensor;
				currentExecution = true;
				if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
					CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
				if (CupCarbon.cupCarbonController != null) {
					CupCarbon.cupCarbonController.updateObjectListView();
					CupCarbon.cupCarbonController.getNodeInformations();
					CupCarbon.cupCarbonController.getRadioInformations();
					CupCarbon.cupCarbonController.updateSelectionInListView();
				}
				DeviceList.deselectAll();
				sensor.setSelected(true);
				break;
			default :
				rep = "001 [ERROR] Radio option error: "+sOption;
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
		return "RADIO";
	}
}
