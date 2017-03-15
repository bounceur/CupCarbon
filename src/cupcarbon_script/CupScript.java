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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

public class CupScript {

	protected LinkedList<CupCommand> commands = new LinkedList<CupCommand>();
	protected LinkedList<String> scommands = new LinkedList<String>();
	
	public static FileOutputStream fis ;
	public static PrintStream slog ;

	protected int index = 0;
	protected int loopIndex = 0;
	protected boolean stopped = false;
	protected int level;
	protected CupScript parent;
	protected CupCommand_IF currentIf = null;
	protected CupCommand_WHILE currentWhile = null;
	protected CupCommand_FOR currentFor = null;

	protected CupVariableList variables;
	protected Hashtable<String, Integer> labels;
	
	public CupScript() {
		index = 0;
		variables = new CupVariableList();
		labels = new Hashtable<String, Integer>();		
		try {						
			fis = new FileOutputStream("log");
			slog = new PrintStream(fis);
			slog.println("Script");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void add(CupCommand command) {
		commands.add(command);
	}
	
	public void sAdd(String scommand) {
		scommands.add(scommand);
	}

	public void next() {
		if (!stopped) {			
			index++;
			if (index >= commands.size()) {
				index = loopIndex;
			}
		}
	}
	
	public String nextAndExecute() {
		if (!stopped) {			
			index++;
			if (index >= commands.size()) {
				index = loopIndex;
			}
			return executeCommand();
		}
		return "111";
	}

	public void previous() {
		if (!stopped) {
			index--;
			if (index < 0) {
				index = 0;
			}
		}
	}

	public void init() {
		variables = new CupVariableList();
		index = 0;
		loopIndex = 0;
		stopped = false;
		level = 0;
	}

	public int size() {
		return commands.size();
	}

	public CupCommand getCurrent() {
		return commands.get(index);
	}

	@Override
	public String toString() {
		String s = "";
		Iterator<CupCommand> it = commands.iterator();
		while (it.hasNext()) {
			s += it.next() + "\n";
		}
		return s;
	}

	public String executeCommand() {
		return getCurrent().execute();
	}

	public void addVariable(String s1, String s2) {
		variables.put(s1, s2);
	}

	public void putVariable(String var, String s) {
		variables.put(var, s);
	}

	public String getVariableValue(String arg) {
		if (arg.equals("\\"))
			return "";
		if (arg.equals(""))
			return "";
		if (arg.charAt(0) == '$')
			return variables.getValue(arg.substring(1));
		return arg;
	}

	public void variablesToValues(String[] args) {
		for (int i = 0; i < args.length; i++)
			if (args[i].charAt(0) == '$')
				args[i] = variables.getValue(args[i].substring(1));
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setIndexToLoopIndex() {
		loopIndex = index;
	}

	public void setBreaked(boolean b) {
		stopped = b;
	}

	public int getIndex() {
		return index;
	}

	public int getLevel() {
		return level;
	}

	public CupScript getParent() {
		return parent;
	}

	public void setCurrentIf(CupCommand_IF command) {
		currentIf = command;
		add(command);
	}

	public CupCommand_IF getCurrentIf() {
		return currentIf;
	}

	public CupCommand_IF removeCurrentIf() {
		currentIf = currentIf.getParent();
		return currentIf;
	}

	public CupCommand_WHILE getCurrentWhile() {
		return currentWhile;
	}

	public void setCurrentWhile(CupCommand_WHILE currentWhile) {
		this.currentWhile = currentWhile;
	}

	public CupCommand_WHILE removeCurrentWhile() {
		currentWhile = currentWhile.getParent();
		return currentWhile;
	}

	public CupCommand_FOR getCurrentFor() {
		return currentFor;
	}

	public void setCurrentFor(CupCommand_FOR currentFor) {
		this.currentFor = currentFor;
	}

	public CupCommand_FOR removeCurrentFor() {
		currentFor = currentFor.getParent();
		return currentFor;
	}

	public LinkedList<CupCommand> getCommands() {
		return commands;
	}

	public boolean variableExist(String var) {
		return variables.exist(var);
	}

	public void removeVar(String var) {
		variables.remove(var);
	}

	public void displayVars() {
		System.out.println(variables);
	}

	public void addLabel(String label, int lineNumber) {
		labels.put(label, lineNumber);
	}

	public int getLineOfLabel(String label) {
		return labels.get(label);
	}
	
	public void display() {
		for(CupCommand command : commands) {
			System.out.println(command);
		}
	}
	
	public void sDisplay() {
		for(String scommand : scommands) {
			System.out.println(scommand);
		}
	}
	
	public static void close() {		
		slog.close();
		try {
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}