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
 * This class allows changing the map displayed on the simulator plateform
 * 
 * Command examples:
 * map 0
 * -> It displays the OSM Light map
 * map 1
 * -> It displays the OSM Dark map
 * map 2
 * -> It displays the First Standard map
 * map 3
 * -> It displays the Second Standard map
 * map 4
 * -> It displays the Third Standard map (the Dark map)
 * map 5
 * -> It displays the Fourth Standard map (the Black map)
 * map 6
 * -> It displays the White map
 * map 7
 * -> It displays the lines map
 * map 8
 * -> It displays the Notebook map
 * map 9
 * -> It displays the Boxes map
 * map 10
 * -> It displays the Google map
 * map 11
 * -> It displays the Satellite Google map
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

import map.WorldMap;


public class CupCommand_MAP extends CupCommand {
	
	protected String mapNumber;
	
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_MAP(CupScript script, String mapNumber) {
		this.script = script ;
		this.mapNumber = mapNumber;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute map
	// It changes the map displayed on the simulator plateform
	// ---------------------------------------------------------------------------------------------------------------------
	@Override
	public String execute() {
		currentExecution = false;
		String rep = "";
		String sMapNumber = script.getVariableValue(mapNumber); 
		if (isExecuted) {
			WorldMap.changeMap(Integer.parseInt(sMapNumber));
			rep = "000 The map has been changed";
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
		return "MAP";
	}
}
