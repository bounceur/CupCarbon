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
 * This Class calls the class VisibilityZones for each selected sensor node in order
 * to calculate a simple visibility based on a 2D environment 
 **/

package visibility;

import device.DeviceList;
import device.SensorNode;

/**
 * @author Ahcene Bounceur
 * @version 2
 */

public class VisibilityLauncher {

	public static void calculate() {
		//-----------------------------------------------------------------------------------------
		// Calculate the visiblity zone for each sensor node
		//-----------------------------------------------------------------------------------------
		for (SensorNode sensor : DeviceList.sensors) {
			VisibilityZones vz = new VisibilityZones(sensor);
			vz.start();
		}
	}
	
	public static void calculateForSelected() {
		//-----------------------------------------------------------------------------------------
		// Calculate the visiblity zone for each selected sensor node
		//-----------------------------------------------------------------------------------------
		for (SensorNode sensor : DeviceList.sensors) {
			if (sensor.isSelected()) {
				VisibilityZones vz = new VisibilityZones(sensor);
				vz.start();
			}
			
		}
	}
	
	public static void calculate(SensorNode sensor) {
		//-----------------------------------------------------------------------------------------
		// Calculate the visiblity zone for a given sensor node
		//-----------------------------------------------------------------------------------------
		VisibilityZones vz = new VisibilityZones(sensor);
		vz.start();
	}

}
