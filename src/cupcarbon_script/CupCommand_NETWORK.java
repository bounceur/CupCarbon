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
 * This class allows printing out all components' parameters (sensors, devices, radios and markers) on the map layer
 * 
 * Command examples:
 * network
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

import device.MapObject;


public class CupCommand_NETWORK extends CupCommand {
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_NETWORK(CupScript script) {
		this.script = script ;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute netowrk
	// It prints out all components' parameters (sensors, devices, radios  and markers) on the map layer
	// ---------------------------------------------------------------------------------------------------------------------
	@Override
	public String execute() {
		currentExecution = false;
		String rep = "";
		if (isExecuted) {
			MapObject.listNodesParameters();
			MapObject.listRadioParameters();
			MapObject.listMarkerParameters();
			rep = "000 The parameters have been Printed";
			currentExecution = true;
		}
		else 
			rep = "001 [ERROR] Verify the Script";
		System.out.println(rep);
		CupScript.slog.println(rep);
		isExecuted = currentExecution;
		return rep;
	}
	
	@Override
	public String toString() {
		return "NETWORK";
	}
}
