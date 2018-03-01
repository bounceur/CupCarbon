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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public final class CupScriptAddCommand {

	public static Stack<String> endof = new Stack<String>();

//	public static String detectKeyWord(String s) {
//		return s.replaceFirst("\\(", "(");
//	}

	public static void addCommand(CupScript script, String instStr) {
		
//		instStr = detectKeyWord(instStr);
		List<String> objectsList = new ArrayList<String>();

		
		String [] inst = instStr.split(" ");

		if (inst[0].split(":").length > 1) {
			script.addLabel(inst[0].split(":")[0], script.size() + 1);
			inst[0] = inst[0].split(":")[1];
		}

		CupCommand command = null;

		if (inst[0].toLowerCase().equals("end")) {
			instStr = endof.pop();
			addCommand(script, instStr);
		}

		if (inst[0].toLowerCase().equals("project")) {
			if(inst.length==2) command = new CupCommand_PROJECT(script, inst[1]);
			if(inst.length==4) command = new CupCommand_PROJECT(script, inst[1], inst[2], inst[3]);
		}
		
		if (inst[0].toLowerCase().equals("file")) {
			command = new CupCommand_FILE(script, inst[1], inst[2], inst[3]);
		}
		
		if (inst[0].toLowerCase().equals("network")) {
			command = new CupCommand_NETWORK(script);
		}
		
		if (inst[0].toLowerCase().equals("save")) {
			command = new CupCommand_SAVE(script);
		}
		
		if (inst[0].toLowerCase().equals("add")) {
			command = new CupCommand_ADD(script, inst[1], inst[2], inst[3], inst[3]);
		}
		
		if (inst[0].toLowerCase().equals("move")) {
			command = new CupCommand_MOVE(script, inst[1], inst[2], inst[3], inst[4], inst[5]);
		}
		
		if (inst[0].toLowerCase().equals("buildings")) {
			command = new CupCommand_BUILDINGS(script, inst[1]);
		}
		
		if (inst[0].toLowerCase().equals("generate")) {
			command = new CupCommand_GENERATE(script, inst[1], inst[2]);
		}
		
		if (inst[0].toLowerCase().equals("display")) {
			command = new CupCommand_DISPLAY(script, inst[1]);
		}
		
		if (inst[0].toLowerCase().equals("hide")) {
			command = new CupCommand_HIDE(script, inst[1]);
		}
		
		if (inst[0].toLowerCase().equals("color")) {
			command = new CupCommand_COLOR(script, inst[1]);
		}
		
		if (inst[0].toLowerCase().equals("delete")) {
			if(inst.length==2) command = new CupCommand_DELETE(script, inst[1]);
			if(inst.length> 2) {
				for (int i=2; i<inst.length; i++)
					objectsList.add(inst[i]);
				command = new CupCommand_DELETE(script, inst[1], objectsList);
			}
		}
		
		if (inst[0].toLowerCase().equals("map")) {
			command = new CupCommand_MAP(script, inst[1]);
		}
		
		if (inst[0].toLowerCase().equals("duplicate")) {
			if(inst.length==2) command = new CupCommand_DUPLICATE(script, inst[1]);
			if(inst.length> 2) {
				for (int i=2; i<inst.length; i++)
					objectsList.add(inst[i]);
				command = new CupCommand_DUPLICATE(script, inst[1], objectsList);
			}
		}
		
		if (inst[0].toLowerCase().equals("insert")) {
				for (int i=2; i<inst.length; i++)
					objectsList.add(inst[i]);
				command = new CupCommand_INSERT(script, inst[1], objectsList);
		}
		
		if (inst[0].toLowerCase().equals("radio")) {
			if(inst.length==5) command = new CupCommand_RADIO(script, inst[1], inst[2], inst[3], inst[4]);
			if(inst.length==4) command = new CupCommand_RADIO(script, inst[1], inst[2], inst[3], "");
		}
		
		if (inst[0].toLowerCase().equals("select")) {
			
			if(inst.length==2) command = new CupCommand_SELECT(script, inst[1]);
			if(inst.length> 2) {
				for (int i=2; i<inst.length; i++)
					objectsList.add(inst[i]);
				command = new CupCommand_SELECT(script, inst[1], objectsList);
			}
		}
		
		if (inst[0].toLowerCase().equals("deselect")) {
			if(inst.length==2) command = new CupCommand_DESELECT(script, inst[1]);
			if(inst.length> 2) {
				for (int i=2; i<inst.length; i++)
					objectsList.add(inst[i]);
				command = new CupCommand_DESELECT(script, inst[1], objectsList);
			}
		}
		
		if (inst[0].toLowerCase().equals("setparameter")) {
			if(inst.length==5) command = new CupCommand_SETPARAMETER(script, inst[1], inst[2], "", inst[3], inst[4]);
			if(inst.length==6) command = new CupCommand_SETPARAMETER(script, inst[1], inst[2], inst[3], inst[4], inst[5]);
		}
		
		if (inst[0].toLowerCase().equals("getparameter")) {
			if(inst.length==5) command = new CupCommand_GETPARAMETER(script, inst[1], inst[2], "", inst[3], inst[4]);
			if(inst.length==6) command = new CupCommand_GETPARAMETER(script, inst[1], inst[2], inst[3], inst[4], inst[5]);
		}
		
		if (inst[0].toLowerCase().equals("simulation")) {
			if(inst.length==4) command = new CupCommand_SIMULATION(script, inst[1], inst[2], inst[3]);
			if(inst.length==2) command = new CupCommand_SIMULATION(script, inst[1]);
		}
		
		if (inst[0].toLowerCase().equals("plus")) {
			command = new CupCommand_PLUS(script, inst[1], inst[2], inst[3]);
		}
		
		if (inst[0].toLowerCase().equals("quit")) {
			command = new CupCommand_QUIT(script);
		}
		
		if (inst[0].toLowerCase().equals("reset")) {
			command = new CupCommand_RESET(script);
		}
		
		if (inst[0].toLowerCase().equals("init_id")) {
			command = new CupCommand_INIT_ID(script);
		}
		
		if (inst[0].toLowerCase().equals("set")) {
			command = new CupCommand_SET(script, inst[1], inst[2]);
		}
		
		if (inst[0].toLowerCase().equals("println")) {
			command = new CupCommand_PRINTLN(script, inst);
		}

		if (inst[0].toLowerCase().equals("while")) {
			endof.push("endwhile");
			CupCommand_WHILE commandWhile = new CupCommand_WHILE(script, instStr);
			if (script.getCurrentWhile() != null) {
				commandWhile.setParent(script.getCurrentWhile());
			}
			script.add(commandWhile);
			script.setCurrentWhile(commandWhile);
		}
		if (inst[0].toLowerCase().equals("endwhile")) {
			CupCommand_ENDWHILE commandWEndhile = new CupCommand_ENDWHILE(script);
			commandWEndhile.setCurrentWhile(script.getCurrentWhile());
			script.removeCurrentWhile();
			script.add(commandWEndhile);
		}
		
		if (inst[0].toLowerCase().equals("for")) {
			endof.push("endfor");
			CupCommand_FOR cmdFor = null;
			if (inst.length == 4)
				cmdFor = new CupCommand_FOR(script, inst[1], inst[2], inst[3], "1");
			if (inst.length == 5)
				cmdFor = new CupCommand_FOR(script, inst[1], inst[2], inst[3], inst[4]);
			if (script.getCurrentFor() != null) {
				cmdFor.setParent(script.getCurrentFor());
			}
			script.add(cmdFor);
			script.setCurrentFor(cmdFor);
		}
		if (inst[0].toLowerCase().equals("endfor")) {
			CupCommand_ENDFOR cmdEndFor = new CupCommand_ENDFOR(script);
			cmdEndFor.setCurrentFor(script.getCurrentFor());
			script.removeCurrentFor();
			script.add(cmdEndFor);
		}

		if (inst[0].toLowerCase().equals("if")) {
			endof.push("endif");
			CupCommand_IF commandIf = new CupCommand_IF(script, instStr);
			if (script.getCurrentIf() != null) {
				commandIf.setParent(script.getCurrentIf());
			}
			script.setCurrentIf(commandIf);
		}

		if (inst[0].toLowerCase().equals("else")) {
			command = new CupCommand_ELSE(script);
			script.getCurrentIf().setElseIndex(script.size());
		}

		if (inst[0].toLowerCase().equals("endif")) {
			command = new CupCommand_ENDIF(script);
			script.getCurrentIf().setEndIfIndex(script.size());
			script.removeCurrentIf();
		}

		if (inst[0].toLowerCase().equals("goto")) {
			command = new CupCommand_GOTO(script, inst[1]);
		}

		if (command != null) {
			script.add(command);
			command.setCurrentIf(script.getCurrentIf());
			command.setCurrentWhile(script.getCurrentWhile());
			command.setCurrentFor(script.getCurrentFor());
		}

	}

}
