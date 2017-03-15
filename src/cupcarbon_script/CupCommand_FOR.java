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

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

package cupcarbon_script;

/**
 * @author Ahcene Bounceur
 * @author Molham Darwish
 * @version 1.0
 */

public class CupCommand_FOR extends CupCommand {
	
	protected String arg1 ;
	protected String arg2 ;
	
	protected String sStep ;
	protected double step ;
	
	protected String left = "";
	protected String right ;
	
	protected boolean trueCondition = true;
	
	protected boolean first = true ;
	protected boolean exist = false ;
	
	protected int index;
	
	protected CupCommand_FOR parent = null;
	
	public boolean isCondition() {
		return trueCondition;
	}

	public void setCondition(boolean isCondition) {
		this.trueCondition = isCondition;
	}
	
	public CupCommand_FOR getParent() {
		return parent;
	}

	public void setParent(CupCommand_FOR parent) {
		this.parent = parent;
	}

	public CupCommand_FOR(CupScript script, String arg1, String arg2, String arg3, String arg4) {
		// for i 0 10 1
		this.script = script ; 	
		
		trueCondition = true;
		first = true ;
		exist = false ;
				
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		left = "$"+arg1;	
		right = arg3;
		sStep = arg4;
		
	}
	
	public String getRight() {
		return right;
	}
	
	public String getLeft() {
		return left;
	}
	
	public double getStep() {
		return step ;
	}

	@Override
	public String execute() {
		if (first) {
			step = Double.valueOf(script.getVariableValue(sStep));
			first = false ;
			exist = script.variableExist(arg1);
			String arg = script.getVariableValue(arg2);
			script.addVariable(arg1, arg);			
		}
		index = script.getIndex();
		return "000";
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public void removeVar() {
		script.removeVar(arg1);
	}
	
	public void init() {
		first = true ;		
		if(!exist) {
			String arg = script.getVariableValue(arg2);
			script.addVariable(arg1, arg);
			script.removeVar(arg1);
		}
	}
			
	@Override
	public String toString() {
		return "FOR";
	}
}
