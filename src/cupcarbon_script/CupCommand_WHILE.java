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

public class CupCommand_WHILE extends CupCommand {
	
	protected String arg = ""; 
	
	protected String left = "";
	protected String right = "";
	protected String ineq = "";
	
	protected int index;
	
	protected CupCommand_WHILE parent = null;
		
	public CupCommand_WHILE getParent() {
		return parent;
	}


	public void setParent(CupCommand_WHILE parent) {
		this.parent = parent;
	}

	public CupCommand_WHILE(CupScript script, String arg) {
		this.script = script ;
		this.arg = arg;
		
		String[] inst = CupCondition.getTwoParts(arg); 			

		left = inst[0];
		right = inst[1];
		ineq = inst[2];
	}
	
	public String getRight() {
		return right;
	}
	
	public String getLeft() {
		return left;
	}
	
	public String getIneq() {
		return ineq;
	}
	
	@Override
	public String execute() {
		index = script.getIndex();
		return "000";
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public String getArg() {
		return arg; 
	}

	@Override
	public String toString() {
		return "WHILE";
	}
}
