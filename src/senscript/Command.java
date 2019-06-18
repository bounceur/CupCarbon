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

package senscript;

import device.SensorNode;

/**
 * @author Ahcene Bounceur
 * @author Abdelhamid Zemirline
 * @version 2.0
 */

public abstract class Command {
	
	protected SensorNode sensor ;
	
	protected Command_IF currentIf = null;
	protected Command_WHILE currentWhile = null;
	protected Command_FOR currentFor = null;
	
	//protected boolean writtenInUART = false ;
	protected boolean executing = false ;
	
	public Command() {
		currentIf = null;
		currentWhile = null;
		currentFor = null;
	}
	
	public abstract double execute() ;
	
	public boolean isIf() {
		return false ;
	}
	
	public boolean isElse() {
		return false ;
	}
	
	public boolean isEndIf() {
		return false ;
	}
	
	public Command_IF getCurrentIf() {
		return currentIf;
	}

	public void setCurrentIf(Command_IF currentIf) {
		this.currentIf = currentIf;
	}
	
	
	public Command_WHILE getCurrentWhile() {
		return currentWhile;
	}

	public void setCurrentWhile(Command_WHILE currentWhile) {
		this.currentWhile = currentWhile;
	}
	
	public Command_FOR getCurrentFor() {
		return currentFor;
	}

	public void setCurrentFor(Command_FOR currentFor) {
		this.currentFor = currentFor;
	}
	
	public boolean isWait() {
		return false;
	}
	
	public boolean isDelay() {
		return false;
	}
	
	public boolean isPsend() {
		return false;
	}
	
	public boolean isSend() {
		return false;
	}
	
	public boolean visual() {
		return false;
	}
	
	public boolean isExecuting() {
		return executing;
	}
	
	public void setExecuting(boolean b) {		
		this.executing = b; 
	}
	
	public String getArduinoForm() {
		return "// ----";
	}	
	
	public String finishMessage() {
		return "";
	}

	public String getMessage() {
		return ""; 
	}
	
	@Override
	public String toString() {		
		return "----";
	}
	
}
