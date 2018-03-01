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

package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

/**
 * @author Molham Darwich
 * @version 1.0
 */

public class Command_SET extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	
	public Command_SET(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		if(arg1.equals("")) System.err.println("[CupCarbon ERROR] (S"+sensor.getId()+"): SET function ("+arg1+" is null)");
		if(arg2.equals("")) System.err.println("[CupCarbon ERROR] (S"+sensor.getId()+"): SET function -> error in ("+arg2+")");
		if(arg1==null) System.err.println("[CupCarbon ERROR] (S"+sensor.getId()+"): SET function ("+arg1+" is null)");
		if(arg2==null) System.err.println("[CupCarbon ERROR] (S"+sensor.getId()+"): SET function ("+arg2+" is null)");
		boolean contained = false;
		String arg = "";
		WisenSimulation.simLog.add("S" + sensor.getId() + " SET " + arg1 + "=" + arg2);
		String[] match={"(", ")", "+", "-", "*", "/", "%", "^"};
		int i = 0;
		while (!contained && i< match.length) {
			if (arg2.contains(match[i]))
					contained = true;
			i=i+1;
		}
		if (contained) { 
			SenScriptExpressionCalculate calculator = new SenScriptExpressionCalculate();
			arg = calculator.processInput(arg2, sensor);
		}
		else 
			arg = sensor.getScript().getVariableValue(arg2);
		sensor.getScript().addVariable(arg1, arg);
		return 0 ;
	}

	@Override
	public String toString() {
		return "SET";
	}
	
	@Override
	public String getArduinoForm() {
		String s = arg1 + " = " + arg2.replace("$", "") + ";";
		return s;
	}
	
}
