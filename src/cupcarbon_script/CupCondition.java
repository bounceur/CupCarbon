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

public abstract class CupCondition {

	protected CupScript script ;
	protected String arg1 = "";
	protected String arg2 = "";
	
	public CupCondition() {}
	
	public abstract boolean evaluate() ;
	
	public static String [] getTwoParts(String condition) {
		condition = condition.replaceAll(" ", "");
		int frst = condition.indexOf("(");
		int last = condition.lastIndexOf(")");
		if (frst !=-1 && last !=-1)
			condition = condition.substring(frst+1,last);
		
		String [] tReturn = new String [3];
		String[] tCondition = condition.split("<=",2);
		
		tReturn[2] = "<="; 
		if(tCondition.length==1) {
			tCondition = tCondition[0].split(">=");
			tReturn[2] = ">=";
		}
		if(tCondition.length==1) {
			tCondition = tCondition[0].split("==");
			tReturn[2] = "==";
		}
		if(tCondition.length==1) {
			tCondition = tCondition[0].split("!=");
			tReturn[2] = "!=";
		}
		if(tCondition.length==1) {
			tCondition = tCondition[0].split(">");
			tReturn[2] = ">";
		}
		if(tCondition.length==1) {
			tCondition = tCondition[0].split("<");
			tReturn[2] = "<";
		}
		tReturn[0] = tCondition[0];
		tReturn[1] = tCondition[1].equals("\\")?"":tCondition[1]; 
		
		return tReturn;
	}
	
	@Override
	public String toString() {		
		return "-";
	}
	
}
