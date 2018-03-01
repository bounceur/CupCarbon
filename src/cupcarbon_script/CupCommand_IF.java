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

public class CupCommand_IF extends CupCommand {

	protected String arg = "" ;
	protected CupCommand_IF parent = null;
	protected boolean resultOfCondition = false;
	protected int ifIndex = -1;
	protected int elseIndex = -1;
	protected int endIfIndex = -1;
	
	public CupCommand_IF(CupScript script, String arg) {
		this.script = script ;
		this.arg = arg ;
		parent = null;
		resultOfCondition = false;
		ifIndex = -1;
		elseIndex = -1;
		endIfIndex = -1;
	}
	
	@Override
	public String execute() {		
		String condition = arg.replaceFirst("if", "");
		CupEvalCondition evalCondtion = new CupEvalCondition(script);
		
		CupConditionElement conditionElement = evalCondtion.initCondition(condition);
		resultOfCondition = conditionElement.evaluate();
		
		
		if (!resultOfCondition)
			if (elseIndex != -1) {
				script.setIndex(elseIndex);
			}
			else {
				script.setIndex(endIfIndex);
			}
		
		return "000";
	}
	
	public void setParent(CupCommand_IF parent){
		this.parent = parent;
	}

	public CupCommand_IF getParent() {
		return parent;
	}
	
	public boolean getRestultOfCondition() {
		return resultOfCondition ;
	}
	
	public void setRestultOfCondition(boolean resultOfCondition) {
		this.resultOfCondition = resultOfCondition;
	}
	
	public int getElseIndex() {
		return elseIndex;
	}

	public void setElseIndex(int elseIndex) {
		this.elseIndex = elseIndex;
	}

	public int getEndIfIndex() {
		return endIfIndex;
	}

	public void setEndIfIndex(int endIfIndex) {
		this.endIfIndex = endIfIndex;
	}

	
	public int getIfIndex() {
		return ifIndex;
	}

	public void setIfIndex(int ifIndex) {
		this.ifIndex = ifIndex;
	}
	
	@Override
	public String toString() {		
		return "IF";
	}
	
	public static void main(String [] args) {
		System.out.println((int)'0');
	}
}
