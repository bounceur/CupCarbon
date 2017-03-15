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

import cupcarbon.CupCarbon;
import device.DeviceList;
import device.SensorNode;
import javafx.scene.paint.Color;
import map.MapLayer;

/**
 * @author Ahcene Bounceur
 * @version 1
 */

public class VisibilityLauncher extends Thread {

	@Override
	public void run() {

		//-----------------------------------------------------------------------------------------
		// Switch on the led (on the ihm) to show that the process of calculating the 
		// visibility is started
		//-----------------------------------------------------------------------------------------
		CupCarbon.cupCarbonController.monitor.setFill(Color.ORANGERED);
		CupCarbon.cupCarbonController.stateLabel.setText("Calculating ...");

		//-----------------------------------------------------------------------------------------
		// Calculate the visiblity zone for each selected sensor node
		//-----------------------------------------------------------------------------------------
		for (SensorNode sensor : DeviceList.sensors) {
			if (sensor.isSelected()) {
				VisibilityZones vz = new VisibilityZones(sensor);
				vz.start();
			}
			
		}
		
		//-----------------------------------------------------------------------------------------
		// Onces the visibility calculation process is finished, if the mode of the propagation 
		// is activated, we recalculate the propagation 
		//-----------------------------------------------------------------------------------------
		if (DeviceList.propagationsCalculated)
			DeviceList.calculatePropagations();
		MapLayer.repaint();

		//-----------------------------------------------------------------------------------------
		// Switch off the led (on the ihm) to show that the process of calculating the 
		// visibility is finished
		//-----------------------------------------------------------------------------------------
		CupCarbon.cupCarbonController.monitor.setFill(Color.YELLOWGREEN);
		CupCarbon.cupCarbonController.stateLabel.setText("Ready");
		CupCarbon.cupCarbonController.mapFocus();

	}

}
