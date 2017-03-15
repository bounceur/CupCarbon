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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------
 * CupCarbon U-One is part of the research project PERSEPTEUR supported by the
 * French Agence Nationale de la Recherche ANR
 * under the reference ANR-14-CE24-0017-01.
 * ----------------------------------------------------------------------------------------------------------------
 **/

package cupcarbon_script;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

public class CupCondition_NOTEQUAL extends CupCondition {
	
	public CupCondition_NOTEQUAL(CupScript script, String arg1, String arg2){
		this.script = script ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}
	
	
	@Override
	public boolean evaluate() {	
		String s1 = script.getVariableValue(arg1);
		String s2 = script.getVariableValue(arg2);
		try {
			double v1 = Double.valueOf(s1);
			double v2 = Double.valueOf(s2);			
			return  (v1 != v2);
		}
		catch(NumberFormatException e) {			
			return  (!s1.equals(s2));
		}
	}


}
