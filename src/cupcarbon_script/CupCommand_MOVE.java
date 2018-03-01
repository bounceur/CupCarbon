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
 * This class allows moving an object (sensor, device or marker) on the map layer to a new coordinate (x, y, z)
 * The new coordinate is defined by the defined shift values of x, y, and z
 * 
 * Command examples:
 * move node S1 dx dy dz
 * -> It moves the defined nodes (sensors or devices) the defined shift values to the new coordinates  
 * move marker 1 dx dy dz
 * -> It moves the defined markers (by their indices) the defined shift values to the new coordinates
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

import device.Device;
import device.DeviceList;
import markers.Marker;
import markers.MarkerList;


public class CupCommand_MOVE extends CupCommand {

	protected String type ;
	protected String name ;
	protected String longitudeShift ;
	protected String latitudeShift ;
	protected String elevationShift ;
	
	protected String sType ;
	protected String sName ;
	protected String sLongitudeShift ;
	protected String sLatitudeShift ;
	protected String sElevationShift ;
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_MOVE(CupScript script,  String type, String name, String longitudeShift, String latitudeShift, String elevationShift) {
		this.script = script ;
		this.type = type;
		this.name = name;
		this.longitudeShift = longitudeShift;
		this.latitudeShift = latitudeShift;
		this.elevationShift = elevationShift;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute move
	// First it checks the option
	// Either it will move nodes (sensors or devices)
	// or it will move markers (by index)
	// Then, it will execute the movement according to the defined shift values, to the new coordinates 
	// ---------------------------------------------------------------------------------------------------------------------
	@Override
	public String execute() {
		currentExecution = false;
		String rep = "";
		sType = script.getVariableValue(type);
		sName = script.getVariableValue(name);
		sLongitudeShift = script.getVariableValue(longitudeShift);
		sLatitudeShift = script.getVariableValue(latitudeShift);
		sElevationShift = script.getVariableValue(elevationShift); 
		if (isExecuted) {
			switch (sType) {
			case("node") :
				Device node = DeviceList.getNodeByName(sName);
				if (node != null) {
					node.setLongitude(node.getLongitude() + Double.parseDouble(sLongitudeShift));
					node.setLatitude(node.getLatitude() + Double.parseDouble(sLatitudeShift));
					node.setElevation(node.getElevation() + Double.parseDouble(sElevationShift));
					rep = "000 Moving Object: "+ sName + " to the New Coordinates: " + node.getLongitude() + ", " + node.getLatitude() + ", " + node.getElevation();
					currentExecution = true;
				}
				else {
					rep = "There is no node with the name " + sName + ", try again!";
					currentExecution = true;
				}
				break;
			case ("marker") :
				for (Marker marker: MarkerList.markers) {
					if (Integer.parseInt(sName) > MarkerList.markers.size()-1) {
						rep = "There is no marker with the index " + sName + ", try again!";
						currentExecution = true;
						break;
					}
					if (MarkerList.getIndex(marker) == Integer.parseInt(sName)) {
						marker.setLongitude(marker.getLongitude() + Double.parseDouble(sLatitudeShift));
						marker.setLatitude(marker.getLatitude() + Double.parseDouble(sLatitudeShift));
						marker.setElevation(marker.getElevation() + Double.parseDouble(sLatitudeShift));
						rep = "000 Moving Marker: "+ sName + " to the New Coordinates: " + marker.getLongitude() + ", " + marker.getLatitude() + ", " + marker.getElevation();
					}
				}
				currentExecution = true;
				break;
			default:
				rep = "[ERROR] Unknown type "+sType;
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
		return "move "+sType+" "+sLongitudeShift+" "+sLatitudeShift+" "+sElevationShift;
	}
}
