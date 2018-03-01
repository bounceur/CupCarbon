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
 * hide details
 * -> It hides the details of the objects (name, id, my) 
 * hide rdistances
 * -> It hides the radio distances between different connected sensors on the map layer
 * hide rmessages
 * -> It hides the radio messages
 * hide mdistances
 * -> It hides the distances between the linked markers
 * hide links
 * -> It hides the radio links between different sensors
 * hide narrows
 * -> It hides the network arrows
 * hide marrows
 * -> It hides the markers arrows
 * hide buildings
 * -> It hides the buildings
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

import buildings.BuildingList;
import map.NetworkParameters;
import map.MapLayer;


public class CupCommand_HIDE extends CupCommand {

	protected String option = "";
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_HIDE(CupScript script, String option) {
		this.script = script ;
		this.option = option;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute hide
	// First it checks the option to be hidden
	// Then, depending on the option, it will execute the hide
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
				rep = "000 The Details have been Hidden";
				currentExecution = true;
				break;
			case ("rdistances") : 
				NetworkParameters.displayRLDistance = !NetworkParameters.displayRLDistance; 
	 			MapLayer.repaint();
	 			rep = "000 The Distances have been Hidden";
				currentExecution = true;
				break;
			case ("rmessages") : 
				NetworkParameters.displayRadioMessages = !NetworkParameters.displayRadioMessages; 
	 			MapLayer.repaint();
	 			rep = "000 The Distances have been Hidden";
				currentExecution = true;
				break;
			case ("mdistances") : 
				NetworkParameters.displayMarkerDistance = !NetworkParameters.displayMarkerDistance; 
	 			MapLayer.repaint();
	 			rep = "000 The Distances have been Hidden";
				currentExecution = true;
				break;
			case ("links") : 
				NetworkParameters.drawRadioLinks = !NetworkParameters.drawRadioLinks; 
				MapLayer.repaint();
				rep = "000 The Links have been Hidden";
				currentExecution = true;
				break;
			case ("narrows") : 
				NetworkParameters.drawSensorArrows = !NetworkParameters.drawSensorArrows;
				MapLayer.repaint();
				rep = "000 The Netowrks Arrows have been Hidden";
				currentExecution = true;
				break;
			case ("marrows") : 
				NetworkParameters.drawMarkerArrows = !NetworkParameters.drawMarkerArrows;
				MapLayer.repaint();
				rep = "000 The Markers Arrows have been Hidden";
				currentExecution = true;
				break;
			case ("buildings") : 
				BuildingList.showHideBuildings();
				MapLayer.repaint();
				rep = "000 The Buildings have been Hidden";
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
		return "HIDE";
	}
}
