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
 * Definition:
 * This class allows to send messages by a sensor
 * 
 * Command examples:
 * send A 4
 * -> The current sensor will send a message "A" to the sensor having an id = 4
 * send A
 * -> The current sensor will send a message "A" in a broadcast
 * send A *
 * -> The same than send A
 * send A * 3
 * -> The current sensor will send a message "A" in a broadcast mode except for the sensor having an id = 3
 * ----------------------------------------------------------------------------------------------------------------
 **/

package senscript;

import device.SensorNode;
import simulation.WisenSimulation;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 *
 */

public class Command_SCOLOR extends Command {
	
	protected String arg = "";
	
	public Command_SCOLOR(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {		
		Double v = Double.parseDouble(sensor.getScript().getVariableValue(arg));
		
		WisenSimulation.simLog.add("S" + sensor.getId() + " SCOLOR "+v);
		
		sensor.setRadioLinkColor(UColor.colorTab2[v.intValue()]);
		return 0;
	}
			
}