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
 * This class allows to stop the execution of CupCarbon script and exit the script execution
 * 
 * Command examples:
 * stop
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */


public class CupCommand_QUIT extends CupCommand {
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_QUIT(CupScript script) {
		this.script = script ;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute stop
	// It stops the execution of CupCarbon script and exit the script execution
	// ---------------------------------------------------------------------------------------------------------------------
	@Override
	public String execute() {
		String rep = "";
		if (isExecuted) {
			script.setBreaked(true);
			rep = "111 STOP";
		}
		else 
			rep = "001 [ERROR] Verify the Script";
		System.out.println(rep);
		CupScript.slog.println(rep);
		return rep;
	}
	
	@Override
	public String toString() {
		return "QUIT";
	}
}
