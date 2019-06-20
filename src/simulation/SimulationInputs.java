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

package simulation;

import radio_module.ErrorBits;
import radio_module.RadioDetection;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 * 
 * The necessary parameters for the simulation
 */
public class SimulationInputs {
	
	public static final int NONE = 0;
	public static final int CSMA = 1;	
	
	public static boolean clockDrift = false;
	
	public static boolean mobilityAndEvents = false;
	public static double simulationTime = 86400.0;
	public static double resultsWritingPeriod = 0.1;  // The period in seconds, of writing the battery level in the results csv file
	
	public static int visualDelay = 10;
	public static int arrowsDelay = 50;
	public static boolean showInConsole = false;
	public static boolean displayLog = false;
	public static boolean displayResults = false;
	public static boolean showAckLinks = false;
	
	public static boolean ack = false ;
	public static double ackType = ErrorBits.PROBABILITY;	// Type of the interference calculation
	public static double ackProba = 1.0 ;
	
	public static boolean macLayer = false ;
	public static double macProba = 1.0 ;
	
	public static boolean symmetricalLinks = false;
	public static int radioDetectionType = RadioDetection.SIMPLE_DETECTION;
	public static boolean visibility = false; 
}
