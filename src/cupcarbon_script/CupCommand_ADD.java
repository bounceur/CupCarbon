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
 * This class allows adding components (sensors, devices or markers) on the map layer
 * 
 * Command examples:
 * add stdsensor 0.3 0.2 0.0
 * -> It adds a sensor node to the map layer with the defined coordinates (Longitude, Latitude, elevation)
 * -> It returns the name of the created sensor (ex. S1)
 * add base_station 0.3 0.2 0.0
 * -> It adds a base station to the map layer with the defined coordinates (Longitude, Latitude, elevation)
 * -> It returns the name of the created base station (ex. SINK_2)
 * add directional_sensor 0.3 0.2 0.0
 * -> It adds a directional sensor to the map layer with the defined coordinates (Longitude, Latitude, elevation)
 * -> It returns the name of the created directional sensor (ex. MS3)
 * add mobile 0.3 0.2 0.0
 * -> It adds a mobile to the map layer with the defined coordinates (Longitude, Latitude, elevation)
 * -> It returns the name of the created mobile (ex. M4)
 * add gas 0.3 0.2 0.0
 * -> It adds an event (gas) to the map layer with the defined coordinates (Longitude, Latitude, elevation)
 * -> It returns the name of the created event (gas) (ex. A5)
 * add marker 0.3 0.2 0.0
 * -> It adds a marker to the map layer with the defined coordinates (Longitude, Latitude, elevation)
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

import device.BaseStation;
import device.Device;
import device.DeviceList;
import device.DirectionalSensorNode;
import device.Mobile;
import device.SensorNode;
import device.StdSensorNode;
import markers.Marker;
import markers.MarkerList;
import natural_events.Gas;


public class CupCommand_ADD extends CupCommand {

	protected String type ;
	protected String longitude ;
	protected String latitude ;
	protected String elevation ;
	
	protected String sType ;
	protected String sLongitude ;
	protected String sLatitude ;
	protected String sElevation ;
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_ADD(CupScript script, String type, String longitude, String latitude, String elevation) {
		this.script = script ;
		this.type = type;
		this.longitude = longitude;
		this.latitude = latitude;
		this.elevation = elevation;
	}
	
	//---------------------------------------------------------------------------------------------------------------------
	// execute add
	// First it checks the type of the object to be added to the map layer
	// Either it is a sensor node, base station...
	// Then, depending on the type of the object, it will create it and adding it to the DeviceList (markerList) and to the map layer as well
	// ---------------------------------------------------------------------------------------------------------------------
	@Override
	public String execute() {
		currentExecution = false;
		String rep = "";
		sType = script.getVariableValue(type);
		sLongitude = script.getVariableValue(longitude);
		sLatitude = script.getVariableValue(latitude);
		sElevation = script.getVariableValue(elevation); 
		Device device = null;
		SensorNode sensor = null;
		if (isExecuted) {
			switch (sType) {
			case("stdsensor") :
				sensor = new StdSensorNode(Double.parseDouble(sLongitude), Double.parseDouble(sLatitude), Double.parseDouble(sElevation), 0, 100, 20, -1);
				DeviceList.add(sensor);
				rep = "000 Adding Sensor: Id="+sensor.getId()+", name="+sensor.getName();
				currentExecution = true;
				break;
			case ("base_station") :
				sensor = new BaseStation(Double.parseDouble(sLongitude), Double.parseDouble(sLatitude), Double.parseDouble(sElevation), 0, 100, 20, -1);
				DeviceList.add(sensor);
				rep = "000 Adding Base Station: Id="+sensor.getId()+", name="+sensor.getName();
				currentExecution = true;
				break;
			case ("directional_sensor") :
				sensor = new DirectionalSensorNode(Double.parseDouble(sLongitude), Double.parseDouble(sLatitude), Double.parseDouble(sElevation), 0, 100, 20,-1, 0, 0, 0);
				DeviceList.add(sensor);
				rep = "000 Adding Directional Sensor: Id="+sensor.getId()+", name="+sensor.getName();
				currentExecution = true;
				break;
			case ("mobile") :
				device = new Mobile(Double.parseDouble(sLongitude), Double.parseDouble(sLatitude), Double.parseDouble(sElevation), 0, -1);
				DeviceList.add(device);
				rep = "000 Adding Mobile: Id="+device.getId()+", name="+device.getName();
				currentExecution = true;
				break;
			case ("gas") :
				device = new Gas(Double.parseDouble(sLongitude), Double.parseDouble(sLatitude), Double.parseDouble(sElevation), 0, -1);
				DeviceList.add(device);
				rep = "000 Adding Gas: Id="+device.getId()+", name="+device.getName();
				currentExecution = true;
				break;
			case ("marker") :
				Marker marker = new Marker(Double.parseDouble(sLongitude), Double.parseDouble(sLatitude), Double.parseDouble(sElevation), 0);
				MarkerList.add(marker);
				rep = "000 Adding Marker";
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
		return "add "+sType+" "+sLongitude+" "+sLatitude+" "+sElevation;
	}
}
