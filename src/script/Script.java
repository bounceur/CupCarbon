/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2015 Ahcene Bounceur
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

package script;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import device.SensorNode;

public class Script {

	protected LinkedList<Command> commands = new LinkedList<Command>();
	protected SensorNode sensor = null;
	protected int index = 0;
	protected int loopIndex = 0;
	protected long event = Long.MAX_VALUE - 1;
	protected boolean breaked = false;
	protected int level;
	protected Script parent;
	protected Command_IF currentIf = null;
	protected Command_WHILE currentWhile = null;
	protected Command_FOR currentFor = null;	
	
	protected VariableList variables ; 
	protected TabList tables;
	protected Hashtable<String, Integer> labels;

	protected boolean waiting = false;
	
	public Script(SensorNode sensor) {		
		this.sensor = sensor;
		index = 0;
		variables = new VariableList();
		tables = new TabList();
		labels = new Hashtable<String, Integer>();
		//System.out.println("--------->"+labels.size());
	}
	
	public void add(Command command) {
		commands.add(command);
	}

	public void next() {
		//System.out.println("NEXT -> " + sensor.getId());
		if (!breaked) {
			index++;
			if (index >= commands.size()){
				index = loopIndex;
			}
		}		
	}
	
	public void previous() {
		//System.out.println("NEXT -> " + sensor.getId());
		if (!breaked) {
			index--;
			if (index < 0){
				index = 0;
			}
		}		
	}

	public void init() {
		//labels = new Hashtable<String, Integer>();
		variables = new VariableList();
		sensor.initBuffer();		
		tables = new TabList();
		index = 0;
		waiting = false;
		loopIndex = 0;
		event = Integer.MAX_VALUE - 1;
		breaked = false;
		level = 0;
	}
	
	public int size(){
		return commands.size();
	}
	
	public Command getCurrent() {
		return commands.get(index);
	}

	@Override
	public String toString() {
		String s = "";
		Iterator<Command> it = commands.iterator();
		while (it.hasNext()) {
			s += it.next() + "\n";
		}
		return s;
	}

	public void executeCommand() {
		event = getCurrent().execute();
		waiting = false;
	}

	public Long getEvent() {
		return event;
	}
		
//	public int getNextIteration() {
//		int index = this.index;
//		while (index < commands.size()) {
//			if (commands.get(index).isElse() || commands.get(index).isEndIf())
//				return index;
//			index++;
//		}
//		return this.index;
//	}
	
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
		if(arg.charAt(0)=='$')
			return variables.getValue(arg.substring(1));
		return arg;
	}
	
	public void variablesToValues(String [] args) {
		for(int i=0; i<args.length; i++)
			if(args[i].charAt(0)=='$')
				args[i] = variables.getValue(args[i].substring(1));
	}	

	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setIndexToLoopIndex() {
		loopIndex = index;
	}

	public void setBreaked(boolean b) {
		breaked = b;
	}

	public void setWiting(boolean b) {
		waiting = b;
	}
	
	public int getIndex() {
		return index ;
	}
	
	public int getLevel(){
		return level;
	}
	
	public Script getParent(){
		return parent;
	}
	
	public void setCurrentIf(Command_IF command){
		currentIf = command;
		add(command);
	}
	
	public Command_IF getCurrentIf(){
		return currentIf;
	}
	
	public Command_IF removeCurrentIf(){
		currentIf = currentIf.getParent();
		return currentIf;
	}
	
	public Command_WHILE getCurrentWhile() {
		return currentWhile;
	}

	public void setCurrentWhile(Command_WHILE currentWhile) {
		this.currentWhile = currentWhile;
	}
	
	public Command_WHILE removeCurrentWhile(){
		currentWhile = currentWhile.getParent();
		return currentWhile;
	}
	
	public Command_FOR getCurrentFor() {
		return currentFor;
	}

	public void setCurrentFor(Command_FOR currentFor) {
		this.currentFor = currentFor;
	}
		
	public Command_FOR removeCurrentFor() {
		currentFor = currentFor.getParent();
		return currentFor;
	}
	
	public void putTable(String name, int heigth, int width) {
		tables.putTable(name, heigth, width);
	}

	public Object[][] getTable(String tabName) {
		return  tables.getTable(tabName);
	}
	
	public LinkedList<Command> getCommands() {
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
}