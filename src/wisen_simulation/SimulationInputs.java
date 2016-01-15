/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2014 Ahcene Bounceur
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
 *----------------------------------------------------------------------------------------------------------------*/

package wisen_simulation;

import propagation.RadioDetection;
import radio.Ber;

/**
 * @author Ahcene Bounceur
 * @author Massinissa Lounis
 * @version 1.0
 * 
 * The necessary parameters for the simulation
 */
public class SimulationInputs {
	
	public static final int NONE = 0;
	public static final int CSMA = 1;
	
	public static boolean cpuDrift = false;
	
	public static boolean mobility = false;
	public static int iterNumber = 10000;
	
	public static int visualDelay = 10;
	public static boolean showInConsole = false;
	public static boolean displayLog = true;
	public static boolean displayResults = true;
	public static boolean showAckLinks = false;
	
	public static boolean ack = false ;
	public static double ackType = Ber.PROBABILITY;
	public static double ackProba = 1.0 ;
	public static int protocol = SimulationInputs.CSMA ;
	
	public static boolean symmetricalLinks = true;
	public static int radioDetectionType = RadioDetection.SIMPLE_DETECTION;
}
