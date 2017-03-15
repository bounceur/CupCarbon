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
 * This class allows inserting objects (principally markers) after specific markers to the map layer
 * 
 * Command examples:
 * insert marker selected
 * -> It inserts new marker after each selected marker 
 * insert marker 0 1 2
 * -> It inserts new marker after each defined marker (by index)
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
import markers.Marker;
import markers.MarkerList;


public class CupCommand_INSERT extends CupCommand {

	protected List<String> objects;
	protected String option = "";
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_INSERT(CupScript script, String option, List<String> objects) {
		this.script = script ;
		this.objects = objects;
		this.option = option;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute insert
	// First it checks the objects
	// Either it will insert after the selected markers
	// or it will insert after the defined markers (by index)
	// Then, it will insert new marker after each selected (defined) marker
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
			case ("marker") : 
				if ((sObjects.size() == 1) && (sObjects.get(0).equals("selected"))) {
					Marker marker;
					List<Marker> addedMarkers = new ArrayList<Marker>();
					for (Iterator<Marker> iterator = MarkerList.markers.iterator(); iterator.hasNext();) {
						marker = iterator.next();
						if (marker.isSelected()) {
							addedMarkers.add(marker); 
						}
					}
					for (Iterator<Marker> iterator = addedMarkers.iterator(); iterator.hasNext();) {
						marker = iterator.next();
						marker.insertAfterSelectedMarker();
					}
					rep = "000 markers are inserted";
				}
				else {
					for(int i=0; i<objects.size();i++) {
						MarkerList.markers.get(Integer.parseInt(objects.get(i))).insertInAll();
						for (int j = i+1; j< objects.size(); j++)
							objects.set(j, "" + (Integer.parseInt(objects.get(j))+1 ));
					}
					rep = "000 markers are inserted";
				}
				currentExecution = true;
				break;
			default :
				rep = "001 [ERROR] No such marker";
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
		return "INSERT";
	}
}
