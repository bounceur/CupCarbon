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
 * This class allows changing the color of the links between the different objects on the map layer
 * by changing the color to the next or previous color 
 * Command examples:
 * color next
 * -> It changes the color of the links between the different objects to the next color
 * color prev
 * -> It changes the color of the links between the different objects to the previous color
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon_script;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

import map.MapLayer;


public class CupCommand_COLOR extends CupCommand {

	protected String option = "";
	
	//---------------------------------------------------------------------------------------------------------------------
	// Constructor 
	// ---------------------------------------------------------------------------------------------------------------------
	public CupCommand_COLOR(CupScript script, String option) {
		this.script = script ;
		this.option = option;
	}

	//---------------------------------------------------------------------------------------------------------------------
	// execute color
	// by changing the color to the next or previous color
	// ---------------------------------------------------------------------------------------------------------------------
	@Override
	public String execute() {
		currentExecution = false;
		String rep = "";
		String sOption = script.getVariableValue(option);
		if (isExecuted) {
			switch(sOption) {
			case ("next") : 
				MapLayer.nextLinkColor('v');
				MapLayer.repaint();
				rep = " 000 The Color has been Changed";
				currentExecution = true;
				break;
			case ("prev") : 
				MapLayer.nextLinkColor('V'); 
	 			MapLayer.repaint();
	 			rep = " 000 The Color has been Changed";
				currentExecution = true;
				break;
			default :
				rep = "001 [ERROR]";
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
		return "COLOR";
	}
}
