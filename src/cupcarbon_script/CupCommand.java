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
 * @version 1.0
 */

public abstract class CupCommand {

	protected CupCommand_IF currentIf = null;
	protected CupCommand_WHILE currentWhile = null;
	protected CupCommand_FOR currentFor = null;
	protected CupScript script;
	public static boolean isExecuted;
	public static boolean currentExecution;

	public CupCommand() {
		currentIf = null;
		currentWhile = null;
		currentFor = null;
	}

	public abstract String execute();

	public boolean isIf() {
		return false;
	}

	public boolean isElse() {
		return false;
	}

	public boolean isEndIf() {
		return false;
	}

	public CupCommand_IF getCurrentIf() {
		return currentIf;
	}

	public void setCurrentIf(CupCommand_IF currentIf) {
		this.currentIf = currentIf;
	}

	public CupCommand_WHILE getCurrentWhile() {
		return currentWhile;
	}

	public void setCurrentWhile(CupCommand_WHILE currentWhile) {
		this.currentWhile = currentWhile;
	}

	public CupCommand_FOR getCurrentFor() {
		return currentFor;
	}

	public void setCurrentFor(CupCommand_FOR currentFor) {
		this.currentFor = currentFor;
	}

	@Override
	public String toString() {
		return "----";
	}
}
