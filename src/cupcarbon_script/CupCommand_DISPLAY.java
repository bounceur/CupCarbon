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
 * This class allows displaying the details linked to the components on the map layer
 * 
 * Command examples:
 * display details
 * -> It displays the details of the devices (name, id, my) 
 * display rdistances
 * -> It displays the radio distances between different connected sensors on the map layer
 * display rmessages
 * -> It displays the radio messages
 * display mdistances
 * -> It displays the distances between the linked markers
 * display links
 * -> It displays the radio links between different sensors
 * display narrows
 * -> It displays the network arrows
 * display marrows
 * -> It displays the markers arrows
 * display buildings
 * -> It displays the buildings
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

import buildings.BuildingList;
import device.Device;
import device.DeviceList;
import device.SensorNode;
import map.NetworkParameters;
import map.MapLayer;


public class CupCommand_DISPLAY extends CupCommand {

	protected String option = "";
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_DISPLAY(CupScript script, String option) {
		this.script = script ;
		this.option = option;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute display
	// First it checks the option to be displayed
	// Then, depending on the option, it will execute the display
	// ---------------------------------------------------------------------------------------------------------------------
	@Override
	public String execute() {
		currentExecution = false;
		String rep = "";
		String sOption = script.getVariableValue(option);
		if (isExecuted) {
			switch(sOption) {
			case ("details") : 
				NetworkParameters.displayDetails = !NetworkParameters.displayDetails;
				MapLayer.repaint();
				rep = "000 The Details have been Shown";
				currentExecution = true;
				break;
			case ("rdistances") : 
				NetworkParameters.displayRLDistance = !NetworkParameters.displayRLDistance; 
	 			MapLayer.repaint();
	 			rep = "000 The Distances have been Shown";
				currentExecution = true;
				break;
			case ("rmessages") : 
				NetworkParameters.displayRadioMessages = !NetworkParameters.displayRadioMessages; 
	 			MapLayer.repaint();
	 			rep = "000 The Distances have been Shown";
				currentExecution = true;
				break;
			case ("mdistances") : 
				NetworkParameters.displayMarkerDistance = !NetworkParameters.displayMarkerDistance; 
	 			MapLayer.repaint();
	 			rep = "000 The Distances have been Shown";
				currentExecution = true;
				break;
			case ("links") : 
				NetworkParameters.drawRadioLinks = !NetworkParameters.drawRadioLinks; 
				MapLayer.repaint();
				rep = "000 The Links have been Shown";
				currentExecution = true;
				break;
			case ("narrows") : 
				NetworkParameters.drawSensorArrows = !NetworkParameters.drawSensorArrows;
				MapLayer.repaint();
				rep = "000 The Netowrks Arrows have been Shown";
				currentExecution = true;
				break;
			case ("marrows") : 
				NetworkParameters.drawMarkerArrows = !NetworkParameters.drawMarkerArrows;
				MapLayer.repaint();
				rep = "000 The Markes Arrows have been Shown";
				currentExecution = true;
				break;
			case ("nodes") :
				for (SensorNode d : DeviceList.sensors) {
					d.setHide(1);
				}
				for (Device d : DeviceList.devices) {
					d.setHide(1);
				}
				NetworkParameters.drawRadioLinks = false;
				MapLayer.repaint();
				rep = "000 The nodes Only will be Shown";
				currentExecution = true;
				break;
			case ("buildings") : 
				BuildingList.showHideBuildings();
				MapLayer.repaint();
				rep = "000 The Buildings have been Shown";
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
		return "DISPLAY";
	}
}
